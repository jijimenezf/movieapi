package dev.journey.movieapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;

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

}
