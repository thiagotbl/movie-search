package tp.moviesearch.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import tp.moviesearch.R;

/**
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_IMDB_ID = "imdb_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            String imdbId = getIntent().getStringExtra(EXTRA_IMDB_ID);
            Fragment fragment = DetailsFragment.newInstance(imdbId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
