package com.mycom.backenddaengplace.pet.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BreedSearchRequest {
    private String keyword;

    public static BreedSearchRequest of(String keyword) {
        return BreedSearchRequest.builder()
                .keyword(keyword)
                .build();
    }
}
