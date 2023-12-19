package com.koball.springboot.restapidemo.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class SurveyResourceTest {

	@MockBean
	private SurveyService surveyService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
	private static String GET_ALL_QUESTIONS_URL = "http://localhost:8080/surveys/Survey1/questions";
	
	@Test
	void retrieveSurveyQuestion_basicScenario() throws Exception {
		Question question = new Question("Question1", "Most Popular Cloud Platform Today",
				Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
		
		when(surveyService.retrieveQuestionById("Survey1", "Question1")).thenReturn(question);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI.create(SPECIFIC_QUESTION_URL)).accept(MediaType.APPLICATION_JSON);
		final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		final String expected = """
				{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
								""";
		
		assertEquals(200, result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		
	}
	
	@Test
	void addNewSurveyQuestion_basicScenario() throws Exception {
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
		when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("Q510");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI.create(GET_ALL_QUESTIONS_URL)).accept(MediaType.APPLICATION_JSON).content(strBody).contentType(MediaType.APPLICATION_JSON);
		final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		assertEquals(201, result.getResponse().getStatus());
		System.out.println(result.getResponse().getHeader("Location"));
		assertTrue(result.getResponse().getHeader("Location").equals("http://localhost:8080/surveys/Survey1/questions/Q510"));
		
	}
}
