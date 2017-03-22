package hu.pe.lirfu.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lirfu on 20.03.17..
 */

public class FavouriteMovieContract {
    public static final String AUTHORITY = "hu.pe.lirfu.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAV_MOVIES = "my_movies";

    public static final class FavouriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final String TABLE_NAME = "my_movies";

        public static final String COLUMN_TITLE = "title";
    }
}
