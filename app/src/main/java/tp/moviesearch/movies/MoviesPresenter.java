package tp.moviesearch.movies;

import android.support.annotation.NonNull;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.model.MovieSearchItem;

/**
 * Listen to user actions from {@link MoviesFragment}, retrieves the data and updates the UI as required.
 */
class MoviesPresenter implements MoviesContract.UserActionsListener {

    private final MovieRepository mRepository;
    private final MoviesContract.View mView;

    MoviesPresenter(@NonNull MovieRepository movieRepository, @NonNull MoviesContract.View view) {
        mRepository = movieRepository;
        mView = view;
    }

    @Override
    public void searchMovie(@NonNull String title) {
        // TODO cancelar inscrição
        mView.showLoading();
        mView.clearSearchResults();

        mRepository.searchMovie(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> {
                            mView.hideLoading();

                            if (movies.isEmpty()) {
                                mView.showEmptyResult();
                            } else {
                                mView.showSearchResults(movies);
                            }
                        },
                        error -> {
                            mView.hideLoading();
                            mView.showError();
                        }
                );
    }

    @Override
    public void openMovieDetails(@NonNull MovieSearchItem movie) {

    }
}
