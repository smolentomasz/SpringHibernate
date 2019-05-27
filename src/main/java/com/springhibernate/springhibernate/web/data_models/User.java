package com.springhibernate.springhibernate.web.data_models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;
    private String username;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Survey> survey = new HashSet<>();

    public User(String username) {
        this.username = username;
    }
    public User(){}

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Answer> answer = new HashSet<>();

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
