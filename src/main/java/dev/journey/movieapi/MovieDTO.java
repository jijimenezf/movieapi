package dev.journey.movieapi;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Integer movieId;
    @NotBlank(message = "A title is required for this movie")
    private String title;
    @NotBlank(message = "A director is required for this movie")
    private String director;
    @NotBlank(message = "A studio is required for this movie")
    private String studio;
    private Set<String> movieCast;
    private Integer releaseYear;
    @NotBlank(message = "Provide the poster for this movie")
    private String poster;
    @NotBlank(message = "Please provide poster's url!")
    private String posterUrl;
}
