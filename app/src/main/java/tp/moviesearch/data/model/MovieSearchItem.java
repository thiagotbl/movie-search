package tp.moviesearch.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a search result.
 */
public class MovieSearchItem implements Parcelable {

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

    private MovieSearchItem(Parcel in) {
        title = in.readString();
        year = in.readString();
        imdbId = in.readString();
        type = in.readString();
        poster = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(imdbId);
        dest.writeString(type);
        dest.writeString(poster);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieSearchItem> CREATOR =
            new Parcelable.Creator<MovieSearchItem>() {

        @Override
        public MovieSearchItem createFromParcel(Parcel in) {
            return new MovieSearchItem(in);
        }

        @Override
        public MovieSearchItem[] newArray(int size) {
            return new MovieSearchItem[size];
        }
    };
}
