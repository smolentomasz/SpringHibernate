package com.springhibernate.springhibernate.web.data_models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long survey_id;
    private String survey_title;
    private String question;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    private Set<Answer> answer = new HashSet<>();

    @ManyToOne
    private User user;

    public Survey(String survey_title, String question, User user) {
        this.survey_title = survey_title;
        this.question = question;
        this.user = user;
    }
    public Survey(){}

    public long getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(long survey_id) {
        this.survey_id = survey_id;
    }

    public String getSurvey_title() {
        return survey_title;
    }

    public void setSurvey_title(String survey_title) {
        this.survey_title = survey_title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
