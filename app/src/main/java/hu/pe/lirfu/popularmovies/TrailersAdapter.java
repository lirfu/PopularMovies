package hu.pe.lirfu.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hu.pe.lirfu.popularmovies.tools.Trailer;

/**
 * Created by lirfu on 17.03.17..
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {
    private Context context;
    private Trailer[] trailers;

    public TrailersAdapter(Context context, Trailer[] trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailersAdapter.TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.trailer_holder, parent, false);

        TrailersAdapter.TrailerHolder holder = new TrailersAdapter.TrailerHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailerHolder holder, int position) {
        holder.bind(trailers[position]);
    }

    @Override
    public int getItemCount() {
        return trailers.length;
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        String url;
        TextView tv_title;
        Button button;

        public TrailerHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_trailer_title);
            button = (Button) itemView.findViewById(R.id.btn_trailer_play);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(url);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);

                    if (i.resolveActivity(context.getPackageManager()) != null)
                        context.startActivity(i);
                }
            });
        }

        public void bind(Trailer trailer) {
            this.url = trailer.getUrl();
            tv_title.setText(trailer.getName());
        }
    }
}
