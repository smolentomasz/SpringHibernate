package com.springhibernate.springhibernate.web.repositories;

import com.springhibernate.springhibernate.web.data_models.Answer;
import com.springhibernate.springhibernate.web.data_models.Survey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findBySurvey(Survey survey);
}
