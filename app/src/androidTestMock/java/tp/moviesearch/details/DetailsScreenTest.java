package tp.moviesearch.details;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tp.moviesearch.R;
import tp.moviesearch.data.MovieRepositoryMock;
import tp.moviesearch.data.model.MovieDetails;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Tests for the details screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailsScreenTest {

    private static final String MOVIE_TITLE = "Braveheart";
    private static final String MOVIE_YEAR = "1995";
    private static final String MOVIE_RATED = "R";
    private static final String MOVIE_RELEASE = "24 May 1995";
    private static final String MOVIE_RUNTIME = "178 min";
    private static final String MOVIE_GENRE = "Biography, Drama, History";
    private static final String MOVIE_DIRECTOR = "Mel Gibson";
    private static final String MOVIE_WRITER = "Randall Wallace";
    private static final String MOVIE_ACTORS = "James Robinson, Sean Lawlor, Sandy Nelson, James Cosmo";
    private static final String MOVIE_PLOT = "When his secret bride is executed for assaulting an English soldier...";
    private static final String MOVIE_LANGUAGE = "English, French, Latin, Scottish Gaelic";
    private static final String MOVIE_COUNTRY = "USA";
    private static final String MOVIE_AWARDS = "Won 5 Oscars. Another 26 wins & 29 nominations.";
    private static final String MOVIE_POSTER = "https://images-na.ssl-images-amazon.com/images/M/MV5BNTMyNGE1ODQtYTNiNS00ZTUyLThhZjktMTgyOGZkZThlYTc3XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg";
    private static final String MOVIE_METASCORE = "68";
    private static final float MOVIE_IMDB_RATING = 8.4f;
    private static final String MOVIE_IMDB_VOTES = "776,743";
    private static final String MOVIE_IMDB_ID = "tt0112573";
    private static final String MOVIE_TYPE = "movie";

    private static final MovieDetails MOVIE_DETAILS = new MovieDetails(
            MOVIE_TITLE,
            MOVIE_YEAR,
            MOVIE_RATED,
            MOVIE_RELEASE,
            MOVIE_RUNTIME,
            MOVIE_GENRE,
            MOVIE_DIRECTOR,
            MOVIE_WRITER,
            MOVIE_ACTORS,
            MOVIE_PLOT,
            MOVIE_LANGUAGE,
            MOVIE_COUNTRY,
            MOVIE_AWARDS,
            MOVIE_POSTER,
            MOVIE_METASCORE,
            MOVIE_IMDB_RATING,
            MOVIE_IMDB_VOTES,
            MOVIE_IMDB_ID,
            MOVIE_TYPE
    );

    @Rule
    public ActivityTestRule<DetailsActivity> mDetailsActivityTestRule =
            new ActivityTestRule<>(DetailsActivity.class, true, false);

    @Before
    public void intentWithStubbedImdbId() {
        MovieRepositoryMock.setMovieDetails(MOVIE_DETAILS);

        Intent intent = new Intent();
        intent.putExtra(DetailsActivity.EXTRA_IMDB_ID, MOVIE_DETAILS.getImdbId());
        mDetailsActivityTestRule.launchActivity(intent);
    }

    @Test
    public void movieDetailsDisplayedInUi() {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(MOVIE_TITLE)));

        onView(withId(R.id.tv_movie_title)).check(matches(withText(MOVIE_TITLE)));

        final String ratingDescription = InstrumentationRegistry.getTargetContext().getResources()
                .getString(R.string.details_rating_description, String.valueOf(MOVIE_IMDB_RATING));
        onView(withId(R.id.rating_bar)).check(matches(withContentDescription(ratingDescription)));

        onView(withId(R.id.tv_released)).check(matches(withText(MOVIE_RELEASE)));
        onView(withId(R.id.tv_runtime)).check(matches(withText(MOVIE_RUNTIME)));
        onView(withId(R.id.tv_genre)).check(matches(withText(MOVIE_GENRE)));
        onView(withId(R.id.tv_country)).check(matches(withText(MOVIE_COUNTRY)));
        onView(withId(R.id.tv_language)).check(matches(withText(MOVIE_LANGUAGE)));
        onView(withId(R.id.tv_director)).check(matches(withText(MOVIE_DIRECTOR)));
        onView(withId(R.id.tv_actors)).check(matches(withText(MOVIE_ACTORS)));
        onView(withId(R.id.tv_awards)).check(matches(withText(MOVIE_AWARDS)));
        onView(withId(R.id.tv_plot)).check(matches(withText(MOVIE_PLOT)));
    }
}
