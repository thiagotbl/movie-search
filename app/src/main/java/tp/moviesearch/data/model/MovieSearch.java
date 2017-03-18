package tp.moviesearch.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents the search result. Keeps a list of {@link MovieSearchItem}.
 */
public class MovieSearch {

    @SerializedName("totalResults") private final int totalResults;
    @SerializedName("Search") private final List<MovieSearchItem> movies;

    public MovieSearch(int totalResults, List<MovieSearchItem> movies) {
        this.totalResults = totalResults;
        this.movies = movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<MovieSearchItem> getMovies() {
        return movies;
    }
}
