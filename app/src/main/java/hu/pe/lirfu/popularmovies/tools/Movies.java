package hu.pe.lirfu.popularmovies.tools;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import hu.pe.lirfu.popularmovies.BuildConfig;

/**
 * Created by lirfu on 05.02.17..
 */

public class Movies {
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_ENDPOINT = "movie/popular";
    private static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    private static final String TRAILERS_ENDPOINT = "/videos";
    private static final String REVIEWS_ENDPOINT = "/reviews";
    private static final String ID_ENDPOINT = "movie/";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    private static final String IMAGE_SIZE = "w185";

    /**
     * Returns the fetched movie JSON string based on the movie id.
     */
    public static String getById(String id) throws IOException {
        Log.d("lirfu", "Requested movie id: "+id);
        return getContentFrom(ID_ENDPOINT + id);
    }

    public static String getTrailersFor(String id) throws IOException {
        return getContentFrom(ID_ENDPOINT + id+TRAILERS_ENDPOINT);
    }
    // TODO test getReviewsFrom
    public static String getReviewsFrom(String id) throws IOException {
        return getContentFrom(ID_ENDPOINT + id+REVIEWS_ENDPOINT);
    }

    /**
     * Returns the fetched JSON string of the most popular movies.
     */
    public static String getAllPopular() throws IOException {
        return getContentFrom(POPULAR_ENDPOINT);
    }

    /**
     * Returns the fetched JSON string of the top rated movies.
     */
    public static String getAllTopRated() throws IOException {
        return getContentFrom(TOP_RATED_ENDPOINT);
    }

    /**
     * Turn the given url into a valid uri string.
     */
    public static String parseImageUrl(String imageUrl) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_SIZE)
                .appendEncodedPath(imageUrl)
                .build()
                .toString();
    }

    /**
     * Turn the JSON string of a single movie into a simple movie object.
     */
    public static Movie parseMovieFromString(String content) throws JSONException {
        JSONObject fetchedObject = new JSONObject(content);

        return new Movie(fetchedObject);
    }

    public static Trailer[] parseTrailersFromString(String content) throws JSONException {
        JSONArray trailersJSONArray = new JSONObject(content).optJSONArray("results");
        Trailer[] trailers = new Trailer[trailersJSONArray.length()];

        for (int i = 0; i < trailersJSONArray.length(); i++)
            trailers[i] = new Trailer(trailersJSONArray.getJSONObject(i));

        return trailers;
    }
    // TODO test parseReviewsFromString
    public static Review[] parseReviewsFromString(String content) throws JSONException {
        JSONArray reviewsJSONArray = new JSONObject(content).optJSONArray("results");

        Review[] reviews = new Review[reviewsJSONArray.length()];

        for (int i = 0; i < reviewsJSONArray.length(); i++)
            reviews[i] = new Review(reviewsJSONArray.getJSONObject(i));

        return reviews;
    }

    /**
     * Turn the JSON string of multiple movies into the simple movie objects.
     */
    public static MovieSimple[] parseMoviesFromString(String content) throws JSONException {

        JSONArray moviesJSONArray = new JSONObject(content).optJSONArray("results");

        MovieSimple[] movies = new MovieSimple[moviesJSONArray.length()];

        for (int i = 0; i < moviesJSONArray.length(); i++)
            movies[i] = new MovieSimple(moviesJSONArray.getJSONObject(i));

        return movies;
    }

    /**
     * Builds URL and fetches the data from the network.
     */
    private static String getContentFrom(String endpoint) throws IOException {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(endpoint)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();

        URL url = new URL(uri.toString());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream in = connection.getInputStream();
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        String result;

        if (scanner.hasNext())
            result = scanner.next();

        else
            throw new IOException("Error fetching data from network.");

        connection.disconnect();

        return result;
    }
}
