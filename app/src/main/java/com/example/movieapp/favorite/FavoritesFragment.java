package com.example.movieapp.favorite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.db.SQLiteHelper;
import com.example.movieapp.detail.MovieDetailActivity;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    private MovieAdapter movieAdapter;
    private SQLiteDatabase sqLiteDatabaseObj;
    private SQLiteHelper sqLiteHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sqLiteHelper = new SQLiteHelper(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        movieAdapter = new MovieAdapter(getContext(), false);
        movieAdapter.setListener(new MovieAdapter.Callback() {
            @Override
            public void onMovieClicked(Movie movie) {
                startActivity(MovieDetailActivity.newIntent(getContext(), movie.getId()));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME_FAVORITES, null, null, null, null, null, null);

        List<Movie> movieList = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_MOVIE_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_MOVIE_DESC));
                String picture = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_MOVIE_PICTURE));

                Movie movie = new Movie(id, title, desc, picture);
                movieList.add(movie);
            }
        } finally {
            cursor.close();
        }

        movieAdapter.setMovieList(movieList);
        movieAdapter.setListener(new MovieAdapter.Callback() {
            @Override
            public void onMovieClicked(Movie movie) {
                startActivity(MovieDetailActivity.newIntent(getContext(), movie.getId()));
            }
        });
    }
}
