package com.mycom.backenddaengplace.pet.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BreedSearchResponse {
    private int totalCount;
    private List<BreedTypeResponse> breeds;
}
