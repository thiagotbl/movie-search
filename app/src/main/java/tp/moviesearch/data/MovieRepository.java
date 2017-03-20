package tp.moviesearch.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearchItem;

/**
 */
public interface MovieRepository {

    Observable<List<MovieSearchItem>> searchMovie(@NonNull String title);

    Observable<MovieDetails> getMovieDetails(@NonNull String imdbId);
}
