package com.movie.movieApi.service;

import com.movie.movieApi.dto.MovieDto;
import com.movie.movieApi.dto.MoviePageResponse;
import com.movie.movieApi.entities.Movie;
import com.movie.movieApi.exceptions.FileExistsException;
import com.movie.movieApi.exceptions.MovieNotFoundException;
import com.movie.movieApi.repository.MovieRepository;
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
import java.nio.file.Paths;
import java.util.List;


@Service
public class MovieServiceImpl implements MovieService{

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
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException{
//        1. upload the file
        if(Files.exists(Paths.get(path+ File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("file already exist! Please enter another file name!");
        }
        String uploadedFileName = fileService.uploadFile(path,file);

//        2. set the value of field 'poster' as file name
        movieDto.setPoster(uploadedFileName);

//        3. map dto to movie object
        Movie movie =new Movie(
                null,//movieDto.getMovieId()
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

//        4. save movie object->saved movie object
        Movie savedMovie = movieRepository.save(movie);

//        5. generate the poster url
        String posterUrl = baseUrl+"/file/"+uploadedFileName;

//        6. map the movie object to dto object and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
//         1. check the data in DB and if exists , fetch the data of given ID
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("movie not found with id="+movieId));

//        2. generate posterUrl
        String posterUrl = baseUrl+"/file/"+movie.getPoster();

//        3. map to movieDto object and return it

        return new MovieDto(
               movie.getMovieId(),
               movie.getTitle(),
               movie.getDirector(),
               movie.getStudio(),
               movie.getMovieCast(),
               movie.getReleaseYear(),
               movie.getPoster(),
               posterUrl
       );
    }

    @Override
    public List<MovieDto> getAllMovies() {
//        1. fetch all data from DB
        List<Movie> movieList= movieRepository.findAll();


//        2. iterate through the movieList, generate posterUrl for each movie object,
//           map it to movieDto object and return the list of movieDto object
        List<MovieDto> movieDtoList  = movieList.stream().map((movie)->{
            //        generate posterUrl
            String posterUrl = baseUrl+"/file/"+movie.getPoster();

            return new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
        }).toList();
        return movieDtoList;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
//        1. check if the movie exist with the given id
//        boolean isMovieExist =movieRepository.existsById(movieId);

        Movie movieDB = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("movie not found with id="+movieId));

//        2. if fil is null,do nothing
//           if file is not null,the deleted existing file associated with the record
//           and upload the new file
        String fileName=movieDB.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path+ File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }


//         3. set the movieDto's poster, accorded to step2
            movieDto.setPoster(fileName);


//        4. map it to Movie object
        Movie movie =new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

//        5.save Movie object-> return saved Movie object
        Movie savedMovie = movieRepository.save(movie);

//        6. generate posterUrl for it
        String posterUrl = baseUrl+"/file/"+movie.getPoster();

//        7. map to MovieDto and return it

        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
//        1. check if the movie exist with the given id
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("movie not found with id="+movieId));

//        2. delete the file associated with this object

        Files.deleteIfExists(Paths.get(path+File.separator+movie.getPoster()));


//        3. delete the movie object
        movieRepository.deleteById(movieId);
        return "the movie  deleted with id= "+movieId;
    }

    @Override
    public MoviePageResponse getAllMovieWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies=moviePages.getContent();

        List<MovieDto> movieDtoList  = movies.stream().map((movie)->{
            //        generate posterUrl
            String posterUrl = baseUrl+"/file/"+movie.getPoster();

            return new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
        }).toList();
        return new MoviePageResponse(movieDtoList,pageNumber,pageSize,  moviePages.getTotalElements(),moviePages.getTotalPages(),moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMovieWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies=moviePages.getContent();

        List<MovieDto> movieDtoList  = movies.stream().map((movie)->{
            //        generate posterUrl
            String posterUrl = baseUrl+"/file/"+movie.getPoster();

            return new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
        }).toList();
        return new MoviePageResponse(movieDtoList,pageNumber,pageSize, (int) moviePages.getTotalElements(),moviePages.getTotalPages(),moviePages.isLast());
    }
}
