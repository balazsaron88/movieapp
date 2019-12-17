package com.example.movieapp.api;

import android.util.Log;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDbApi {

    public static MovieDbApi getInstance() {
        if (instance == null) {
            instance = new MovieDbApi();
        }
        return instance;
    }

    private static MovieDbApi instance;

    private MovieDbService service;

    private MovieDbApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.themoviedb.org/3/")
                .build();

        service = retrofit.create(MovieDbService.class);
    }

    public void getMovieDetails(String id, final MovieApiCallback callback) {
        service.getMovieDetails(id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d("TAG", "onResponse");
                Movie movie = response.body();
                List<Movie> result = new ArrayList<>();
                result.add(movie);

                if (callback != null) {
                    callback.onMoviesLoaded(result);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("TAG", "onError");

                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    public void getTopMovies(final MovieApiCallback callback) {
        service.getTopMovies().enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d("TAG", "onResponse");
                MovieResponse movieResponse = response.body();
                List<Movie> result = new ArrayList<>();
                if (movieResponse != null && movieResponse.getMovieList() != null) {
                    result.addAll(movieResponse.getMovieList());
                }

                if (callback != null) {
                    callback.onMoviesLoaded(result);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("TAG", "onError");

                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    public void getSimilarMovies(String id, final MovieApiCallback callback) {
        service.getSimilarMovies(id).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d("TAG", "onResponse");
                MovieResponse movieResponse = response.body();
                List<Movie> result = new ArrayList<>();
                if (movieResponse != null && movieResponse.getMovieList() != null) {
                    result.addAll(movieResponse.getMovieList());
                }

                if (callback != null) {
                    callback.onMoviesLoaded(result);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("TAG", "onError");

                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    public void searchMovies(String title, final MovieApiCallback callback) {
        service.searchMovies(title).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d("TAG", "onResponse");
                MovieResponse movieResponse = response.body();
                List<Movie> result = new ArrayList<>();
                if (movieResponse != null && movieResponse.getMovieList() != null) {
                    result.addAll(movieResponse.getMovieList());
                }

                if (callback != null) {
                    callback.onMoviesLoaded(result);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("TAG", "onError");

                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }


    public interface MovieApiCallback {
        void onMoviesLoaded(List<Movie> movieList);

        void onError();
    }
}
