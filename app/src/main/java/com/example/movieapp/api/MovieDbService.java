package com.example.movieapp.api;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbService {

    @GET("movie/top_rated?api_key=c3411686c83dfce3056aa0f53abdcb46")
    Call<MovieResponse> getTopMovies();

    @GET("movie/{id}?api_key=c3411686c83dfce3056aa0f53abdcb46")
    Call<Movie> getMovieDetails(@Path("id") String id);

    @GET("movie/{id}/similar?api_key=c3411686c83dfce3056aa0f53abdcb46")
    Call<MovieResponse> getSimilarMovies(@Path("id") String id);

    @GET("search/movie?api_key=c3411686c83dfce3056aa0f53abdcb46")
    Call<MovieResponse> searchMovies(@Query("query") String title);
}
