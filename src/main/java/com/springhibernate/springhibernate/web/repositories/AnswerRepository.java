package com.springhibernate.springhibernate.web.repositories;

import com.springhibernate.springhibernate.web.data_models.Answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
