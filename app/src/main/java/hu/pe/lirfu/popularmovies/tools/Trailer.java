package hu.pe.lirfu.popularmovies.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lirfu on 17.03.17..
 */
public class Trailer {
    private String name;
    private String url;
    public Trailer(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        this.url = "https://www.youtube.com/watch?v=" + object.getString("key");
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
