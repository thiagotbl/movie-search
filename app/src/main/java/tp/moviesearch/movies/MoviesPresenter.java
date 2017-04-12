package tp.moviesearch.movies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    private String mLastSearch;
    private int mFetched;
    private int mTotal;
    private int mResultsPerPage;

    private CompositeSubscription mSubscriptions;

    MoviesPresenter(@NonNull MovieRepository repository, @NonNull MoviesContract.View view) {
        this(repository, view, null);
    }

    MoviesPresenter(@NonNull MovieRepository repository, @NonNull MoviesContract.View view,
                    @Nullable MoviesContract.State state) {
        mRepository = repository;
        mView = view;
        mSubscriptions = new CompositeSubscription();

        if (state != null) {
            mLastSearch = state.getLastSearchTerm();
            mFetched = state.getTotalFetched();
            mTotal = state.getTotal();
            mResultsPerPage = state.getResultsPerPage();
        }
    }

    @Override
    public void searchMovie(@NonNull String title) {
        mView.showLoading();
        mView.clearSearchResults();

        if (title.trim().isEmpty()) {
            return;
        }

        clearState();
        mLastSearch = title.trim();

        mSubscriptions.clear();
        Subscription subscription = mRepository.search(mLastSearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movieSearch -> {
                            mView.hideLoading();

                            if (movieSearch.getMovies().isEmpty()) {
                                mView.showEmptyResult();
                                mTotal = 0;
                                mFetched = 0;
                            } else {
                                mView.showSearchResults(movieSearch.getMovies());
                                mTotal = movieSearch.getTotalResults();
                                mFetched = movieSearch.getMovies().size();
                                mResultsPerPage = movieSearch.getMovies().size();
                            }
                        },
                        error -> {
                            mView.hideLoading();
                            mView.showError();
                            mSubscriptions.clear();
                        },
                        () -> mSubscriptions.clear()
                );
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMoreResults() {
        if (mSubscriptions.hasSubscriptions()) {
            return;
        }

        if (mFetched == mTotal) {
            return;
        }

        int nextPage = (mFetched / mResultsPerPage) + 1;
        int totalPages = (mTotal / mResultsPerPage) + ((mTotal % mResultsPerPage) == 0 ? 0 : 1);

        if (nextPage > totalPages) {
            return;
        }

        Subscription subscription = mRepository.search(mLastSearch, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movieSearch -> {
                            if (!movieSearch.getMovies().isEmpty()) {
                                mView.showMoreSearchResults(movieSearch.getMovies());
                                mFetched += movieSearch.getMovies().size();
                            }
                        },
                        error -> {
                            mView.showError();
                            mSubscriptions.clear();
                        },
                        () -> mSubscriptions.clear()
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

    @Override
    public MoviesContract.State getState() {
        return new MoviesContract.State() {
            @Override
            public String getLastSearchTerm() {
                return mLastSearch;
            }

            @Override
            public int getTotal() {
                return mTotal;
            }

            @Override
            public int getTotalFetched() {
                return mFetched;
            }

            @Override
            public int getResultsPerPage() {
                return mResultsPerPage;
            }
        };
    }

    private void clearState() {
        mLastSearch = null;
        mTotal = 0;
        mFetched = 0;
        mResultsPerPage = 0;
    }
}
