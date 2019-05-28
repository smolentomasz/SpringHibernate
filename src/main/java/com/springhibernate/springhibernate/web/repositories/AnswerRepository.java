package com.springhibernate.springhibernate.web.repositories;

import com.springhibernate.springhibernate.web.data_models.Answer;
import com.springhibernate.springhibernate.web.data_models.Survey;
import com.springhibernate.springhibernate.web.data_models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByUser(User user);
    List<Answer> findBySurvey(Survey survey);
}
