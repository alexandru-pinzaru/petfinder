package com.example.petfinder.common.data.networking

import com.example.petfinder.common.domain.model.GetAnimalByIdResponse
import com.example.petfinder.common.domain.model.GetAnimalsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PetfinderApi {

    @GET(Constants.GET_ANIMALS_URL)
    fun getAnimalsNearby(
        @Query(Params.LOCATION) location: String,
        @Query(Params.DISTANCE) distance: Int,
        @Query(Params.PAGE) page: Int,
        @Query(Params.LIMIT) limit: Int,
    ): Observable<GetAnimalsResponse>

    @GET(Constants.GET_ANIMALS_URL)
    fun getAnimalById(
        @Path(Params.ID) id: Int,
    ): Observable<GetAnimalByIdResponse>
}