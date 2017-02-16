package hu.pe.lirfu.popularmovies;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import hu.pe.lirfu.popularmovies.tools.MovieSimple;
import hu.pe.lirfu.popularmovies.tools.Movies;

import static hu.pe.lirfu.popularmovies.R.menu.menu_main;


public class MainActivity extends AppCompatActivity {

    TextView errorView;
    ProgressBar loadingBar;
    RecyclerView moviesFeed;

    private boolean sortedByPopularity = true;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.menu_sort) {

            if (getString(R.string.sort_by_popularity).equals(item.getTitle())) {
                sortedByPopularity = true;
                item.setTitle(R.string.sort_by_top_rated);
            } else {
                sortedByPopularity = false;
                item.setTitle(R.string.sort_by_popularity);
            }

            new FetchMoviesTask().execute();

        }

        return true;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            errorView.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if (sortedByPopularity)
                    return Movies.getAllPopular();
                else
                    return Movies.getAllTopRated();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            loadingBar.setVisibility(View.GONE);

            if (s == null) {
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(R.string.error_no_internet_connection);
                return;
            }

            try {
                MovieSimple[] movies = Movies.parseMoviesFromString(s);

                MoviesAdapter adapter = new MoviesAdapter(MainActivity.this, movies);
                moviesFeed.setAdapter(adapter);
            } catch (JSONException e) {
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(R.string.error_JSON_parsing);
            }
        }
    }
}
