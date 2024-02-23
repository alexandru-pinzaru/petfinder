package com.example.petfinder.common.domain.model

import com.google.gson.annotations.SerializedName

data class Animal(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("breeds") val breeds: AnimalBreed?,
    @SerializedName("size") val size: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("description") val description: String?,
    @SerializedName("photos") val photos: List<AnimalPhoto>?,
)

data class AnimalBreed(
    @SerializedName("primary") val primary: String?,
    @SerializedName("secondary") val secondary: String?,
    @SerializedName("mixed") val mixed: Boolean?,
    @SerializedName("unknown") val unknown: Boolean?,
)

data class AnimalPhoto(
    @SerializedName("small") val small: String?,
    @SerializedName("medium") val medium: String?,
    @SerializedName("large") val large: String?,
    @SerializedName("full") val full: String?,
) {

    fun getSmallestAvailablePhoto(): String {
        return when {
            isValidPhoto(small) -> small!!
            isValidPhoto(medium) -> medium!!
            isValidPhoto(large) -> large!!
            isValidPhoto(full) -> full!!
            else -> ""
        }
    }

    fun getLargestAvailablePhoto(): String {
        return when {
            isValidPhoto(full) -> full!!
            isValidPhoto(large) -> large!!
            isValidPhoto(medium) -> medium!!
            isValidPhoto(small) -> small!!
            else -> ""
        }
    }

    private fun isValidPhoto(photo: String?): Boolean {
        return photo?.isNotEmpty() ?: false
    }
}