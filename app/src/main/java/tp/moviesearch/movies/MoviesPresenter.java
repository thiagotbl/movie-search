package tp.moviesearch.movies;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tp.moviesearch.data.MovieRepository;
import tp.moviesearch.data.model.MovieSearchItem;

/**
 * Listen to user actions from {@link MoviesFragment}, retrieves the data and updates the UI as required.
 */
class MoviesPresenter implements MoviesContract.UserActionsListener {

    private final MovieRepository mRepository;
    private final MoviesContract.View mView;
    private CompositeSubscription mSubscriptions;

    MoviesPresenter(@NonNull MovieRepository movieRepository, @NonNull MoviesContract.View view) {
        mRepository = movieRepository;
        mView = view;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void searchMovie(@NonNull String title) {
        mView.showLoading();
        mView.clearSearchResults();

        mSubscriptions.clear();
        Subscription subscription = mRepository.searchMovie(title)
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
        mSubscriptions.add(subscription);
    }

    @Override
    public void openMovieDetails(@NonNull MovieSearchItem movie) {
        mView.showMovieDetailsUi(movie.getImdbId());
    }

    @Override
    public void clear() {
        mSubscriptions.clear();
    }
}
