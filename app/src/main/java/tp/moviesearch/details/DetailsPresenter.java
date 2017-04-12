package tp.moviesearch.details;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tp.moviesearch.data.MovieRepository;

/**
 */
class DetailsPresenter implements DetailsContract.UserActionsListener {

    private MovieRepository mRepository;
    private DetailsContract.View mView;

    private CompositeSubscription mSubscriptions;

    DetailsPresenter(MovieRepository repository, DetailsContract.View view) {
        mRepository = repository;
        mView = view;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void getMovieDetails(@NonNull String imdbId) {
        mView.showLoading();

        mSubscriptions.clear();
        Subscription subscription = mRepository.getMovieDetails(imdbId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movieDetails -> {
                            mView.hideLoading();
                            mView.showMovieDetails(movieDetails);
                        },
                        error -> {
                            mView.hideLoading();
                            mView.showError(error.getMessage());
                        }
                );

        mSubscriptions.add(subscription);
    }

    @Override
    public void openFullMoviePoster(@NonNull String poster) {
        mView.showFullScreenMoviePoster(poster);
    }

    @Override
    public void clear() {
        mSubscriptions.clear();
    }
}
