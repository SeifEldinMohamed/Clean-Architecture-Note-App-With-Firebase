package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteDetailsBinding

class NoteDetailsFragment : Fragment() {
    private val TAG = "NoteDetailsFragment"
    lateinit var binding: FragmentNoteDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}