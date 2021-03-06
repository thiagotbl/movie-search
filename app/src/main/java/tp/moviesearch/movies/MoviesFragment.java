package tp.moviesearch.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tp.moviesearch.Injection;
import tp.moviesearch.R;
import tp.moviesearch.data.model.MovieSearchItem;
import tp.moviesearch.details.DetailsActivity;
import tp.moviesearch.util.EndlessRecyclerViewScrollListener;

/**
 * UI for the search screen.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View {

    private static final String MOVIES_KEY = "movies_key";
    private static final String QUERY_KEY = "query_key";

    private static final String STATE_LAST_SEARCH_KEY = "state_search_key";
    private static final String STATE_TOTAL_KEY = "state_total_key";
    private static final String STATE_FETCHED_KEY = "state_fetched_key";
    private static final String STATE_PER_PAGE_KEY = "state_per_page_key";
    
    private MoviesContract.UserActionsListener mActionsListener;
    private MovieAdapter mAdapter;

    private SearchView mSearchView;
    private String mQueryString;

    private View mProgressBar;
    private TextView mTextViewEmptyScreen;
    private ImageView mImgEmptyScreen;

    private RecyclerView mRecyclerView;

    public MoviesFragment() {}

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mAdapter = new MovieAdapter(getContext(), movie -> mActionsListener.openMovieDetails(movie));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(getScrollListener(layoutManager));

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mImgEmptyScreen = (ImageView) rootView.findViewById(R.id.img_empty_screen);
        mTextViewEmptyScreen = (TextView) rootView.findViewById(R.id.tv_empty_screen);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mActionsListener.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActionsListener = null;
        mRecyclerView.removeOnScrollListener(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        // restores the search result state
        if (savedInstanceState != null && savedInstanceState.getParcelableArray(MOVIES_KEY) != null) {
            Parcelable[] state = savedInstanceState.getParcelableArray(MOVIES_KEY);
            assert state != null;
            List<MovieSearchItem> movies = Arrays.asList((MovieSearchItem[]) state);
            mAdapter.setMovies(movies);
            hideEmptyStateElements();
        }

        // recovers the query string
        if (savedInstanceState != null) {
            mQueryString = savedInstanceState.getString(QUERY_KEY);
        }

        // creates the presenter with the saved state
        mActionsListener = new MoviesPresenter(Injection.provideMovieRepository(), this,
                readStateFromBundle(savedInstanceState));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // saves the search result state
        if (mAdapter.getItemCount() != 0) {
            outState.putParcelableArray(MOVIES_KEY, mAdapter.getMovies().toArray(new MovieSearchItem[]{}));
        }

        // saves the search query
        outState.putString(QUERY_KEY, mSearchView.getQuery().toString());

        // saves the presenter state
        MoviesContract.State state = mActionsListener.getState();
        writeStateToBundle(outState, state);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_search, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionsListener.searchMovie(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // recovers the search view state
        if (mQueryString != null && !mQueryString.isEmpty()) {
            menuItem.expandActionView();
            mSearchView.setQuery(mQueryString, false);
            mSearchView.clearFocus();
        }
    }

    @Override
    public void showSearchResults(@NonNull List<MovieSearchItem> movies) {
        hideEmptyStateElements();
        mAdapter.setMovies(movies);
    }

    @Override
    public void showMoreSearchResults(@NonNull List<MovieSearchItem> movies) {
        mAdapter.addMovies(movies);
    }

    @Override
    public void showEmptyResult() {
        mImgEmptyScreen.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_sentiment_dissatisfied_black_24dp, null));
        mTextViewEmptyScreen.setText(R.string.no_result);
        mImgEmptyScreen.setVisibility(View.VISIBLE);
        mTextViewEmptyScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearSearchResults() {
        mAdapter.setMovies(Collections.emptyList());
    }

    @Override
    public void showLoading() {
        hideEmptyStateElements();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMovieDetailsUi(@NonNull String imdbId) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_IMDB_ID, imdbId);
        startActivity(intent);
    }

    private void hideEmptyStateElements() {
        mImgEmptyScreen.setVisibility(View.GONE);
        mTextViewEmptyScreen.setVisibility(View.GONE);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager) {
        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                mActionsListener.getMoreResults();
            }
        };
    }

    private void writeStateToBundle(Bundle bundle, MoviesContract.State state) {
        bundle.putString(STATE_LAST_SEARCH_KEY, state.getLastSearchTerm());
        bundle.putInt(STATE_TOTAL_KEY, state.getTotal());
        bundle.putInt(STATE_FETCHED_KEY, state.getTotalFetched());
        bundle.putInt(STATE_PER_PAGE_KEY, state.getResultsPerPage());
    }

    private MoviesContract.State readStateFromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }

        return new MoviesContract.State() {
            @Override
            public String getLastSearchTerm() {
                return bundle.getString(STATE_LAST_SEARCH_KEY);
            }

            @Override
            public int getTotal() {
                return bundle.getInt(STATE_TOTAL_KEY);
            }

            @Override
            public int getTotalFetched() {
                return bundle.getInt(STATE_FETCHED_KEY);
            }

            @Override
            public int getResultsPerPage() {
                return bundle.getInt(STATE_PER_PAGE_KEY);
            }
        };
    }

    /**
     * RecyclerView ViewHolder
     */
    private static class MovieViewHolder extends RecyclerView.ViewHolder {
        
        ImageView poster;
        TextView title;
        TextView year;
        TextView type;

        MovieViewHolder(View view) {
            super(view);

            poster = (ImageView) view.findViewById(R.id.img_movie_poster);
            title = (TextView) view.findViewById(R.id.tv_movie_title);
            year = (TextView) view.findViewById(R.id.tv_movie_year);
            type = (TextView) view.findViewById(R.id.tv_type);
        }
    }

    /**
     * RecyclerView Adapter
     */
    private static class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

        interface MovieItemListener {
            void onMovieClicked(MovieSearchItem movie);
        }

        private Context mContext;
        private MovieItemListener mListener;

        private List<MovieSearchItem> mMovies;

        MovieAdapter(Context context, MovieItemListener listener) {
            mContext = context;
            mListener = listener;
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            
            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position) {
            MovieSearchItem movie = mMovies.get(position);

            holder.title.setText(movie.getTitle());
            holder.year.setText(movie.getYear());
            holder.type.setText(movie.getType());

            Glide.with(mContext)
                    .load(movie.getPoster())
                    .crossFade()
                    .into(holder.poster);

            holder.itemView.setOnClickListener(view -> mListener.onMovieClicked(mMovies.get(position)));
        }

        @Override
        public int getItemCount() {
            if (mMovies == null) {
                return 0;
            }
            
            return mMovies.size();
        }

        List<MovieSearchItem> getMovies() {
            return mMovies;
        }

        void setMovies(List<MovieSearchItem> movies) {
            mMovies = new ArrayList<>(movies);
            notifyDataSetChanged();
        }

        void addMovies(List<MovieSearchItem> movies) {
            mMovies.addAll(movies);
            notifyDataSetChanged();
        }
    }
}
