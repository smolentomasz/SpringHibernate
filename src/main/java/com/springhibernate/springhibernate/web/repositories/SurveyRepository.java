package com.springhibernate.springhibernate.web.repositories;

import com.springhibernate.springhibernate.web.data_models.Survey;
import com.springhibernate.springhibernate.web.data_models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByUser(User user);
}
