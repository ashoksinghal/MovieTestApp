package com.example.movesearchtestapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.data.MovieItemDetail
import com.example.movesearchtestapp.db.MoviesLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class FavouriteViewModel @Inject internal constructor(
    private val moviesLocalRepository: MoviesLocalRepository
) : ViewModel() {

    private val _resultLiveData: MutableLiveData<Result> = MutableLiveData()
    val resultLiveData: LiveData<Result> = _resultLiveData

    private val compositeDisposable = CompositeDisposable()

    init {

        moviesLocalRepository.getMoviesList().subscribeOn(Schedulers.io())
            .subscribe({ response ->
                _resultLiveData.postValue(Result.Success(response))
            }, { error ->
                _resultLiveData.postValue(Result.Success(emptyList()))
            }).also {
                compositeDisposable.add(it)
            }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    sealed class Result {
        data class Error(val errorMsg: String) : Result()
        data class Success(val result: List<MovieItem>) : Result()
        data class Loader(val isLoading: Boolean) : Result()
    }

}
