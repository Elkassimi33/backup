package com.movie.movieApi.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movieApi.dto.MovieDto;
import com.movie.movieApi.dto.MoviePageResponse;
import com.movie.movieApi.exceptions.EmptyFileException;
import com.movie.movieApi.service.MovieService;
import com.movie.movieApi.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
@RequestMapping("/api/vi/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file
            ,@RequestPart String movieDto) throws IOException, EmptyFileException {

        if(file.isEmpty()){
            throw new EmptyFileException("file is empty! please send another file");
        }
        MovieDto dto = convertMovieDto(movieDto);
        return new  ResponseEntity<>(movieService.addMovie(dto,file),HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId){

        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovieHandler(){

        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId
                                                       ,@RequestPart MultipartFile file
                                                       ,@RequestPart String MovieDtoString) throws IOException {
        file = file.isEmpty() ? null: file;
        MovieDto dto = convertMovieDto(MovieDtoString);
        return new  ResponseEntity<>(movieService.updateMovie(movieId,dto,file),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deletMoviehandler(@PathVariable Integer movieId) throws IOException {

        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    private MovieDto convertMovieDto (String MovieDtoString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(MovieDtoString, MovieDto.class);
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMovieWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/allMoviesPageSorting")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationSort(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortDir
    ){
        return ResponseEntity.ok(movieService.getAllMovieWithPaginationAndSorting(pageNumber,pageSize,sortBy,sortDir));
    }
}
