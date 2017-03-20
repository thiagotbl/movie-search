package tp.moviesearch.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import tp.moviesearch.Injection;
import tp.moviesearch.R;
import tp.moviesearch.data.model.MovieSearchItem;
import tp.moviesearch.details.DetailsActivity;

/**
 * UI for the search screen.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View {
    
    private MoviesContract.UserActionsListener mActionsListener;
    private MovieAdapter mAdapter;

    private View mProgressBar;
    private TextView mTextViewEmptyScreen;
    private ImageView mImgEmptyScreen;

    public MoviesFragment() {}

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new MoviesPresenter(Injection.provideMovieRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new MovieAdapter(getContext(), movie -> mActionsListener.openMovieDetails(movie));

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mImgEmptyScreen = (ImageView) rootView.findViewById(R.id.img_empty_screen);
        mTextViewEmptyScreen = (TextView) rootView.findViewById(R.id.tv_empty_screen);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    @Override
    public void showSearchResults(@NonNull List<MovieSearchItem> movies) {
        hideAllElements();
        mAdapter.setMovies(movies);
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
        hideAllElements();
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

    private void hideAllElements() {
        mImgEmptyScreen.setVisibility(View.GONE);
        mTextViewEmptyScreen.setVisibility(View.GONE);
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

        void setMovies(List<MovieSearchItem> movies) {
            mMovies = movies;
            notifyDataSetChanged();
        }
    }
}
