package com.example.petfinder.common.domain.model

import com.google.gson.annotations.SerializedName

data class GetAnimalByIdResponse(
    @SerializedName("animal") val animal: Animal,
)
