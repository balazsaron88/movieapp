package com.example.movieapp.detail;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.api.MovieDbApi;
import com.example.movieapp.db.SQLiteHelper;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieAdapter;

import java.util.List;

public class MovieDetailFragment extends Fragment {

    public static MovieDetailFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("key_id", id);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView posterImageView;
    private ImageView closeImageView;
    private ImageView favoriteImageView;

    private TextView titleTextView;
    private TextView descriptionTextView;

    private RecyclerView relatedRecyclerView;
    private ProgressBar progressBar;

    private SQLiteDatabase sqLiteDatabaseObj;
    private SQLiteHelper sqLiteHelper;

    private Movie movie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sqLiteHelper = new SQLiteHelper(getContext());

        posterImageView = view.findViewById(R.id.img_cover);
        closeImageView = view.findViewById(R.id.img_close);
        favoriteImageView = view.findViewById(R.id.img_favorite);
        titleTextView = view.findViewById(R.id.tv_title);
        descriptionTextView = view.findViewById(R.id.tv_description);
        relatedRecyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        String id = getArguments().getString("key_id");

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        boolean favorite = isFavorite(id);
        favoriteImageView.setImageResource(favorite? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorite(movie, !favorite);
                favoriteImageView.setImageResource(!favorite? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }
        });

        loadMovieDetails(id);
    }

    private void loadMovieDetails(String id) {
        progressBar.setVisibility(View.VISIBLE);
        MovieDbApi.getInstance().getMovieDetails(id, new MovieDbApi.MovieApiCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movieList) {
                progressBar.setVisibility(View.GONE);
                if (movieList.size() > 0) {
                    movie = movieList.get(0);
                    bindDetails(movie);

                    loadSimilarMovies(id);
                }
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSimilarMovies(String id) {
        MovieDbApi.getInstance().getSimilarMovies(id, new MovieDbApi.MovieApiCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movieList) {
                bindSimilarMovies(movieList);
            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindDetails(Movie movie) {
        titleTextView.setText(movie.getName());
        descriptionTextView.setText(movie.getDescription());

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPicture())
                .into(posterImageView);
    }

    private void bindSimilarMovies(List<Movie> movieList) {
        MovieAdapter adapter = new MovieAdapter(getContext(), true);
        relatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        relatedRecyclerView.setAdapter(adapter);

        adapter.setListener(new MovieAdapter.Callback() {
            @Override
            public void onMovieClicked(Movie movie) {
                startActivity(MovieDetailActivity.newIntent(getContext(), movie.getId()));
            }
        });

        adapter.setMovieList(movieList);
    }

    private void addToFavorite(Movie movie, boolean add) {

        String query = null;
        if (add) {
            // SQLite query to insert data into table.

            query = "INSERT INTO " + SQLiteHelper.TABLE_NAME_FAVORITES + " VALUES" + "(?, ?, ?, ?)";
            sqLiteDatabaseObj.execSQL(query, new String[]{movie.getId(), movie.getName(), movie.getDescription(), movie.getPicture()});
                 /*   "'" + movie.getId() + "', " +
                    "'" + movie.getName() + "'," +
                    " '" + DatabaseUtils.sqlEscapeString(movie.getDescription()) + "', " +
                    "'" + movie.getPicture() + "');";*/
        } else {
            query = "DELETE FROM " + SQLiteHelper.TABLE_NAME_FAVORITES + " WHERE " + SQLiteHelper.COLUMN_MOVIE_ID + "=" + "'" + movie.getId() + "'";
            // Executing query.
            sqLiteDatabaseObj.execSQL(query);
        }



        // Closing SQLite database object.
        sqLiteDatabaseObj.close();
    }

    private boolean isFavorite(String id) {
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME_FAVORITES, null, " " + SQLiteHelper.COLUMN_MOVIE_ID + "=?", new String[]{id}, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    private void close() {
        getActivity().finish();
    }
}
