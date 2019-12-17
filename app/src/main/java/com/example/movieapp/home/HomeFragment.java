package com.example.movieapp.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.api.MovieDbApi;
import com.example.movieapp.detail.MovieDetailActivity;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchEditText = view.findViewById(R.id.et_search);
        ImageView searchImageView = view.findViewById(R.id.img_search);
        progressBar = view.findViewById(R.id.progress_bar);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchImageView.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if(s.length() > 0){
                    movieAdapter.setMovieList(new ArrayList<>());
                    searchMovies(s.toString());
                }else{
                    loadTopMovies();
                }
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                loadTopMovies();
            }
        });

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

        loadTopMovies();
    }

    private void searchMovies(String query) {
        progressBar.setVisibility(View.VISIBLE);
        MovieDbApi.getInstance().searchMovies(query, new MovieDbApi.MovieApiCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movieList) {
                movieAdapter.setMovieList(movieList);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopMovies() {
        progressBar.setVisibility(View.VISIBLE);
        MovieDbApi.getInstance().getTopMovies(new MovieDbApi.MovieApiCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movieList) {
                movieAdapter.setMovieList(movieList);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
