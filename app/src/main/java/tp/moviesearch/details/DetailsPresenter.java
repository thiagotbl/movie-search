package tp.moviesearch.details;

import android.support.annotation.NonNull;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tp.moviesearch.data.MovieRepository;

/**
 */
class DetailsPresenter implements DetailsContract.UserActionsListener {

    private MovieRepository mRepository;
    private DetailsContract.View mView;

    DetailsPresenter(MovieRepository repository, DetailsContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void getMovieDetails(@NonNull String imdbId) {
        mView.showLoading();

        mRepository.getMovieDetails(imdbId)
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
    }
}
