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


    @PostMapping("/list/{listId}/cards")
    public ResponseEntity<CardCreateResponse> createCard (
            @PathVariable Long listId,
            @ModelAttribute CardCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.ok(cardService.createCard(listId ,authUser, request));
    }

    @PutMapping ("/cards/{cardId}")
    public ResponseEntity<CardUpdateResponse> updateCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CardUpdateRequest request){
        return ResponseEntity.ok(cardService.updateCard(cardId, authUser, request));
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardDetailResponse> getCard(@PathVariable Long cardId){
        return ResponseEntity.ok(cardService.getCardDetails(cardId));
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId, @AuthenticationPrincipal AuthUser authUser){
       cardService.deleteCard(cardId, authUser);
       return ResponseEntity.ok("Deleted");
    }


    @GetMapping("/cards/search")
    public ResponseEntity<Page<Card>> searchCards(
            @RequestParam(required = false) String cardTitle,
            @RequestParam(required = false) String cardContent,
            @RequestParam(required = false) Long assignedMemberId,
            @RequestParam(required = false) Long boardId,
            @PageableDefault(size=10, page=0) Pageable pageable
    ){
        return ResponseEntity.ok(cardService.searchCards(cardTitle, cardContent, assignedMemberId, boardId, pageable));
    }

}
