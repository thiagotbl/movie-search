package tp.moviesearch;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.MovieRepositoryImpl;
import tp.moviesearch.data.remote.OmdbRestService;

/**
 */
public class Injection {

    private static final String BASE_URL = "http://www.omdbapi.com";

    public static MovieRepository provideMovieRepository() {
        return new MovieRepositoryImpl(createService());
    }

    private static OmdbRestService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(OmdbRestService.class);
    }
}
