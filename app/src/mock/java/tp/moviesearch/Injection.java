package tp.moviesearch;

import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.MovieRepositoryMock;

/**
 */
public class Injection {

    public static MovieRepository provideMovieRepository() {
        return new MovieRepositoryMock();
    }
}
