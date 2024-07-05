package dev.journey.movieapi;

import dev.journey.movieapi.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer movieId) throws IOException {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MovieResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MovieResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = Constants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = Constants.SORT_DIR, required = false) String dir
    ) {
        return ResponseEntity.ok(movieService.getMoviesWithPaginationAndSorted(pageNumber, pageSize, sortBy, dir));
    }
    /**
     * addMovieHandler
     * updateMovieHandler
     */
}
