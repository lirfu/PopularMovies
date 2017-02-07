package hu.pe.lirfu.popularmovies.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lirfu on 05.02.17..
 */

public class MovieSimple {
    private String id;
    private String poster_image_url;

    public MovieSimple(JSONObject object) throws JSONException {
        this.id = object.getString("id");
        this.poster_image_url = Movies.parseImageUrl(object.getString("poster_path"));
    }

    public String getId() {
        return id;
    }

    public String getPosterUrl() {
        return poster_image_url;
    }
}
