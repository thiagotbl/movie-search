package tp.moviesearch.data;

import android.support.annotation.NonNull;

import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearch;
import tp.moviesearch.data.remote.OmdbRestService;

/**
 */
public class MovieRepositoryImpl implements MovieRepository {

    private OmdbRestService service;

    public MovieRepositoryImpl(OmdbRestService service) {
        this.service = service;
    }

    @Override
    public Observable<MovieDetails> getMovieDetails(@NonNull String imdbId) {
        return service.getMovieDetails(imdbId);
    }

    @Override
    public Observable<MovieSearch> search(@NonNull String title) {
        return search(title, 1);
    }

    @Override
    public Observable<MovieSearch> search(@NonNull String title, int page) {
        return service.searchMovie(title, page);
    }
}
