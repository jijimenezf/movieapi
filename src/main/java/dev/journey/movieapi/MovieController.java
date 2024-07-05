package dev.journey.movieapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.journey.movieapi.exceptions.EmptyFileException;
import dev.journey.movieapi.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addMovie")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDto) {
        MovieDTO result = movieService.addMovie(movieDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Integer movieId,
            @RequestBody MovieDTO movieDto
    ) {
        try {
            MovieDTO result = movieService.updateMovie(movieId, movieDto, null);
            // return ResponseEntity.ok(result);
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
