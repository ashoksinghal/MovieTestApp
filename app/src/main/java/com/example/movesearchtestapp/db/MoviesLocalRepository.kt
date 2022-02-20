package com.example.movesearchtestapp.db

import com.example.movesearchtestapp.data.MovieItem
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class MoviesLocalRepository @Inject constructor(
    private val movieDao: MovieDao
) {

     fun markMovieAsFavourite(movie: MovieItem): Single<Long> {
       return Single.fromCallable{ movieDao.insert(movie) }
    }

    fun removeMovieFromFavourite(movieID: String): Single<Unit> {
        return Single.fromCallable{movieDao.deleteMovie(movieID)}
    }

    fun getMoviesList(): Flowable<List<MovieItem>> {
        return movieDao.getMovies()
    }
}
