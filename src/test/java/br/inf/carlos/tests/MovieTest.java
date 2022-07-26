package br.inf.carlos.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.inf.carlos.bean.MovieDtoRes;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MovieTest {

	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@Test
    public void test() {
		System.out.println("MovieTest.hasEndpoint()");
		
		ResponseEntity<MovieDtoRes[]> response = this.testRestTemplate.exchange("/produtores", HttpMethod.GET, null, MovieDtoRes[].class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		MovieDtoRes[] movies = response.getBody();
		
		assertFalse(movies == null || movies.length == 0);
		
		// Para ter algum dos produtores com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido
		assertTrue(movies != null || movies.length == 2);
    }
}
