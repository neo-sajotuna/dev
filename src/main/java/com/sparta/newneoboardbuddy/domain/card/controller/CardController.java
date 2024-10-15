package com.sparta.newneoboardbuddy.domain.card.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardUpdateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardDetailResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardUpdateResponse;
import com.sparta.newneoboardbuddy.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody CardCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam("file") MultipartFile file
    ){
        return ResponseEntity.ok(cardService.createCard(listId ,authUser, request, file));
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


}
