package hu.pe.lirfu.popularmovies.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lirfu on 17.03.17..
 */

public class Review {
    private String author;
    private String content;
    private String url;

    public Review(JSONObject object) throws JSONException {
        author = object.getString("author");
        content = object.getString("content");
        url = object.getString("url");
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
