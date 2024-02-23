package com.example.petfinder.nearby.presentation

import com.example.petfinder.common.domain.model.Animal
import com.example.petfinder.nearby.presentation.list.AnimalUiModel
import javax.inject.Inject

class AnimalUiMapper @Inject constructor() {

    fun toUiModels(input: List<Animal>): List<AnimalUiModel> {
        return input.map { animal ->
            AnimalUiModel(
                id = animal.id,
                name = animal.name,
                breed = animal.breeds?.primary,
                size = animal.size,
                gender = animal.gender,
                status = animal.status,
                distance = animal.distance,
                description = animal.description,
                smallestPhotoUrl = animal.photos?.firstOrNull()?.getSmallestAvailablePhoto() ?: "",
                largestPhotoUrl = animal.photos?.firstOrNull()?.getLargestAvailablePhoto() ?: "",
            )
        }
    }
}
