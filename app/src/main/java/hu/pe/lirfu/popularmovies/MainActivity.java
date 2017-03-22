package hu.pe.lirfu.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import hu.pe.lirfu.popularmovies.db.FavouriteMovieContract;
import hu.pe.lirfu.popularmovies.tools.MovieSimple;
import hu.pe.lirfu.popularmovies.tools.Movies;

import static hu.pe.lirfu.popularmovies.R.menu.menu_main;


public class MainActivity extends AppCompatActivity {
    static final String SORTING_EXTRA_TAG = "MainActivity.sorting";

    TextView errorView;
    ProgressBar loadingBar;
    RecyclerView moviesFeed;

    private Sorting sortedBy = Sorting.POPULARITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorView = (TextView) findViewById(R.id.tv_error);
        loadingBar = (ProgressBar) findViewById(R.id.pb_fetching_progress);
        moviesFeed = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager manager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            manager = new GridLayoutManager(this, 2);
        } else {
            manager = new GridLayoutManager(this, 4);
        }
        moviesFeed.setLayoutManager(manager);
        moviesFeed.setHasFixedSize(true);

        new FetchMoviesTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_sort);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorting_array, R.layout.activity_bar_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.onOptionsItemSelected(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        onOptionsItemSelected(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.menu_sort) {
            String selectedString = (String) ((Spinner) MenuItemCompat.getActionView(item)).getSelectedItem();

            if (selectedString.equals(getString(R.string.entry_popular))) {
                sortedBy = Sorting.POPULARITY;
            } else if (selectedString.equals(getString(R.string.entry_toprated))) {
                sortedBy = Sorting.TOP_RATED;
            } else if (selectedString.equals(getString(R.string.entry_favourite))) {
                sortedBy = Sorting.FAVOURITES;
            }

            new FetchMoviesTask().execute();

        }

        return true;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Integer, MovieSimple[]> {
        @Override
        protected void onPreExecute() {
            errorView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieSimple[] doInBackground(Void... voids) {
            try {
                switch (sortedBy) {
                    case FAVOURITES:
                        try {
                            Uri uri = FavouriteMovieContract.FavouriteMovieEntry.CONTENT_URI;
                            Cursor query = getContentResolver().query(uri, null, null, null, null);

                            if (query.getCount() == 0)
                                return new MovieSimple[]{};

                            int indexOfIdColumn = query.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry._ID);
                            int i = 0;
                            MovieSimple[] movies = new MovieSimple[query.getCount()];
                            while (query.moveToNext()) {
                                movies[i] = Movies.parseMovieFromString(Movies.getById(query.getString(indexOfIdColumn)));
                                i++;
                            }

                            query.close();
                            return movies;

                        } catch (Exception e) {
                            Log.e("lirfu", "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }

                    case POPULARITY:
                        return Movies.parseMoviesFromString(Movies.getAllPopular());
                    case TOP_RATED:
                        return Movies.parseMoviesFromString(Movies.getAllTopRated());
                    default:
                        return null;
                }
            } catch (JSONException | IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieSimple[] result) {
            loadingBar.setVisibility(View.GONE);

            if (result == null) {
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(R.string.error_no_internet_connection);
                return;
            }
            else if(result.length==0 && sortedBy.equals(Sorting.FAVOURITES)){
                errorView.setVisibility(View.VISIBLE);
                errorView.setText("You have no favourite movies.");
            }

            MoviesAdapter adapter = new MoviesAdapter(MainActivity.this, result);
            moviesFeed.setAdapter(adapter);
        }
    }

    static enum Sorting {
        POPULARITY, TOP_RATED, FAVOURITES
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        if (i != null) {
            String sorting = i.getStringExtra(SORTING_EXTRA_TAG);
            if (sorting != null) {
                sortedBy = Sorting.valueOf(sorting);
            }
        }
        Log.d("lirfu", "onResume: "+sortedBy);
    }
}
