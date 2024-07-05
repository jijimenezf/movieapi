package dev.journey.movieapi;

import java.util.List;

public record MovieResponse(List<MovieDTO> movieDTOList, Integer pageNumber, Integer pageSize,
                            long totalElements, int totalPages, boolean isLast) {}
