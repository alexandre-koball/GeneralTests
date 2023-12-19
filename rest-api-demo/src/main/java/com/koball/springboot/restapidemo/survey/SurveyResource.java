package com.koball.springboot.restapidemo.survey;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class SurveyResource {

	private SurveyService surveyService;
	
	public SurveyResource(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	@RequestMapping("surveys")
	public List<Survey> retrieveAllSurveys() {
		return surveyService.retrieveAllSurveys();
	}
	
	@RequestMapping("surveys/{surveyId}")
	public Survey retrieveSurvey(@PathVariable String surveyId) {
		final Survey survey = surveyService.retrieveSurveyById(surveyId);
		if (survey == null) {
			throw new ResponseStatusException(HttpStatusCode.valueOf(404));
		}
		return survey;
	}
	
	@RequestMapping("surveys/{surveyId}/questions")
	public List<Question> retrieveAllSurveyQuestions(@PathVariable String surveyId) {
		final List<Question> questions = surveyService.retrieveAllSurveyQuestionsBySurveyId(surveyId);
		if (questions == null) {
			throw new ResponseStatusException(HttpStatusCode.valueOf(404));
		}
		return questions;
	}
	
	@RequestMapping("surveys/{surveyId}/questions/{questionId}")
	public Question retrieveSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
		final Question question = surveyService.retrieveQuestionById(surveyId, questionId);
		if (question == null) {
			throw new ResponseStatusException(HttpStatusCode.valueOf(404));
		}
		return question;
	}
	
	@RequestMapping(value="surveys/{surveyId}/questions", method=RequestMethod.POST)
	public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId, @RequestBody Question question) {
		final String newQuestionId = surveyService.addNewSurveyQuestion(surveyId, question);
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionId}").buildAndExpand(newQuestionId).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(value="surveys/{surveyId}/questions/{questionId}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
		surveyService.deleteSurveyQuestion(surveyId, questionId);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="surveys/{surveyId}/questions/{questionId}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody Question question) {
		surveyService.updateSurveyQuestion(surveyId, questionId, question);
		return ResponseEntity.noContent().build();
	}
	
}
