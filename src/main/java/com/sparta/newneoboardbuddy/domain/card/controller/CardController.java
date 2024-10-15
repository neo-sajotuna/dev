package com.sparta.newneoboardbuddy.domain.card.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/{listId}")
public class CardController {
    private final CardService cardService;


    @PostMapping("/cards")
    public ResponseEntity<CardCreateResponse> createCard (
            @PathVariable Long listId,
            @RequestBody CardCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return ResponseEntity.ok(cardService.createCard(listId ,authUser, request));
    }


}
