package com.luckyb.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewDeleteResponse {
    private  boolean success;
    private DeleteData data;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DeleteData{
        private  String message;
    }

}
