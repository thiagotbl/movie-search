package tp.moviesearch.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearchItem;

/**
 * Fake implementation of {@link MovieRepository} for tests.
 */
public class MovieRepositoryMock implements MovieRepository {

    private static List<MovieSearchItem> sSearchResult;
    private static MovieDetails sMovieDetails;

    @Override
    public Observable<List<MovieSearchItem>> searchMovie(@NonNull String title) {
        return Observable.just(sSearchResult);
    }

    @Override
    public Observable<MovieDetails> getMovieDetails(@NonNull String imdbId) {
        return Observable.just(sMovieDetails);
    }

    @VisibleForTesting
    public static void setSearchResult(@NonNull MovieSearchItem... movies) {
        sSearchResult = Arrays.asList(movies);
    }

    @VisibleForTesting
    public static void setMovieDetails(@NonNull MovieDetails movieDetails) {
        sMovieDetails = movieDetails;
    }
}
