package hu.pe.lirfu.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lirfu on 20.03.17..
 */

public class FavouriteMovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FavouriteMoviesDb.db";
    private static final int VERSION = 1;

    FavouriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME + " (" +
                FavouriteMovieContract.FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavouriteMovieContract.FavouriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
