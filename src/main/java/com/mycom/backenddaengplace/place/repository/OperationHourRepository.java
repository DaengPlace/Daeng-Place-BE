package com.mycom.backenddaengplace.place.repository;

import com.mycom.backenddaengplace.place.domain.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {
    OperationHour findByPlaceId(Long placeId);
}
