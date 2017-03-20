package tp.moviesearch.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for the details of a movie
 */
public class MovieDetails {

    @SerializedName("Title") private final String title;
    @SerializedName("Year") private final String year;
    @SerializedName("Rated") private final String rated;
    @SerializedName("Released") private final String releaseDate;
    @SerializedName("Runtime") private final String runtime;
    @SerializedName("Genre") private final String genre;
    @SerializedName("Director") private final String director;
    @SerializedName("Writer") private final String writer;
    @SerializedName("Actors") private final String actors;
    @SerializedName("Plot") private final String plot;
    @SerializedName("Language") private final String language;
    @SerializedName("Country")  private final String country;
    @SerializedName("Awards") private final String awards;
    @SerializedName("Poster") private final String poster;
    @SerializedName("Metascore")  private final String metascore;
    @SerializedName("imdbRating")  private final float imdbRating;
    @SerializedName("imdbVotes")  private final String imdbVotes;
    @SerializedName("imdbID")  private final String imdbId;
    @SerializedName("Type")  private final String type;

    public MovieDetails(String title, String year, String rated, String releaseDate, String runtime,
                        String genre, String director, String writer, String actors, String plot,
                        String language, String country, String awards, String poster, String metascore,
                        float imdbRating, String imdbVotes, String imdbId, String type) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbId = imdbId;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getPoster() {
        return poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getType() {
        return type;
    }
}
