package tp.moviesearch.data;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import tp.moviesearch.data.model.MovieSearchItem;
import tp.moviesearch.data.remote.OmdbRestService;

/**
 */
public class MovieRepositoryImpl implements MovieRepository {

    private OmdbRestService service;

    public MovieRepositoryImpl(OmdbRestService service) {
        this.service = service;
    }

    @Override
    public Observable<List<MovieSearchItem>> searchMovie(@NonNull String title) {
        String q = title.trim();
        return service.searchMovie(q)
                .concatMap(movieSearch -> movieSearch.getMovies() != null ?
                        Observable.from(movieSearch.getMovies()).toList() :
                        Observable.from(Collections.<MovieSearchItem>emptyList()).toList());
    }
}
