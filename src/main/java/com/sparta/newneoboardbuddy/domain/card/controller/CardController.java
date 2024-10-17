package com.sparta.newneoboardbuddy.domain.card.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardUpdateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardDetailResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardUpdateResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;


    /**
     * 카드를 생성하는 Controller 메서드
     * @param listId Card를 생성할 List Id
     * @param request Card생성에 필요한 정보가 담긴 Request
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @return 카드 생성 성공시 : 200 OK + 생성된 Card 정보 / 그 외 : ErrorCode + Description
     */
    @PostMapping("/list/{listId}/cards")
    public ResponseEntity<CardCreateResponse> createCard (
            @PathVariable Long listId,
            @ModelAttribute CardCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.ok(cardService.createCard(listId ,authUser, request));
    }

    /**
     * 카드 정보를 수정하는 Controller 메서드
     * @param cardId 수정할 Card Id
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @param request 카드 수정에 필요한 Request 정보
     * @return 수정 성공시 : 200 OK + 수정된 카드 정보 / 그 외 : ErrorCode + Description
     */
    @PutMapping ("/cards/{cardId}")
    public ResponseEntity<CardUpdateResponse> updateCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CardUpdateRequest request){
        return ResponseEntity.ok(cardService.updateCard(cardId, authUser, request));
    }

    /**
     * 해당 Id를 가진 카드를 조회하는 Controller 메서드
     * @param cardId 조회할 Card Id
     * @return 조회 성공시 : 200 OK + 조회한 카드 정보 / 그 외 : ErrorCode + Description
     */
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardDetailResponse> getCard(@PathVariable Long cardId){
        return ResponseEntity.ok(cardService.getCardDetails(cardId));
    }

    /**
     * 해당 Id를 가진 카드를 삭제하는 Controller 메서드
     * @param cardId 삭제할 Card Id
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @return 삭제 성공시 : 200 OK + 메시지 / 그 외 : ErroCode + Description
     */
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId, @AuthenticationPrincipal AuthUser authUser){
       cardService.deleteCard(cardId, authUser);
       return ResponseEntity.ok("Deleted");
    }

    /**
     * Param인자 내용 중 Null이 아닌 값 모두 만족하는 Card 정보를 반환하는 Controller 메서드
     * @param cardTitle Card.title에 cardTitle의 내용이 들어 있는지 확인할 문자열
     * @param cardContent Card.content에 cardContent의 내용이 들어 있는지 확인할 문자열
     * @param assignedMemberId Card.userId에 assignedMemberId와 일치 여부를 확인할 Card와 동일한 Space에 소속인 UserId
     * @param boardId Card.board.boardId와 일치 여부를 확인할 boardId
     * @param pageable 페이징 조건
     * @return 조회 성공시 : 200 OK + 페이징 된 조건에 맞는 CardList / 그 외 : ErrorCode + Description
     */
    @GetMapping("/cards/search")
    public ResponseEntity<Page<CardCreateResponse>> searchCards(
            @RequestParam(required = false) String cardTitle,
            @RequestParam(required = false) String cardContent,
            @RequestParam(required = false) Long assignedMemberId,
            @RequestParam(required = false) Long boardId,
            @PageableDefault(size=10, page=0) Pageable pageable
    ){
        return ResponseEntity.ok(cardService.searchCards(cardTitle, cardContent, assignedMemberId, boardId, pageable));
    }

}
