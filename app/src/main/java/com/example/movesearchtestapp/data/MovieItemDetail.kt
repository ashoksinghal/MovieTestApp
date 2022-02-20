package com.example.movesearchtestapp.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItemDetail(
    @SerializedName("Actors")
    val actors: String?=null,
    @SerializedName("Director")
    val director: String?=null,
    @SerializedName("Genre")
    val imdbID: String?=null,
    @SerializedName("imdbRating")
    val imdbRating: String?=null,
    @SerializedName("Plot")
    val plot: String?=null,
    @SerializedName("Poster")
    val poster: String?=null,
    @SerializedName("Released")
    val released: String?=null,
    @SerializedName("Response")
    val response: String?=null,
    @SerializedName("Title")
    val title: String?=null,
    @SerializedName("Type")
    val type: String?=null,
    @SerializedName("Year")
    val year: String?=null,
    @SerializedName("Error")
    val error: String? = null
): Parcelable