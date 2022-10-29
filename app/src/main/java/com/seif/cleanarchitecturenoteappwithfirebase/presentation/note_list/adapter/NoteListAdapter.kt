package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ItemNoteBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.MyDiffUtil

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {
     private var notes: List<Note> = emptyList()

    class MyViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvTitleNote.text = note.title
            binding.tvDescriptionNote.text = note.description
            // binding.tvDate.text = note.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(notes[position])
    }

       override fun getItemCount() = notes.size

    companion object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    fun addNotes(newNotes: List<Note>) {
        val diffUtilCallback = MyDiffUtil<Note>(this.notes, newNotes)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        this.notes = newNotes
        result.dispatchUpdatesTo(this)
    }
}