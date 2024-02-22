package com.example.petfinder.common.domain.model

import com.google.gson.annotations.SerializedName

data class GetAnimalsResponse(
    @SerializedName("animals") val animals: List<Animal>,
    @SerializedName("pagination") val pagination: Pagination,
)
