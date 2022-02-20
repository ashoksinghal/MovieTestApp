package com.example.movesearchtestapp.api

import com.example.movesearchtestapp.data.MovieSearchResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import java.lang.Exception


class ErrorComposer : SingleTransformer<MovieSearchResponse, MovieSearchResponse> {

    override fun apply(upstream: Single<MovieSearchResponse>): SingleSource<MovieSearchResponse> {
        return upstream.doOnSuccess { response ->
            if (response == null || !response.error.isNullOrEmpty()) {
                throw Exception(response.error)
            }
        }
    }
}