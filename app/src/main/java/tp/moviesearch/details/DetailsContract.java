package tp.moviesearch.details;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import tp.moviesearch.data.model.MovieDetails;

/**
 */
class DetailsContract {

    interface View {

        void showMovieDetails(@NonNull MovieDetails movie);

        void showLoading();

        void hideLoading();

        void showFullScreenMoviePoster(@NonNull String poster);

        void showError(@Nullable String message);
    }

    interface UserActionsListener {

        void getMovieDetails(@NonNull String imdbId);

        void openFullMoviePoster(@NonNull String poster);
    }
}
