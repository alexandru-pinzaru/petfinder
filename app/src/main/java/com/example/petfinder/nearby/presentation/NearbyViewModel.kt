package com.example.petfinder.nearby.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petfinder.common.applySchedulers
import com.example.petfinder.common.data.networking.NetworkException
import com.example.petfinder.nearby.domain.usecase.GetAnimalsUseCase
import com.example.petfinder.nearby.presentation.events.ErrorEvent
import com.example.petfinder.nearby.presentation.events.NearbyEvent
import com.example.petfinder.nearby.presentation.list.AnimalUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val getAnimals: GetAnimalsUseCase,
    private val uiAnimalMapper: AnimalUiMapper,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _state = MutableLiveData<NearbyViewState>()
    val state: LiveData<NearbyViewState> get() = _state

    init {
        _state.value = NearbyViewState()
    }

    fun onEvent(event: NearbyEvent) {
        when (event) {
            is NearbyEvent.RequestAnimalsList -> loadData()
        }
    }

    private fun loadData() {
        getAnimals()
            .observeOn(AndroidSchedulers.mainThread())
            .applySchedulers()
            .map(uiAnimalMapper::toUiModels)
            .subscribe(
                { onDataSetRetrieved(it) },
                { onError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun onDataSetRetrieved(newDataSet: List<AnimalUiModel>) {
        _state.value = state.value!!.copy(
            loading = false,
            animals = newDataSet
        )
    }

    private fun onError(error: Throwable) {
        when (error) {
            is NetworkException -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    error = ErrorEvent(error)
                )
            }
            else -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    error = ErrorEvent(error)
                )
            }
        }
    }
}