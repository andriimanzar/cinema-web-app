package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.Movie;
import com.manzar.persistence.exception.DBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieDaoTest {

    private static MovieDao movieDao;
    private static Random random;

    @BeforeAll
    static void globalSetUp() {

        movieDao = MovieDaoImpl.getInstance();
        random = new Random();
    }


    @Test
    @Order(1)
    @DisplayName("Save a movie")
    void saveMovie() {
        Movie testMovie = generateMovie();

        int moviesCountBeforeInsert = movieDao.findAll().size();
        movieDao.save(testMovie);
        List<Movie> movies = movieDao.findAll();

        assertNotNull(testMovie.getId());
        assertEquals(moviesCountBeforeInsert + 1, movies.size());
        assertTrue(movies.contains(testMovie));
    }

    @Test
    @Order(2)
    @DisplayName("Find all movies")
    void findAllMovies() {
        List<Movie> previousMovies = movieDao.findAll();
        List<Movie> newMovies = generateMovies();
        newMovies.forEach(movieDao::save);
        int previousMoviesAndNewMoviesCount = previousMovies.size() + newMovies.size();
        List<Movie> allMovies = movieDao.findAll();

        assertTrue(allMovies.containsAll(previousMovies));
        assertTrue(allMovies.containsAll(newMovies));
        assertEquals(previousMoviesAndNewMoviesCount, allMovies.size());

    }

    @Test
    @Order(3)
    @DisplayName("Find movie by id")
    void findMovieById() {
        Movie testMovie = generateMovie();

        movieDao.save(testMovie);

        Movie movieFromDb = movieDao.findMovie(testMovie.getId());

        assertEquals(testMovie, movieFromDb);
        assertEquals(testMovie.getTitle(), movieFromDb.getTitle());
        assertEquals(testMovie.getGenre(), movieFromDb.getGenre());
        assertEquals(testMovie.getDuration(), movieFromDb.getDuration());
        assertEquals(testMovie.getDirector(), movieFromDb.getDirector());
        assertEquals(testMovie.getReleaseYear(), movieFromDb.getReleaseYear());
    }

    @Test
    @Order(4)
    @DisplayName("findMovie() throws an exception, when movie ID is incorrect")
    void findByIncorrectId() {

        long id = -1L;

        assertThrows(DBException.class, () -> movieDao.findMovie(id));
    }

    @Test
    @Order(5)
    @DisplayName("Update a movie")
    void updateMovie() {
        Movie testMovie = generateMovie();

        movieDao.save(testMovie);
        List<Movie> moviesBeforeUpdate = movieDao.findAll();

        Movie generatedMovie = generateMovie();

        changeTargetMovieFields(testMovie, generatedMovie);

        movieDao.update(testMovie);
        List<Movie> allMovies = movieDao.findAll();
        Movie updatedMovie = movieDao.findMovie(testMovie.getId());

        assertEquals(moviesBeforeUpdate.size(), allMovies.size());
        assertEquals(testMovie, updatedMovie);

    }

    @Test
    @Order(6)
    @DisplayName("update() throws an exception, when a movie id is null")
    void updateNotStoredMovie() {
        Movie notStoredMovie = generateMovie();

        assertThrows(DBException.class, () -> movieDao.update(notStoredMovie));
    }

    @Test
    @Order(7)
    @DisplayName("update() throws an exception, when a movie id is invalid")
    void updateMovieWithInvalidId() {
        Movie invalidIdMovie = generateMovie();

        invalidIdMovie.setId(-1L);

        assertThrows(DBException.class, () -> movieDao.update(invalidIdMovie));

    }

    @Test
    @Order(8)
    @DisplayName("Remove a movie")
    void removeMovie() {
        Movie testMovie = generateMovie();

        movieDao.save(testMovie);

        List<Movie> moviesBeforeRemove = movieDao.findAll();
        movieDao.remove(testMovie.getId());

        List<Movie> moviesAfterRemove = movieDao.findAll();

        assertEquals(moviesBeforeRemove.size() - 1, moviesAfterRemove.size());
        assertFalse(moviesAfterRemove.contains(testMovie));
    }

    @Test
    @Order(9)
    @DisplayName("remove() throws an exception, when a movie id is null")
    void removeNotStoredMovie() {
        Movie notStoredMovie = generateMovie();

        assertThrows(DBException.class, () -> movieDao.remove(notStoredMovie.getId()));
    }

    @Test
    @Order(10)
    @DisplayName("remove() throws an exception, when a movie id is invalid")
    void removeMovieWithInvalidId() {
        Movie invalidIdMovie = generateMovie();

        invalidIdMovie.setId(-1L);

        assertThrows(DBException.class, () -> movieDao.remove(invalidIdMovie.getId()));

    }

    private static Movie generateMovie() {
        Movie generatedMovie = new Movie();

        generatedMovie.setTitle(RandomStringUtils.randomAlphabetic(10));
        generatedMovie.setGenre(RandomStringUtils.randomAlphabetic(10));
        generatedMovie.setDuration(random.nextLong(1000,2000));
        generatedMovie.setDirector(RandomStringUtils.randomNumeric(10));
        generatedMovie.setReleaseYear(random.nextInt(1900, LocalDate.now().getYear()));

        return generatedMovie;
    }


    private List<Movie> generateMovies() {
        return Stream.generate(() -> new Random().nextInt(10)).limit(5).
                map(a -> generateMovie()).collect(Collectors.toList());
    }

    private void changeTargetMovieFields(Movie target, Movie source){
        target.setTitle(source.getTitle());
        target.setGenre(source.getGenre());
        target.setDuration(source.getDuration());
        target.setDirector(source.getDirector());
        target.setReleaseYear(source.getReleaseYear());
    }
}


