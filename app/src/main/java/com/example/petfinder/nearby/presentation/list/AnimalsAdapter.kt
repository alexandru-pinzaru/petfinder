package com.example.petfinder.nearby.presentation.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinder.common.setPhotoUrl
import com.example.petfinder.databinding.RvAnimalItemBinding
import com.example.petfinder.details.DetailsActivity

class AnimalsAdapter(private val context: Context,) :
    ListAdapter<AnimalUiModel, AnimalsAdapter.AnimalsViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalsViewHolder {
        val binding = RvAnimalItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return AnimalsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalsViewHolder, position: Int) {
        val item: AnimalUiModel = getItem(position)
        holder.bind(item)
    }

    inner class AnimalsViewHolder(
        private val binding: RvAnimalItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnimalUiModel) {
            binding.name.text = item.name
            binding.photo.setPhotoUrl(item.smallestPhotoUrl)
            binding.root.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("animal", item)
                context.startActivity(intent)
            }
        }
    }
}

private val COMPARATOR = object : DiffUtil.ItemCallback<AnimalUiModel>() {
    override fun areItemsTheSame(oldItem: AnimalUiModel, newItem: AnimalUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AnimalUiModel, newItem: AnimalUiModel): Boolean {
        return oldItem == newItem
    }
}
