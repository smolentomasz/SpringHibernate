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
        User user;
        if(userRepository.findById(user_id).isPresent()){
            user = userRepository.findById(user_id).get();

            Survey newSurvey = new Survey(title, question, user);
            return ResponseEntity.ok().body(surveyRepository.save(newSurvey));
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/survey/user/{id}")
    public ResponseEntity<List<Survey>> findSurveys(@PathVariable("id") long userId){
        User user;
        if(userRepository.findById(userId).isPresent()){
            user = userRepository.findById(userId).get();
            return ResponseEntity.ok().body(surveyRepository.findByUser(user));
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/survey/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") long surveyId){
        Survey getSurvey;
        if(surveyRepository.findById(surveyId).isPresent()){
            getSurvey = surveyRepository.findById(surveyId).get();
            surveyRepository.delete(getSurvey);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/answer")
    public ResponseEntity<Answer> addNewAnswer(@RequestBody Map<String, Integer> body){
        long survey_id = Integer.toUnsignedLong(body.get("survey_id"));
        long rating = Integer.toUnsignedLong(body.get("rating"));
        Survey findSurvey;
        if(surveyRepository.findById(survey_id).isPresent()){
            findSurvey = surveyRepository.findById(survey_id).get();
            User surveyCreator = findSurvey.getUser();

            Answer newAnswer = new Answer(rating, findSurvey, surveyCreator);
            return ResponseEntity.ok().body(answerRepository.save(newAnswer));
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/answer/{id}")
    public ResponseEntity<Answer> findAnswer(@PathVariable("id") long answerId){
        if(answerRepository.findById(answerId).isPresent()){
            return ResponseEntity.ok().body(answerRepository.findById(answerId).get());
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/answer/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable("id") long answerId, @RequestBody Map<String, Integer> body){
        long rating = Integer.toUnsignedLong(body.get("rating"));
        Answer getAnswer;
        if(answerRepository.findById(answerId).isPresent()){
            getAnswer = answerRepository.findById(answerId).get();
            getAnswer.setRating(rating);
            return ResponseEntity.ok().body(answerRepository.save(getAnswer));
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/stats/user/{id}")
    public ResponseEntity<Map> getStatsForUser(@PathVariable("id") long userId){
        Map<String, Object> statsMap = new HashMap<>();
        User getUser;
        if(userRepository.findById(userId).isPresent()){
            getUser = userRepository.findById(userId).get();

            List<Survey> surveysByUser = surveyRepository.findByUser(getUser);
            int countSurvey = surveysByUser.size();
            statsMap.put("survey_quanity", countSurvey);

            List<Answer> answersByUser = answerRepository.findByUser(getUser);
            double sum = 0;
            double quantity = 0;
            Map<String, Object> answerAverage = new HashMap<>();
            for(Survey survey: surveysByUser){
                for(Answer answer: answersByUser){
                    if(survey == answer.getSurvey()){
                        sum+=answer.getRating();
                        quantity++;
                    }
                }
                answerAverage.put("survey_id", survey.getSurvey_id());
                answerAverage.put("answer_average", sum/quantity);
                answerAverage.put("answer_quanity", quantity);
            }
            statsMap.put("answer_summary", answerAverage);

            return ResponseEntity.ok().body(statsMap);
        }
        else{
            System.out.println("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/stats")
    public ResponseEntity<Map> getStats(){
        Map<String, Object> statsMap = new HashMap<>();

        List<Survey> allSurveys = surveyRepository.findAll();
        List<Answer> allAnswers = answerRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        double usersSize = allUsers.size();
        double surveySize = allSurveys.size();
        double answerSize = allAnswers.size();

        double surveys_per_user = surveySize/usersSize;
        double answers_per_survey = answerSize/surveySize;

        statsMap.put("surveys_quantity", allSurveys.size());
        statsMap.put("answers_quantity", allAnswers.size());
        statsMap.put("surveys_per_user", surveys_per_user);
        statsMap.put("answers_per_survey", answers_per_survey);
        return ResponseEntity.ok().body(statsMap);
    }

}
