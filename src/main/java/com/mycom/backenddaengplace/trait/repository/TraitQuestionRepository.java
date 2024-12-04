package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.TraitQuestion;
import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TraitQuestionRepository extends JpaRepository<TraitQuestion, Long> {

    List<TraitQuestion> findByTarget(QuestionTarget target);

}
