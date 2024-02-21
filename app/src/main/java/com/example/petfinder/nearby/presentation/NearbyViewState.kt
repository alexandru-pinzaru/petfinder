package com.example.petfinder.nearby.presentation

import com.example.petfinder.common.presentaion.AnimalUiModel

data class NearbyViewState(
    val animals: List<AnimalUiModel> = emptyList(),
)
