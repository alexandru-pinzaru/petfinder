package com.example.petfinder.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petfinder.common.setPhotoUrl
import com.example.petfinder.databinding.ActivityDetailsBinding
import com.example.petfinder.nearby.presentation.list.AnimalUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var animalUiModel: AnimalUiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animalUiModel = intent.getParcelableExtra("animal")!!
    }

    override fun onResume() {
        super.onResume()
        binding.run {
            animalName.text = "Name: ${animalUiModel.name}"
            breed.text = "Breed: ${animalUiModel.breed}"
            size.text = "Size: ${animalUiModel.size}"
            gender.text = "Gender: ${animalUiModel.gender}"
            status.text = "Status: ${animalUiModel.status}"
            distance.text = "Distance: ${animalUiModel.distance?.roundToInt()} miles"
            photo.setPhotoUrl(animalUiModel.largestPhotoUrl)
        }
    }
}