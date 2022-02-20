package com.example.movesearchtestapp.db

import androidx.room.Dao
import androidx.room.Query
import com.example.movesearchtestapp.data.MovieItem
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : BaseDao<MovieItem> {
    @Query("SELECT * FROM movie")
    fun getMovies(): Flowable<List<MovieItem>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovie(movieId: String): Flowable<MovieItem>

    @Query("DELETE  FROM movie WHERE id = :id")
    abstract fun deleteMovie(id: String)
}