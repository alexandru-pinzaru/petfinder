package com.example.petfinder.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.petfinder.R

fun ImageView.setPhotoUrl(url: String) {
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .error(R.drawable.ic_launcher_foreground)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}