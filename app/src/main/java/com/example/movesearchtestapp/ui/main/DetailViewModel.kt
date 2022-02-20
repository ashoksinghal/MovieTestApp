package com.example.movesearchtestapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movesearchtestapp.api.MoviesRepository
import com.example.movesearchtestapp.data.MovieItemDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject internal constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _resultLiveData: MutableLiveData<Result> = MutableLiveData()
    val resultLiveData: LiveData<Result> = _resultLiveData

    private val compositeDisposable = CompositeDisposable()

     fun getMovieDetail(movieId: String) {
        _resultLiveData.postValue(Result.Loader(true))
        moviesRepository.getMovieDetail(movieId = movieId).subscribeOn(Schedulers.io())
            .subscribe({ response ->
                _resultLiveData.postValue(Result.Success(response))
            }, { error ->
                _resultLiveData.postValue(Result.Error(errorMsg = error.message ?: ""))
            }).also {
                compositeDisposable.add(it)
            }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


    fun initialiseMovieItemDetail(movieItemDetail: MovieItemDetail?) {
        movieItemDetail?.let {
            _resultLiveData.postValue(DetailViewModel.Result.Success(it))
            getMovieDetail(movieItemDetail.imdbID ?: "")
        }

    }

    sealed class Result {
        data class Error(val errorMsg: String) : Result()
        data class Success(val result: MovieItemDetail) : Result()
        data class Loader(val isLoading: Boolean) : Result()
    }

}
