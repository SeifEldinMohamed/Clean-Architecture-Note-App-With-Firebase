package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ItemNoteBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {
    private var notes: List<Note> = emptyList()
    class MyViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(note: Note) {
                binding.tvTitleNote.text = note.title
                binding.tvDescriptionNote.text = note.description
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

    fun addNotes(newNotes: List<Note>){
        this.notes = newNotes
        notifyDataSetChanged()
    }
}