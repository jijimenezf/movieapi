package dev.journey.movieapi.services;

import dev.journey.movieapi.MovieDTO;
import dev.journey.movieapi.MovieResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDTO addMovie(MovieDTO movieDTO);

    MovieDTO getMovie(Integer movieId);

    List<MovieDTO> getAllMovies();

    MovieDTO updateMovie(Integer movieId, MovieDTO movieDTO, MultipartFile multipartFile) throws IOException;

    void deleteMovie(Integer movieId) throws IOException;

    MovieResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    MovieResponse getMoviesWithPaginationAndSorted(Integer pageNumber, Integer pageSize, String sortBy, String dir);
}
