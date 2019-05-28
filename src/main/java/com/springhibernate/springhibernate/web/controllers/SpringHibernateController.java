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

import java.util.HashMap;
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
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @GetMapping("/survey")
    public ResponseEntity<List<Survey>> allSurveys() {
        return ResponseEntity.ok().body(surveyRepository.findAll());
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> allAnswers() {
        return ResponseEntity.ok().body(answerRepository.findAll());
    }

    @PostMapping("/user")
    public ResponseEntity<User> addNewUser(@RequestBody Map<String, String> body){
        String username = body.get("name");
        return ResponseEntity.ok().body(userRepository.save(new User(username)));
    }
    @PostMapping("/survey")
    public ResponseEntity<Survey> addNewSurvey(@RequestBody Map<String, Object> body){
        String title = (String) body.get("title");
        String question = (String) body.get("question");
        long user_id = Integer.toUnsignedLong((Integer) body.get("user_id"));

        User user = userRepository.findById(user_id).get();

        Survey newSurvey = new Survey(title, question, user);
        return ResponseEntity.ok().body(surveyRepository.save(newSurvey));
    }
    @GetMapping("/survey/user/{id}")
    public ResponseEntity<List<Survey>> findSurveys(@PathVariable("id") long userId){
        User user = userRepository.findById(userId).get();
        return ResponseEntity.ok().body(surveyRepository.findByUser(user));
    }
    @DeleteMapping("/survey/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") long surveyId){
        Survey getSurvey = surveyRepository.findById(surveyId).get();
        surveyRepository.delete(getSurvey);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/answer")
    public ResponseEntity<Answer> addNewAnswer(@RequestBody Map<String, Integer> body){
        long survey_id = Integer.toUnsignedLong(body.get("survey_id"));
        long rating = Integer.toUnsignedLong(body.get("rating"));

        Survey findSurvey = surveyRepository.findById(survey_id).get();
        User surveyCreator = findSurvey.getUser();

        Answer newAnswer = new Answer(rating, findSurvey, surveyCreator);
        return ResponseEntity.ok().body(answerRepository.save(newAnswer));
    }
    @GetMapping("/answer/{id}")
    public ResponseEntity<Answer> findAnswer(@PathVariable("id") long answerId){
        return ResponseEntity.ok().body(answerRepository.findById(answerId).get());
    }
    @PutMapping("/answer/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable("id") long answerId, @RequestBody Map<String, Integer> body){
        long rating = Integer.toUnsignedLong(body.get("rating"));

        Answer getAnswer = answerRepository.findById(answerId).get();
        getAnswer.setRating(rating);
        return ResponseEntity.ok().body(answerRepository.save(getAnswer));
    }
    @GetMapping("/stats/user/{id}")
    public ResponseEntity<Map> getStats(@PathVariable("id") long userId){
        Map<String, Object> statsMap = new HashMap<>();

        User getUser = userRepository.findById(userId).get();
        
        return ResponseEntity.ok().body(statsMap);
    }
}
