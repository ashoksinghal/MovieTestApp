package com.example.movesearchtestapp.api

import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.data.MovieItemDetail
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val movieService: MovieApiInterface) : IMovieRepository {

    override fun getMovies(query: String): Single<List<MovieItem>> {
        return movieService.searchMovies(query).compose(ErrorComposer()).map { response ->
            return@map response.result
        }
    }

    override fun getMovieDetail(movieId: String): Single<MovieItemDetail> {
        return movieService.getMovieDetail(movieId).compose(MovieDetailErrorComposer())
    }
}