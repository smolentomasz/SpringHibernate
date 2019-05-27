package com.springhibernate.springhibernate.web.repositories;

import com.springhibernate.springhibernate.web.data_models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
