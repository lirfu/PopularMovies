package hu.pe.lirfu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.pe.lirfu.popularmovies.tools.Review;

/**
 * Created by lirfu on 18.03.17..
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {
    private Context context;
    private Review[] reviews;

    public ReviewsAdapter(Context context, Review[] reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewsAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.review_holder, parent, false);

        ReviewsAdapter.ReviewHolder holder = new ReviewsAdapter.ReviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewHolder holder, int position) {
        holder.bind(reviews[position]);
    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String url;
        TextView tv_author;
        TextView tv_content;

        public ReviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tv_author = (TextView) itemView.findViewById(R.id.tv_review_author);
            tv_content = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        public void bind(Review review) {
            this.url = review.getUrl();
            tv_author.setText(review.getAuthor());
            tv_content.setText(review.getContent());
        }

        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse(url);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);

            if (i.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(i);
        }
    }
}