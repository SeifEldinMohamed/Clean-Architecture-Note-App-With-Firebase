package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private val TAG = "NoteListFragment"
    private lateinit var binding: FragmentNoteListBinding
    private val noteListViewModel : NoteListViewModel by viewModels()

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