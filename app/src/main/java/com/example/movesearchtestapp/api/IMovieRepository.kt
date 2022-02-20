package com.example.movesearchtestapp.api

import com.example.movesearchtestapp.BuildConfig
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.data.MovieItemDetail
import com.example.movesearchtestapp.data.MovieSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieRepository {
    fun getMovies(query: String): Single<List<MovieItem>>

    fun getMovieDetail(movieId: String): Single<MovieItemDetail>
}