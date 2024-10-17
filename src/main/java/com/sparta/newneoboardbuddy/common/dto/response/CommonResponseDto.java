package com.sparta.newneoboardbuddy.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponseDto<T> {

    private final int statusCode;

    private final String message;

    private final T data;

    public static <T> CommonResponseDto <T> success(T data) {
        return new CommonResponseDto<>(200, "success", data);
    }

    public static CommonResponseDto<String> fail(int statusCode, String message) {
        return new CommonResponseDto<>(statusCode, message, null);
    }
}
