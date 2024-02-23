package com.example.petfinder.nearby.presentation.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalUiModel(
    val id: Long,
    val name: String,
    val breed: String?,
    val size: String?,
    val gender: String?,
    val status: String?,
    val distance: Double?,
    val description: String?,
    val smallestPhotoUrl: String,
    val largestPhotoUrl: String,
): Parcelable
