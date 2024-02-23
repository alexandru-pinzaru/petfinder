package com.example.petfinder.common.di

import com.example.petfinder.common.data.repository.AnimalRepositoryImpl
import com.example.petfinder.common.domain.repository.AnimalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MainActivityModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAnimalRepository(repository: AnimalRepositoryImpl): AnimalRepository

    companion object {
        @Provides
        fun provideCompositeDisposable() = CompositeDisposable()
    }
}