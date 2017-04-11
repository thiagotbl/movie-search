package tp.moviesearch.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tp.moviesearch.Injection;
import tp.moviesearch.R;
import tp.moviesearch.data.model.MovieDetails;

import static tp.moviesearch.details.DetailsContract.UserActionsListener;

/**
 */
public class DetailsFragment extends Fragment implements DetailsContract.View {

    private static final String ARGUMENT_IMDB_ID = "imdb_id";

    private UserActionsListener mActionsListener;

    private ActionBar mActionBar;

    private View mProgressBar;

    private View mDetails;

    private ImageView mImgPoster;
    private TextView mTvTitle;
    private RatingBar mRatingBar;

    private TextView mTvReleased;
    private TextView mTvRuntime;
    private TextView mTvGenre;
    private TextView mTvCountry;
    private TextView mTvLanguage;

    private TextView mTvRated;

    private TextView mTvDirector;
    private TextView mTvActors;
    private TextView mTvAwards;
    private TextView mTvPlot;

    public DetailsFragment() {}

    public static DetailsFragment newInstance(@NonNull String imdbId) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_IMDB_ID, imdbId);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new DetailsPresenter(Injection.provideMovieRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("");

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        mDetails = rootView.findViewById(R.id.details);

        mImgPoster = (ImageView) rootView.findViewById(R.id.img_poster);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);

        mTvReleased = (TextView) rootView.findViewById(R.id.tv_released);
        mTvRuntime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mTvGenre = (TextView) rootView.findViewById(R.id.tv_genre);
        mTvCountry = (TextView) rootView.findViewById(R.id.tv_country);
        mTvLanguage = (TextView) rootView.findViewById(R.id.tv_language);

        mTvRated = (TextView) rootView.findViewById(R.id.tv_rated);

        mTvDirector = (TextView) rootView.findViewById(R.id.tv_director);
        mTvActors = (TextView) rootView.findViewById(R.id.tv_actors);
        mTvAwards = (TextView) rootView.findViewById(R.id.tv_awards);
        mTvPlot = (TextView) rootView.findViewById(R.id.tv_plot);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String imdbId = getArguments().getString(ARGUMENT_IMDB_ID);
        assert imdbId != null;
        mActionsListener.getMovieDetails(imdbId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActionsListener.clear();
    }

    @Override
    public void showMovieDetails(@NonNull MovieDetails movie) {
        mDetails.setVisibility(View.VISIBLE);

        mActionBar.setTitle(movie.getTitle());

        Glide.with(this)
                .load(movie.getPoster())
                .crossFade()
                .into(mImgPoster);

        mImgPoster.setOnClickListener(v -> mActionsListener.openFullMoviePoster(movie.getPoster()));

        mTvTitle.setText(movie.getTitle());

        mRatingBar.setRating(movie.getImdbRating() / 10 * mRatingBar.getNumStars());
        mRatingBar.setContentDescription(getString(R.string.details_rating_description,
                String.valueOf(movie.getImdbRating())));

        mTvReleased.setText(movie.getReleaseDate());
        mTvRuntime.setText(movie.getRuntime());
        mTvGenre.setText(movie.getGenre());
        mTvCountry.setText(movie.getCountry());
        mTvLanguage.setText(movie.getLanguage());

        mTvRated.setText(movie.getRated());

        mTvDirector.setText(movie.getDirector());
        mTvActors.setText(movie.getActors());
        mTvAwards.setText(movie.getAwards());
        mTvPlot.setText(movie.getPlot());
    }

    @Override
    public void showLoading() {
        mDetails.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFullScreenMoviePoster(@NonNull String poster) {
        Fragment fullScreenFragment = FullScreenPosterFragment.newInstance(poster);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fullScreenFragment)
                .addToBackStack("full-screen-poster")
                .commit();
    }

    @Override
    public void showError(@Nullable String message) {

    }
}
