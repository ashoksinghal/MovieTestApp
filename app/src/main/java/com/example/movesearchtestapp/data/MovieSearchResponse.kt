package com.example.movesearchtestapp.data


import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val result: List<MovieItem>? = null,
    @SerializedName("Error")
    val error: String? = null
)