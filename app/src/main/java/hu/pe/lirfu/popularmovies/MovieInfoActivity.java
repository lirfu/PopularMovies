package hu.pe.lirfu.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import hu.pe.lirfu.popularmovies.tools.Movie;
import hu.pe.lirfu.popularmovies.tools.Movies;
import hu.pe.lirfu.popularmovies.tools.Review;
import hu.pe.lirfu.popularmovies.tools.Trailer;

/**
 * Created by lirfu on 06.02.17..
 */

public class MovieInfoActivity extends AppCompatActivity {
    public static final String MOVIE_ID_TAG = "movie_id";

    TextView title, releaseDate, runtime, rating, overview, error;
    ImageView poster;
    ProgressBar progress;
    RecyclerView rv_trailers, rv_reviews;

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
        rv_trailers = (RecyclerView) findViewById(R.id.rv_trailers);
        rv_reviews = (RecyclerView) findViewById(R.id.rv_reviews);

        LinearLayoutManager t_manager = new LinearLayoutManager(this);
        rv_trailers.setLayoutManager(t_manager);

        LinearLayoutManager r_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_reviews.setLayoutManager(r_manager);

        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_ID_TAG)) {
            String id = intent.getStringExtra(MOVIE_ID_TAG);
            new FetchMovieTask().execute(id);
        } else
            Toast.makeText(this, R.string.intent_tag_missing, Toast.LENGTH_LONG).show();
    }

    private class FetchMovieTask extends AsyncTask<String, Void, String[]> {
private int DATA_INDEX=0, TRAILERS_INDEX=1, REVIEWS_INDEX=2;

        @Override
        protected void onPreExecute() {
            error.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                return new String[]{Movies.getById(strings[0]), Movies.getTrailersFor(strings[0]), Movies.getReviewsFrom(strings[0])};
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] results) {
            progress.setVisibility(View.GONE);

            if (results == null || results.length < 3) {
                error.setVisibility(View.VISIBLE);
                error.setText(R.string.error_no_internet_connection);
                return;
            }

            try {
                Movie movie = Movies.parseMovieFromString(results[DATA_INDEX]);

                title.setText(movie.getOriginalTitle());
                releaseDate.setText(movie.getReleaseYear());
                runtime.setText(movie.getFormattedRuntime());
                rating.setText(movie.getFormattedVoteAverage());
                overview.setText(movie.getOverview());

                Picasso.with(MovieInfoActivity.this).load(movie.getPosterUrl()).into(poster);

                Trailer[] trailers = Movies.parseTrailersFromString(results[TRAILERS_INDEX]);
                TrailersAdapter t_ad = new TrailersAdapter(MovieInfoActivity.this, trailers);
                rv_trailers.setAdapter(t_ad);

                Review[] reviews = Movies.parseReviewsFromString(results[REVIEWS_INDEX]);
                ReviewsAdapter r_ad = new ReviewsAdapter(MovieInfoActivity.this, reviews);
                rv_reviews.setAdapter(r_ad);
            } catch (JSONException e) {
                error.setVisibility(View.VISIBLE);
                error.setText(R.string.error_JSON_parsing);
            }
        }
    }
}
