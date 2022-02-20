package com.example.movesearchtestapp.di

import com.example.movesearchtestapp.api.IMovieRepository
import com.example.movesearchtestapp.api.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindNavigator(impl: MoviesRepository): IMovieRepository
}