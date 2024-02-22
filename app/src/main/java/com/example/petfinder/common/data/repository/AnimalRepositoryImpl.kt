package com.example.petfinder.common.data.repository

import android.text.format.DateUtils
import com.example.petfinder.common.data.cache.CacheManager
import com.example.petfinder.common.data.networking.PetfinderApi
import com.example.petfinder.common.domain.model.Animal
import com.example.petfinder.common.domain.repository.AnimalRepository
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(
    private val service: PetfinderApi,
    private val cacheManager: CacheManager,
) : AnimalRepository {

    private val ANIMALS_KEY = "ANIMALS"
    private val ANIMALS_EXPIRATION = 2 * DateUtils.MINUTE_IN_MILLIS
    private val ANIMALS_TYPE = object : TypeToken<ArrayList<Animal>>() {}.type

    // Mock data
    private val mock_location = "40.7128, 74.0060"// 10001
    private val mock_distance = 500
    private val mock_page = 1
    private val mock_limit = 100

    override fun getAnimals(
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ): Observable<List<Animal>> {
        return getAnimalsFromCacheUnexpired(
            mock_location,
            mock_distance,
            mock_page,
            mock_limit,
        )
            .switchIfEmpty(Observable.defer {
                fetchAnimals(
                    mock_location,
                    mock_distance,
                    mock_page,
                    mock_limit,
                )
            })
    }

    override fun getAnimalById(id: Int): Observable<Animal> {
        return service.getAnimalById(id).map { result -> result.animal }
    }

    private fun getAnimalsFromCacheUnexpired(
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ): Observable<List<Animal>> {
        return cacheManager.getListObservableUnexpired(
            getAnimalsCacheKey(
                location,
                distance,
                page,
                limit,
            ),
            ANIMALS_TYPE
        )
    }

    private fun fetchAnimals(
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ): Observable<List<Animal>> {
        return service.getAnimalsNearby(
            location,
            distance,
            page,
            limit,
        )
            .map { result -> result.animals }
            .doOnNext { data: List<Animal> ->
                onAnimalsUpdate(
                    data,
                    location,
                    distance,
                    page,
                    limit,
                )
            }
    }

    private fun onAnimalsUpdate(
        data: List<Animal>,
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ) {
        cacheManager.putObject(
            getAnimalsCacheKey(
                location,
                distance,
                page,
                limit,
            ),
            data,
            ANIMALS_EXPIRATION
        )
    }

    private fun getAnimalsCacheKey(
        location: String,
        distance: Int,
        page: Int,
        limit: Int,
    ): String {
        return ANIMALS_KEY +
                location +
                distance.toString() +
                page.toString() +
                limit.toString()
    }
}