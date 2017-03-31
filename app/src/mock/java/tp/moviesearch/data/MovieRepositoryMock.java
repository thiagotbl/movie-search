package tp.moviesearch.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearchItem;

/**
 */
public class MovieRepositoryMock implements MovieRepository {


    @Override
    public Observable<List<MovieSearchItem>> searchMovie(@NonNull String title) {
        return null;
    }

    @Override
    public Observable<MovieDetails> getMovieDetails(@NonNull String imdbId) {
        return null;
    }
}
