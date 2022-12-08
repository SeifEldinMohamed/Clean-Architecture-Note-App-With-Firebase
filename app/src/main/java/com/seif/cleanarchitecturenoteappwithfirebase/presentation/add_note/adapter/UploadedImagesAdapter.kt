package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ItemUploadedImageBinding

class UploadedImagesAdapter : RecyclerView.Adapter<UploadedImagesAdapter.MyViewHolder>() {
    var onImageItemClick: OnImageItemClick<Uri>? = null
    var images: List<Uri> = emptyList()
    inner class MyViewHolder(private val binding: ItemUploadedImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri, position: Int) {
            binding.ivNoteImage.setImageURI(image)
            Log.d("adapter", "bind: image uri $image")
            binding.ivRemoveImage.setOnClickListener {
                Log.d("adapter", "remove clicked")
                onImageItemClick?.onRemoveImageItemClick(image, position)
            }
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
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int {
        return images.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newImages: List<Uri>) {
        this.images = newImages
        notifyDataSetChanged()
    }
}