package com.example.movesearchtestapp.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class MovieItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Poster")
    val posterUrl: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("favourite")
    var isFavourite: Boolean? = false
){
    fun getMovieDetailItem(): MovieItemDetail
    {
        return MovieItemDetail(imdbID= imdbID, poster = posterUrl, title = title, year = year)
    }
}