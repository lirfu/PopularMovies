package hu.pe.lirfu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import hu.pe.lirfu.popularmovies.tools.MovieSimple;

/**
 * Created by lirfu on 06.02.17..
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {
    private Context context;
    private MovieSimple[] movies;

    public MoviesAdapter(Context context, MovieSimple[] movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.movie_simple_holder, parent, false);

        MovieHolder holder = new MovieHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bind(movies[position]);
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String id;
        ImageView poster;

        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            poster = (ImageView) itemView.findViewById(R.id.iv_movie_simple_poster);
        }

        public void bind(MovieSimple movie) {
            this.id = movie.getId();

            Picasso.with(context).load(movie.getPosterUrl()).into(poster);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, MovieInfoActivity.class);

            i.putExtra(MovieInfoActivity.MOVIE_ID_TAG, this.id);

            context.startActivity(i);
        }
    }
}
