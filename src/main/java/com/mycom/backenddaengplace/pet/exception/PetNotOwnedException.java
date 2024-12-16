package com.mycom.backenddaengplace.pet.exception;

public class PetNotOwnedException extends RuntimeException {
    public PetNotOwnedException(Long memberId, Long petId) {
        super(String.format("회원(ID: %d)은 해당 반려견(ID: %d)의 소유자가 아닙니다.", memberId, petId));
    }
}
