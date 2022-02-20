package com.example.movesearchtestapp.ui.main

import androidx.lifecycle.*
import com.example.movesearchtestapp.api.IMovieRepository
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.db.MoviesLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject internal constructor(
    private val movieRepository: IMovieRepository,
    private val moviesLocalRepository: MoviesLocalRepository
) : ViewModel() {

    private val _resultLiveData: MediatorLiveData<Result> = MediatorLiveData()
    val resultLiveData: LiveData<Result> = _resultLiveData
    private val favouriteMoviesLiveData: MutableLiveData<List<MovieItem>> = MutableLiveData()


    private val compositeDisposable = CompositeDisposable()

    init {
        _resultLiveData.addSource(favouriteMoviesLiveData) { favouriteMovieList ->
            val result = _resultLiveData.value
            val favMovieIds = favouriteMovieList?.map { it.imdbID }?.toSet()
            if (result is Result.Success) {
                result.result.forEach {
                    it.isFavourite = favMovieIds?.contains(it.imdbID) ?: false
                }
                _resultLiveData.value = result!!
            }
        }

        moviesLocalRepository.getMoviesList().subscribeOn(Schedulers.io())
            .subscribe({ response ->
                favouriteMoviesLiveData.postValue(response)
            }, { _ ->
                //Failed to get the items from local db
            }).also {
                compositeDisposable.add(it)
            }
    }


    fun searchMovies(query: String) {
        _resultLiveData.value = Result.Loader(true)
        movieRepository
            .getMovies(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val favouriteMovieIdsSet = favouriteMoviesLiveData.value?.map { it.imdbID }?.toSet()
                response.forEach {
                    it.isFavourite = favouriteMovieIdsSet?.contains(it.imdbID)
                }
                _resultLiveData.value = Result.Success(response)
            }, { error ->
                _resultLiveData.value = Result.Error(error.message ?: "")
            }).also {
                compositeDisposable.add(it)
            }
    }

    fun clearSearch() {
        _resultLiveData.value = Result.Success(emptyList())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun addRemoveFavourite(movie: MovieItem, onActionDone: () -> Unit) {
        if (movie.isFavourite == true) {
            moviesLocalRepository
                .markMovieAsFavourite(movie)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    onActionDone.invoke()
                }
                .also { compositeDisposable.addAll(it) }
        } else {
            moviesLocalRepository
                .removeMovieFromFavourite(movie.imdbID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    onActionDone.invoke()
                }
                .also { compositeDisposable.addAll(it) }
        }
    }

    sealed class Result {
        data class Error(val errorMsg: String) : Result()
        data class Success(val result: List<MovieItem>) : Result()
        data class Loader(val isLoading: Boolean) : Result()
    }


}
