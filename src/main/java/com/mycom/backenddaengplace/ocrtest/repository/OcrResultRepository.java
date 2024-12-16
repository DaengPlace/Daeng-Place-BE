package com.mycom.backenddaengplace.ocrtest.repository;

import com.mycom.backenddaengplace.ocrtest.domain.OcrResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {
}
