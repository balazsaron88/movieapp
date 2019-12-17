package com.example.movieapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final Context context;
    private final boolean usePortraitLayout;

    private List<Movie> movieList;
    private Callback listener;

    public MovieAdapter(Context context, boolean usePortraitLayout) {
        this.context = context;
        this.usePortraitLayout = usePortraitLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res = usePortraitLayout ? R.layout.item_movie_portrait : R.layout.item_movie_landscape;
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.nameTextView.setText(movie.getName());

        if (holder.descriptionTextView != null) {
            holder.descriptionTextView.setText(movie.getDescription());
        }

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPicture())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMovieClicked(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    public interface Callback {
        void onMovieClicked(Movie movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, descriptionTextView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_title);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.img_cover);
        }
    }
}
