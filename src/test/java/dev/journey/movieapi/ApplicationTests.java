package dev.journey.movieapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	private static final String BASE_URL = "/api/v1/movies";

	@Test
	void shouldReturnAnExistingRecord() throws Exception {
		ResponseEntity<MovieDTO> response = restTemplate
				.getForEntity(BASE_URL + "/99", MovieDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		MovieDTO singleMovie = response.getBody();
		assertThat(singleMovie.getTitle()).isEqualTo("The Godfather");
		assertThat(singleMovie.getDirector()).isEqualTo("Francis Ford Coppola");
		assertThat(singleMovie.getReleaseYear()).isEqualTo(1972);
	}

	@Test
	void shouldReturnTheFullListOfMovies() throws Exception {
		// Instead of using List
		ResponseEntity<MovieDTO[]> response = restTemplate
				.getForEntity(BASE_URL + "/all", MovieDTO[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		MovieDTO[] results = response.getBody();
		assertThat(results.length).isEqualTo(22);
	}

	@Test
	@DirtiesContext
	void shouldDeleteAMovie() throws Exception {
		ResponseEntity<Void> response = restTemplate
			.exchange(BASE_URL + "/delete/78", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate
				.getForEntity(BASE_URL + "/78", String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldReturnTheListOfMoviesWithPagination() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(BASE_URL + "/allMoviesPage")
				.queryParam("pageNumber", 1)
				.queryParam("pageSize", 10);

		ResponseEntity<MovieResponse> response = restTemplate
				.getForEntity(uriBuilder.toUriString(), MovieResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		MovieResponse movies = response.getBody();
		assertNotNull(movies);
		List<MovieDTO> movieList = movies.movieDTOList();
		assertThat(movieList).isNotEmpty();

		int totalPages = movies.totalPages();
		assertThat(totalPages).isGreaterThan(1);

		assertThat(movies.isLast()).isFalse();
	}

	@Test
	void shouldReturnTheListOfMoviesWithPaginationAndSorting() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(BASE_URL + "/allMoviesPageSort")
				.queryParam("pageNumber", 0)
				.queryParam("pageSize", 10)
				.queryParam("sortBy", "studio")
				.queryParam("dir", "asc");

		ResponseEntity<MovieResponse> response = restTemplate
				.getForEntity(uriBuilder.toUriString(), MovieResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		MovieResponse movies = response.getBody();
		assertNotNull(movies);
		List<MovieDTO> movieList = movies.movieDTOList();
		assertThat(movieList).isNotEmpty();

		for (int start = 0; start < 4; start++) {
			MovieDTO item = movieList.get(start);
			assertThat(item.getStudio()).isEqualTo("Columbia Pictures");
		}
	}

	@Test
	@DirtiesContext
	void shouldAddANewMovie() {
		MovieDTO movieDTORequest = new MovieDTO();
		movieDTORequest.setDirector("David Lynch");
		movieDTORequest.setTitle("Blue Velvet");
		movieDTORequest.setStudio("Sony Pictures");
		movieDTORequest.setPoster("http://localhost:8080/file/");
		movieDTORequest.setPosterUrl("http://localhost:8080/file/");
		movieDTORequest.setReleaseYear(1989);
		ResponseEntity<MovieDTO> response = restTemplate.postForEntity(BASE_URL + "/addMovie", movieDTORequest, MovieDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		MovieDTO result = response.getBody();
		assertThat(result).isNotNull();
	}

	@Test
	@DirtiesContext
	void shouldUpdateANewMovie() {
		HttpEntity<MovieDTO> entity = getMovieDTOHttpEntity();

		ResponseEntity<MovieDTO> response = restTemplate.exchange(
				BASE_URL + "/update/91",
				HttpMethod.PUT, entity, MovieDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		//MovieDTO result = response.getBody();
		//assertThat(result).isNotNull();
	}

	private static HttpEntity<MovieDTO> getMovieDTOHttpEntity() {
		Set<String> movieCast = new HashSet<>();
		movieCast.add("Liam Neeson");
		movieCast.add("Ben Kingsley");
		movieCast.add("Ralph Fiennes");
		movieCast.add("Caroline Goodall");
		movieCast.add("Jonathan Sagall");
		movieCast.add("Embeth Davidtz");
		movieCast.add("Malgorzata Gebel");
		movieCast.add("Shmuel Levy");
		movieCast.add("Mark Ivanir");

		MovieDTO movieDTORequest = new MovieDTO();
		movieDTORequest.setDirector("Steven Spielberg");
		movieDTORequest.setTitle("The Schindler List");
		movieDTORequest.setStudio("Paramount");
		movieDTORequest.setReleaseYear(1993);
		movieDTORequest.setPoster("http://localhost:8080/file/");
		movieDTORequest.setPosterUrl("http://localhost:8080/file/");
		movieDTORequest.setMovieCast(movieCast);

		HttpHeaders headers = new HttpHeaders();
		return new HttpEntity<>(movieDTORequest, headers);
	}
}
