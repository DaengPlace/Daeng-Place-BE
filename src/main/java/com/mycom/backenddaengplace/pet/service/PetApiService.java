package com.mycom.backenddaengplace.pet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.dto.response.BreedTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PetApiService {

    @Value("${dog.api.key}")
    private String dogApiKey;

    private final RestTemplate restTemplate;
    private final String DOG_API_URL = "https://api.thedogapi.com/v1/breeds";

    public List<BreedTypeResponse> getAllBreedTypes() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", dogApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                DOG_API_URL,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );

        return StreamSupport.stream(response.getBody().spliterator(), false)
                .map(breed -> BreedTypeResponse.builder()
                        .breedTypeId(breed.get("id").asLong())
                        .breedType(breed.get("name").asText())
                        .build())
                .collect(Collectors.toList());
    }

    public String getBreedById(Long breedId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", dogApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                DOG_API_URL + "/" + breedId,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );

        return response.getBody().get("name").asText();
    }
}
