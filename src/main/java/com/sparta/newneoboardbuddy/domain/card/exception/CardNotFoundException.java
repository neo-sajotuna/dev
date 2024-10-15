package com.sparta.newneoboardbuddy.domain.card.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class CardNotFoundException extends CommonException {
    public CardNotFoundException(HttpStatus httpStatus) {
        super(httpStatus, "카드 정보를 찾을 수 없습니다.");
    }
}
