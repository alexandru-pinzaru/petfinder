package com.example.petfinder.common.domain.repository

import com.example.petfinder.common.domain.model.Animal
import io.reactivex.rxjava3.core.Observable


interface AnimalRepository {

    fun getAnimals(
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ): Observable<List<Animal>>

    fun getAnimalById(id: Int): Observable<Animal>
}