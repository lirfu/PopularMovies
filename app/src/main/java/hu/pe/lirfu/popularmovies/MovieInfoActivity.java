package hu.pe.lirfu.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import hu.pe.lirfu.popularmovies.tools.Movie;
import hu.pe.lirfu.popularmovies.tools.Movies;

/**
 * Created by lirfu on 06.02.17..
 */

public class MovieInfoActivity extends AppCompatActivity {
    public static final String MOVIE_ID_TAG = "movie_id";

    TextView title, releaseDate, runtime, rating, overview, error;
    ImageView poster;
    ProgressBar progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        title = (TextView) findViewById(R.id.tv_movie_title);
        releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        runtime = (TextView) findViewById(R.id.tv_movie_runtime);
        rating = (TextView) findViewById(R.id.tv_movie_rating);
        overview = (TextView) findViewById(R.id.tv_movie_overview);
        error = (TextView) findViewById(R.id.tv_error);

        poster = (ImageView) findViewById(R.id.iv_movie_poster);
        progress = (ProgressBar) findViewById(R.id.pb_fetching_progress);

        final String id = getIntent().getStringExtra("movie_id");
        new FetchMovieTask().execute(id);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            error.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return Movies.getById(strings[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progress.setVisibility(View.GONE);

            if (result == null) {
                error.setVisibility(View.VISIBLE);
                error.setText(R.string.error_no_internet_connection);
                return;
            }

            try {
                Movie movie = Movies.parseMovieFromString(result);

                title.setText(movie.getOriginalTitle());
                releaseDate.setText(movie.getReleaseYear());
                runtime.setText(movie.getFormattedRuntime());
                rating.setText(movie.getFormattedVoteAverage());
                overview.setText(movie.getOverview());

                Picasso.with(MovieInfoActivity.this).load(movie.getPosterUrl()).into(poster);

            } catch (JSONException e) {
                error.setVisibility(View.VISIBLE);
                error.setText(R.string.error_JSON_parsing);
            }
        }
    }
}
