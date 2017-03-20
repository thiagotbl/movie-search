package tp.moviesearch.data.remote;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.data.model.MovieSearch;

/**
 */
public interface OmdbRestService {

    @GET("/")
    Observable<MovieSearch> searchMovie(@Query("s") String title);

    @GET("/?tomatoes=true")
    Observable<MovieDetails> getMovieDetails(@Query("i") String imdbId);
}
