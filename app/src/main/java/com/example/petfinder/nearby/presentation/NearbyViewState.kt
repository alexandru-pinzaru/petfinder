package com.example.petfinder.nearby.presentation

import com.example.petfinder.nearby.presentation.list.AnimalUiModel

data class NearbyViewState(
    val animals: List<AnimalUiModel> = emptyList(),
    val loading: Boolean = true,
)
