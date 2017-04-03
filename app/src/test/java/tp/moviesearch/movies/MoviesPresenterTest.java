package tp.moviesearch.movies;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.model.MovieSearchItem;
import tp.moviesearch.util.SchedulersConfig;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MoviesPresenter}.
 */
public class MoviesPresenterTest {

    @Mock
    private MovieRepository mMovieRepository;
    @Mock
    private MoviesContract.View mView;

    private MoviesPresenter mMoviesPresenter;

    @Before
    public void setupMoviesPresenter() {
        MockitoAnnotations.initMocks(this);
        mMoviesPresenter = new MoviesPresenter(mMovieRepository, mView);
        SchedulersConfig.setupSchedulers();
    }

    @After
    public void tearDown() {
        SchedulersConfig.resetSchedulers();
    }

    @Test
    public void testEmptySearchResult() {
        when(mMovieRepository.searchMovie(anyString()))
                .thenReturn(Observable.just(Collections.emptyList()));

        mMoviesPresenter.searchMovie("title");

        verify(mMovieRepository).searchMovie("title");

        verify(mView).showLoading();
        verify(mView).clearSearchResults();
        verify(mView).hideLoading();
        verify(mView).showEmptyResult();
    }

    @Test
    public void testSearchResult() {
        List<MovieSearchItem> searchResult = new ArrayList<>();
        searchResult.add(new MovieSearchItem("Movie 1", "1999", "id01", "movie", "poster-01.jpg"));
        searchResult.add(new MovieSearchItem("Movie 2", "2000", "id02", "movie", "poster-02.jpg"));
        when(mMovieRepository.searchMovie(anyString()))
                .thenReturn(Observable.just(searchResult));

        mMoviesPresenter.searchMovie("title");

        verify(mMovieRepository).searchMovie("title");

        verify(mView).showLoading();
        verify(mView).clearSearchResults();
        verify(mView).hideLoading();
        verify(mView).showSearchResults(searchResult);
    }

    @Test
    public void testSearchError() {
        when(mMovieRepository.searchMovie(anyString()))
                .thenReturn(Observable.error(null));

        mMoviesPresenter.searchMovie("title");

        verify(mMovieRepository).searchMovie("title");

        verify(mView).showLoading();
        verify(mView).clearSearchResults();
        verify(mView).hideLoading();
        verify(mView).showError();
    }

    @Test
    public void testOpenMovieDetails() {
        mMoviesPresenter.openMovieDetails(new MovieSearchItem("", "", "id", "", ""));

        verify(mView).showMovieDetailsUi("id");
    }
}
