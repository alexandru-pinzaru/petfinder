package com.example.petfinder.nearby.domain.usecase

import com.example.petfinder.common.domain.repository.AnimalRepository
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    // Mock data
    private val mock_location = "02108"
    private val mock_distance = 500
    private val mock_page = 1
    private val mock_limit = 100

    operator fun invoke() = animalRepository.getAnimals(
        mock_location,
        mock_distance,
        mock_page,
        mock_limit,
    )
}