package com.koball.springboot.restapidemo.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Base64;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

	private static String QUESTION_URL = "/surveys/Survey1/questions/Question1";
	private static String ALL_QUESTIONS_URL = "/surveys/Survey1/questions";
	
	@Autowired
	private TestRestTemplate template;
	
	@Test
	void retrieveSurveyQuestion_basicScenario() throws JSONException {
		
		final HttpHeaders headers = getHeaders();
		final HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> responseEntity = template.exchange(QUESTION_URL, HttpMethod.GET, entity, String.class);
		String expectedResponse = 
				"""
				{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
				""";
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals(responseEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), true);
	}
	
	@Test
	void retrieveAllSurveyQuestions_basicScenario() throws JSONException {
		
		final HttpHeaders headers = getHeaders();
		final HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> responseEntity = template.exchange(ALL_QUESTIONS_URL, HttpMethod.GET, entity, String.class);
		String expectedResponse =
				"""
				[
				    {
				        "id": "Question1"
				    },
				    {
				        "id": "Question2"
				    },
				    {
				        "id": "Question3"
				    }
				]
				""";
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals(responseEntity.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void addNewSurveyQuestion_basicScenario() {

		final String strBody = """
				{
				    "description": "Fastest Growing Cloud Platform",
				    "options": [
				        "AWS",
				        "Azure",
				        "Google Cloud",
				        "Oracle Cloud"
				    ],
				    "correctAnswer": "Google Cloud"
				}
				""";
		final HttpHeaders headers = getHeaders();
		final HttpEntity<String> entity = new HttpEntity<String>(strBody, headers);
		ResponseEntity<String> responseEntity = template.exchange(ALL_QUESTIONS_URL, HttpMethod.POST, entity, String.class);
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		final String responseLocation = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(responseLocation.contains("/surveys/Survey1/questions/"));

		template.exchange(responseLocation, HttpMethod.DELETE, entity, String.class);
		
	}

	private HttpHeaders getHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic " + doAuthBasicEncoding("admin", "admin"));
		return headers;
	}
	
	private String doAuthBasicEncoding(String username, String password) {
		String combo = username + ":" + password;
		byte[] encoded = Base64.getEncoder().encode(combo.getBytes());
		return new String(encoded);
	}
	
}
