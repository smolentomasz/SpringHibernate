package com.springhibernate.springhibernate.web.controllers;

import com.springhibernate.springhibernate.web.data_models.Answer;
import com.springhibernate.springhibernate.web.data_models.Survey;
import com.springhibernate.springhibernate.web.data_models.User;
import com.springhibernate.springhibernate.web.repositories.AnswerRepository;
import com.springhibernate.springhibernate.web.repositories.SurveyRepository;
import com.springhibernate.springhibernate.web.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SpringHibernateController {

    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;

    @GetMapping("/user")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/survey")
    public List<Survey> allSurveys() {
        return surveyRepository.findAll();
    }

    @GetMapping("/answer")
    public List<Answer> allAnswers() {
        return answerRepository.findAll();
    }

    @PostMapping("/user")
    public User addNewUser(@RequestBody String body){
        return userRepository.save(new User(body));
    }
    @PostMapping("/survey")
    public Survey addNewSurvey(@RequestBody Map<String, Object> body){
        String title = (String) body.get("title");
        String question = (String) body.get("question");
        long user_id = Integer.toUnsignedLong((Integer) body.get("user_id"));

        User user = userRepository.findById(user_id).get();

        Survey newSurvey = new Survey(title, question, user);
        return surveyRepository.save(newSurvey);
    }
    @GetMapping("/survey/user/{id}")
    public List<Survey> findSurveys(@PathVariable("id") long userId){
        User user = userRepository.findById(userId).get();
        return surveyRepository.findByUser(user);
    }
    @DeleteMapping("/survey/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") long surveyId){
        Survey getSurvey = surveyRepository.findById(surveyId).get();
        surveyRepository.delete(getSurvey);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/answer")
    public Answer addNewAnswer(@RequestBody Map<String, Integer> body){
        long survey_id = Integer.toUnsignedLong(body.get("survey_id"));
        long rating = Integer.toUnsignedLong(body.get("rating"));

        Survey findSurvey = surveyRepository.findById(survey_id).get();
        User surveyCreator = findSurvey.getUser();

        Answer newAnswer = new Answer(rating, findSurvey, surveyCreator);
        return answerRepository.save(newAnswer);
    }
    @GetMapping("/answer/{id}")
    public Answer findAnswer(@PathVariable("id") long answerId){
        return answerRepository.findById(answerId).get();
    }
    @PutMapping("/answer/{id}")
    public Answer updateAnswer(@PathVariable("id") long answerId, @RequestBody Map<String, Integer> body){
        long rating = Integer.toUnsignedLong(body.get("rating"));

        Answer getAnswer = answerRepository.findById(answerId).get();
        getAnswer.setRating(rating);
        return answerRepository.save(getAnswer);
    }
}
