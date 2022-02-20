package com.example.movesearchtestapp.api

import com.example.movesearchtestapp.BuildConfig
import com.example.movesearchtestapp.data.MovieItemDetail
import com.example.movesearchtestapp.data.MovieSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("/")
    fun searchMovies(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("apikey") clientId: String = BuildConfig.API_KEY
    ): Single<MovieSearchResponse>

    @GET("/")
    fun getMovieDetail(
        @Query("i") movieId: String,
        @Query("apikey") clientId: String = BuildConfig.API_KEY
    ): Single<MovieItemDetail>
}