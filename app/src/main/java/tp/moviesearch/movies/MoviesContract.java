package tp.moviesearch.movies;

import android.support.annotation.NonNull;

import java.util.List;

import tp.moviesearch.data.model.MovieSearchItem;

/**
 * Specifies the contract between the {@link MoviesPresenter} and the {@link MoviesFragment}
 */
class MoviesContract {

    interface View {

        void showSearchResults(@NonNull List<MovieSearchItem> movies);

        void showEmptyResult();

        void clearSearchResults();

        void showLoading();

        void hideLoading();

        void showError();

        void showMovieDetailsUi(@NonNull String imdbId);
    }

    interface UserActionsListener {

        void searchMovie(@NonNull String title);

        void openMovieDetails(@NonNull MovieSearchItem movie);

        void clear();
    }
}
