package hu.pe.lirfu.popularmovies.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lirfu on 05.02.17..
 */

public class Movie extends MovieSimple {
    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;
    private String runtime;

    public Movie(JSONObject object) throws JSONException {
        super(object);

        this.original_title = object.getString("original_title");
        this.overview = object.getString("overview");
        this.vote_average = object.getString("vote_average");
        this.release_date = object.getString("release_date");
        this.runtime = object.getString("runtime");
    }

    public String getFormattedVoteAverage() {
        return vote_average + "/10";
    }

    public String getFormattedRuntime() {
        return runtime + "min";
    }

    public String getReleaseYear() {
        return release_date.split("-")[0];
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getRuntime() {
        return runtime;
    }
}
