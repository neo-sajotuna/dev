package com.sparta.newneoboardbuddy.domain.comment.service;


import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.exception.CardNotFoundException;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentUpdateRequestDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.response.CommentUpdateResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import com.sparta.newneoboardbuddy.domain.comment.exception.CommentNotFoundException;
import com.sparta.newneoboardbuddy.domain.comment.exception.NotAuthorException;
import com.sparta.newneoboardbuddy.domain.comment.repository.CommentRepository;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.sparta.newneoboardbuddy.domain.user.entity.User.fromAuthUser;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CardRepository cardRepository;

    private final MemberService memberService;

    @Transactional
    public CommentSaveResponseDto saveComment(AuthUser authUser, CommentSaveRequestDto commentSaveRequestDto) {

        // 카드 유효성 검사
        Card card = getCardById(commentSaveRequestDto.getCardId());

        // 권한 체크
        memberService.memberPermission(authUser, card.getWorkspace().getSpaceId());

        // 유저 엔티티 변환
        User user = fromAuthUser(authUser);

        // 새로운 댓글 저장을 위한 인스턴스 생성
        Comment comment = Comment.builder()
                .commentId(commentSaveRequestDto.getCardId())
                .card(card)
                .user(user)
                .comment(commentSaveRequestDto.getComment())
                .emoji(commentSaveRequestDto.getEmoji())
                .build();

        try {
            // 댓글 저장
            Comment newComment = commentRepository.save(comment);

            // 저장된 댓글을 response 로 변환
            return CommentSaveResponseDto.builder()
                    .commentId(newComment.getCommentId())
                    .comment(newComment.getComment())
                    .emoji(newComment.getEmoji())
                    .createdAt(newComment.getCreatedAt())
                    .build();

        } catch (RuntimeException e) {
            throw new RuntimeException("저장도중 알수없는 오류가 발생하였습니다. 다시 시도해주세요.");
        }

    }

    @Transactional
    public CommentUpdateResponseDto updateComment(AuthUser authUser, Long commentId, CommentUpdateRequestDto commentUpdateRequestDto) {

        // comment 객체 생성
        Comment comment = getCommentById(commentId);

        // 권한 체크
        memberService.memberPermission(authUser, comment.getCard().getWorkspace().getSpaceId());

        // 자기가 작성한 글을 수정하는지 확인
        checkWriter(authUser.getId(), comment.getUser().getId());

        // 업데이트
        if (commentUpdateRequestDto.getComment() != null && !commentUpdateRequestDto.getComment().trim().isEmpty()) {
            comment.setComment(commentUpdateRequestDto.getComment());
        }

        if (commentUpdateRequestDto.getEmoji() != null && !commentUpdateRequestDto.getEmoji().trim().isEmpty()) {
            comment.setEmoji(commentUpdateRequestDto.getEmoji());
        }

        // 업데이트 된 comment 객체 response 로 변환
        return CommentUpdateResponseDto.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .emoji(comment.getEmoji())
                .updatedAt(comment.getModifiedAt())
                .build();

    }

    @Transactional
    public void deleteComment(AuthUser authUser, Long commentId) {

        // comment 객체 생성
        Comment comment = getCommentById(commentId);

        // 권한 체크
        memberService.memberPermission(authUser, comment.getCard().getWorkspace().getSpaceId());

        // 자기가 작성한 글을 수정하는지 확인
        checkWriter(authUser.getId(), comment.getUser().getId());

        try {
            // comment 삭제
            commentRepository.delete(comment);
        } catch (RuntimeException e){
            throw new RuntimeException("댓글 삭제 중 알수없는 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    // commentid 로 comment 가 있는지 확인
    private Comment getCommentById(Long commentId){
        return commentRepository.findByCommentId(commentId).orElseThrow(
                () -> new CommentNotFoundException(HttpStatus.BAD_REQUEST)
        );
    }

    // cardId 로 card 가 있는지 확인
    private Card getCardById(Long cardId){
        return cardRepository.findByCardId(cardId).orElseThrow(
                () -> new CardNotFoundException(HttpStatus.BAD_REQUEST)
        );
    }

    // 자기가 쓴글을 수정하려는지 확인
    private void checkWriter(Long authUserId, Long userId){
        if(!authUserId.equals(userId)){
            throw new NotAuthorException(HttpStatus.BAD_REQUEST);
        }
    }
}
