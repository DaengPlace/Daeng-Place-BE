package com.mycom.backenddaengplace.member.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }

        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
            }
        }

        return Gender.valueOf(value);  // FEMALE, MALE, OTHER 문자열도 처리
    }
}
