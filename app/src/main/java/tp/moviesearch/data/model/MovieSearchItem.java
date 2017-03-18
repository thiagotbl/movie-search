package tp.moviesearch.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a search result.
 */
public class MovieSearchItem {

    @SerializedName("Title") private final String title;
    @SerializedName("Year") private final String year;
    @SerializedName("imdbID") private final String imdbId;
    @SerializedName("Type") private final String type;
    @SerializedName("Poster") private final String poster;

    public MovieSearchItem(String title, String year, String imdbId, String type, String poster) {
        this.title = title;
        this.year = year;
        this.imdbId = imdbId;
        this.type = type;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }
}
