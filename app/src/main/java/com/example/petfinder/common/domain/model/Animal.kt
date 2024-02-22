package com.example.petfinder.common.domain.model

import com.google.gson.annotations.SerializedName

data class Animal(
    @SerializedName("id") val id: Long,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("distance") val distance: Double,
)
