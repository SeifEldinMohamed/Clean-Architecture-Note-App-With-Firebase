package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {
    private val TAG = "NoteListFragment"
    private lateinit var binding: FragmentNoteListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}