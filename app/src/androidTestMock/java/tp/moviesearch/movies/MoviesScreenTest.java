package tp.moviesearch.movies;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tp.moviesearch.R;
import tp.moviesearch.data.MovieRepositoryMock;
import tp.moviesearch.data.model.MovieSearchItem;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the main screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MoviesScreenTest {

    private static final MovieSearchItem[] SEARCH_ITEMS = new MovieSearchItem[]{
            new MovieSearchItem("Movie 01", "1991", "tt01", "movie", "http://01.jpg"),
            new MovieSearchItem("Movie 02", "1992", "tt02", "movie", "http://02.jpg"),
            new MovieSearchItem("Movie 03", "2003", "tt03", "movie", "http://03.jpg"),
            new MovieSearchItem("TV Series 04", "2004-2007", "tt04", "series", "http://04.jpg"),
            new MovieSearchItem("Game 05", "2005", "tt05", "game", "http://05.jpg"),
    };

    @Rule
    public ActivityTestRule<MoviesActivity> mMoviesActivityTestRule =
            new ActivityTestRule<>(MoviesActivity.class, true, true);

    @Before
    public void setupRepository() {
        MovieRepositoryMock.setSearchResult(SEARCH_ITEMS);
    }

    @Test
    public void showSearchResult() {
        onView(withId(R.id.menu_search)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("movies"), pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("Movie 01"))));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("1991"))));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(1));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("Movie 02"))));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("1992"))));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("Movie 03"))));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("2003"))));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(3));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("TV Series 04"))));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("2004-2007"))));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(4));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("Game 05"))));
        onView(withId(R.id.recycler_view)).perform(scrollTo(hasDescendant(withText("2005"))));
    }
}
