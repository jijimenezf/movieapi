package dev.journey.movieapi.services;

import dev.journey.movieapi.Movie;
import dev.journey.movieapi.MovieDTO;
import dev.journey.movieapi.MovieRepository;
import dev.journey.movieapi.MovieResponse;
import dev.journey.movieapi.exceptions.FileExistsException;
import dev.journey.movieapi.exceptions.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        /*Path filePath = Paths.get(path + File.separator + multipartFile.getOriginalFilename());
        if (Files.exists(filePath)) {
            throw new FileExistsException("File already exists, please enter another file name :-(");
        }

        String uploadedFile = fileService.uploadFile(path, multipartFile);

        movieDTO.setPoster(uploadedFile);*/

        Movie movie = new Movie(null, movieDTO.getTitle(),
                movieDTO.getDirector(), movieDTO.getStudio(),
                movieDTO.getMovieCast(), movieDTO.getReleaseYear(),
                movieDTO.getPoster());

        Movie movieObj = movieRepository.save(movie);

        String posterUrl = baseUrl + "/file/"; // + uploadedFile;

        return new MovieDTO(movieObj.getMovieId(), movieObj.getTitle(),
                movieObj.getDirector(), movieObj.getStudio(),
                movieObj.getMovieCast(), movieObj.getReleaseYear(),
                movieObj.getPoster(), posterUrl);
    }

    @Override
    public MovieDTO getMovie(Integer movieId) {
        Movie movieObj = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("Movie not found with id = " + movieId));

        String posterUrl = baseUrl + "/file/" + movieObj.getPoster();
        return new MovieDTO(movieObj.getMovieId(), movieObj.getTitle(),
                movieObj.getDirector(), movieObj.getStudio(),
                movieObj.getMovieCast(), movieObj.getReleaseYear(),
                movieObj.getPoster(), posterUrl);
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        List<MovieDTO> movieDTOList = new ArrayList<>();

        for (Movie item : movies) {
            String posterUrl = baseUrl + "/file/" + item.getPoster();
            MovieDTO response = new MovieDTO(item.getMovieId(), item.getTitle(),
                    item.getDirector(), item.getStudio(),
                    item.getMovieCast(), item.getReleaseYear(),
                    item.getPoster(), posterUrl);
            movieDTOList.add(response);
        }
        return movieDTOList;
    }

    @Override
    public MovieDTO updateMovie(Integer movieId, MovieDTO movieDTO, MultipartFile multipartFile) throws IOException {
        Movie movieObj = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("Movie not found with id = " + movieId));

        String fileName = movieObj.getPoster();

        if (multipartFile != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, multipartFile);
        }

        movieDTO.setPoster(fileName);

        Movie movie = new Movie(movieObj.getMovieId(), movieDTO.getTitle(),
                movieDTO.getDirector(), movieDTO.getStudio(),
                movieDTO.getMovieCast(), movieDTO.getReleaseYear(), movieDTO.getPoster());

        Movie updated = movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + fileName;
        return new MovieDTO(updated.getMovieId(), updated.getTitle(),
                updated.getDirector(), updated.getStudio(),
                updated.getMovieCast(), updated.getReleaseYear(),
                updated.getPoster(), posterUrl);
    }

    @Override
    public void deleteMovie(Integer movieId) throws IOException {
        Movie movieObj = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("Movie not found with id = " + movieId));
        Integer id = movieObj.getMovieId();

        Files.deleteIfExists(Paths.get(path + File.separator + movieObj.getPoster()));

        movieRepository.delete(movieObj);
    }

    @Override
    public MovieResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<MovieDTO> results = getMovieResults(moviePages);
        return new MovieResponse(results,
                pageNumber, pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    @Override
    public MovieResponse getMoviesWithPaginationAndSorted(Integer pageNumber, Integer pageSize,
                                                          String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<MovieDTO> results = getMovieResults(moviePages);
        return new MovieResponse(results,
                pageNumber, pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    private List<MovieDTO> getMovieResults(Page<Movie> moviePages) {
        List<Movie> movieList = moviePages.getContent();

        List<MovieDTO> results = new ArrayList<>();

        for (Movie item : movieList) {
            String posterUrl = baseUrl + "/file/" + item.getPoster();
            MovieDTO movieDto = new MovieDTO(
                    item.getMovieId(),
                    item.getTitle(),
                    item.getDirector(),
                    item.getStudio(),
                    item.getMovieCast(),
                    item.getReleaseYear(),
                    item.getPoster(),
                    posterUrl
            );
            results.add(movieDto);
        }
        return results;
    }
}
