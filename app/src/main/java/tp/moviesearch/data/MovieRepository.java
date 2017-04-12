package tp.moviesearch.data;

import android.support.annotation.NonNull;

import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearch;

/**
 */
public interface MovieRepository {

    /**
     * Search for a movie by its title. Will try to get the first result page.
     *
     * @param title the title to search for.
     * @return a search result as an instance of {@link MovieSearch}.
     */
    Observable<MovieSearch> search(@NonNull String title);

    /**
     * Get the specified page result of a search for the received title.
     *
     * @param title the title to search for.
     * @return a search result as an instance of {@link MovieSearch}.
     */
    Observable<MovieSearch> search(@NonNull String title, int page);

    /**
     * @param imdbId the unique indentifier for a movie.
     * @return the movie details.
     */
    Observable<MovieDetails> getMovieDetails(@NonNull String imdbId);
}
