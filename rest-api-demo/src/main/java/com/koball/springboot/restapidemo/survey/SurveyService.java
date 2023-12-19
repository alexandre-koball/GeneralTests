package com.koball.springboot.restapidemo.survey;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SurveyService {

	private static List<Survey> surveys = new ArrayList<>();

	static {
		Question question1 = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		Question question2 = new Question("Question2", "Fastest Growing Cloud Platform",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
		Question question3 = new Question("Question3", "Most Popular DevOps Tool",
				Arrays.asList("Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

		List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));

		Survey survey = new Survey("Survey1", "My Favorite Survey", "Description of the Survey", questions);

		surveys.add(survey);
	}
	
	public List<Survey> retrieveAllSurveys() {
		return surveys;
	}

	public Survey retrieveSurveyById(String surveyId) {
		final Optional<Survey> optSurvey = surveys.stream().filter(survey -> survey.getId().equals(surveyId)).findFirst();
		return optSurvey.isPresent() ? optSurvey.get() : null;
	}

	public List<Question> retrieveAllSurveyQuestionsBySurveyId(String surveyId) {
		final Survey survey = retrieveSurveyById(surveyId);
		return survey != null ? survey.getQuestions() : null;
	}

	public Question retrieveQuestionById(String surveyId, String questionId) {
		final List<Question> questions = retrieveAllSurveyQuestionsBySurveyId(surveyId);
		if (questions == null) {
			return null;
		}
		final Optional<Question> optQuestion = questions.stream().filter(q -> q.getId().equals(questionId)).findFirst();
		return optQuestion.isPresent() ? optQuestion.get() : null;
	}

	public String addNewSurveyQuestion(String surveyId, Question question) {
		final List<Question> questions = retrieveAllSurveyQuestionsBySurveyId(surveyId);
		question.setId(generateRandId());
		questions.add(question);
		return question.getId();
	}

	private String generateRandId() {
		SecureRandom secRand = new SecureRandom();
		String questionId = new BigInteger(32, secRand).toString();
		return questionId;
	}

	public String deleteSurveyQuestion(String surveyId, String questionId) {
		final List<Question> questions = retrieveAllSurveyQuestionsBySurveyId(surveyId);
		if (questions == null) {
			return null;
		}
		return questions.removeIf(q -> q.getId().equals(questionId)) ? questionId : null;
	}

	public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
		final Question questionToUpdate = retrieveQuestionById(surveyId, questionId);
		if (questionToUpdate == null) {
			return;
		}
		questionToUpdate.setCorrectAnswer(question.getCorrectAnswer());
		questionToUpdate.setDescription(question.getDescription());
		questionToUpdate.setOptions(question.getOptions());
		
	}

}
