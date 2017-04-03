package tp.moviesearch.details;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.model.MovieDetails;
import tp.moviesearch.util.SchedulersConfig;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DetailsPresenter}.
 */
public class DetailsPresenterTest {

    @Mock
    private MovieRepository mMovieRepository;
    @Mock
    private DetailsContract.View mView;

    private DetailsPresenter mDetailsPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mDetailsPresenter = new DetailsPresenter(mMovieRepository, mView);
        SchedulersConfig.setupSchedulers();
    }

    @After
    public void tearDown() {
        SchedulersConfig.resetSchedulers();
    }

    @Test
    public void testGetMovieDetails() {
        MovieDetails movieDetails = new MovieDetails(
                "Title",
                "1999",
                "7.5",
                "06/06/1999",
                "143 min",
                "action",
                "John Film Maker",
                "John Film Maker, George Lucas",
                "Mel Gibson",
                "Some incredible plot",
                "english, russian, latin",
                "USA, New Zealand",
                "10 oscars",
                "amazing-poster.jpg",
                "65.4",
                84.3f,
                "19073",
                "movie-id",
                "Adventure, Drama, Fantasy"
        );
        when(mMovieRepository.getMovieDetails(anyString()))
                .thenReturn(Observable.just(movieDetails));

        mDetailsPresenter.getMovieDetails("movie title");

        verify(mMovieRepository).getMovieDetails("movie title");

        verify(mView).showLoading();
        verify(mView).hideLoading();
        verify(mView).showMovieDetails(movieDetails);
    }

    @Test
    public void testGetMovieDetailsError() {
        String errorMessage = "Something went wrong";
        when(mMovieRepository.getMovieDetails(anyString()))
                .thenReturn(Observable.error(new RuntimeException(errorMessage)));

        mDetailsPresenter.getMovieDetails("movie title");

        verify(mMovieRepository).getMovieDetails("movie title");

        verify(mView).showLoading();
        verify(mView).hideLoading();
        verify(mView).showError(errorMessage);
    }

    @Test
    public void testOpenFullMoviePoster() {
        String poster = "poster.jpg";

        mDetailsPresenter.openFullMoviePoster(poster);

        verify(mView).showFullScreenMoviePoster(poster);
    }
}
