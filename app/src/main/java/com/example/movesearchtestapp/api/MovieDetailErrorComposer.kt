package com.example.movesearchtestapp.api

import com.example.movesearchtestapp.data.MovieItemDetail
import com.example.movesearchtestapp.data.MovieSearchResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import java.lang.Exception


class MovieDetailErrorComposer : SingleTransformer<MovieItemDetail, MovieItemDetail> {

    override fun apply(upstream: Single<MovieItemDetail>): SingleSource<MovieItemDetail> {
        return upstream.doOnSuccess { response ->
            if (response == null || !response.error.isNullOrEmpty()) {
                throw Exception(response.error)
            }
        }
    }
}