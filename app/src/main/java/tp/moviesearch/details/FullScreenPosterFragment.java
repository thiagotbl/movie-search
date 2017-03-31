package tp.moviesearch.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tp.moviesearch.R;

/**
 * UI to show a full screen Image View.
 */
public class FullScreenPosterFragment extends Fragment {

    private static final String ARGUMENT_POSTER = "arg-poster";

    public FullScreenPosterFragment() {}

    public static FullScreenPosterFragment newInstance(@NonNull String poster) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_POSTER, poster);

        FullScreenPosterFragment fragment = new FullScreenPosterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fullscreen_poster, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.img_movie_poster);

        Glide.with(this)
                .load(getArguments().getString(ARGUMENT_POSTER))
                .crossFade()
                .into(imageView);

        return rootView;
    }
}
