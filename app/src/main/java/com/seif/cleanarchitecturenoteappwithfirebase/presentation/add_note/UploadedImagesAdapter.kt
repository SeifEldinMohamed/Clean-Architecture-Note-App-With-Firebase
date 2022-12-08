package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ItemUploadedImageBinding

class UploadedImagesAdapter : RecyclerView.Adapter<UploadedImagesAdapter.MyViewHolder>() {
    private var images = emptyList<Uri>()
    class MyViewHolder(private val binding: ItemUploadedImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri) {
            binding.ivNoteImage.setImageURI(image)
            Log.d("adapter", "bind: image uri $image")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemUploadedImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.size

    fun updateImages(newImages: List<Uri>) {
        this.images = newImages
        notifyDataSetChanged()
    }
}