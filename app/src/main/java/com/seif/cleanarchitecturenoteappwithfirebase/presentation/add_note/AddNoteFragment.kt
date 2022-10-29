package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentAddNoteBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private val TAG = "AddNoteFragment"
    lateinit var binding: FragmentAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        binding.btnAddNote.setOnClickListener {
            val note = prepareNote()
            addNoteViewModel.addNote(note)
        }

    }

    private fun observe() {
        observeState()
    }


    private fun observeState() {
        addNoteViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    AddNoteFragmentState.Init -> Unit
                    is AddNoteFragmentState.IsLoading -> handleLoadingState(state.isLoading)
                    is AddNoteFragmentState.ShowError -> binding.root.showSnackBar(state.message)
                    is AddNoteFragmentState.NoteId -> {
                        binding.root.showSnackBar("note created with id: ${state.noteId}")
                      //  findNavController().navigate(R.id.action_addNoteFragment_to_noteListFragment)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                Log.d(TAG, "handleLoadingState: Loading..")
                binding.progressBarAdd.visibility = View.VISIBLE
            } // show loading progress
            false -> {
                Log.d(TAG, "handleLoadingState: Finish Loading !")
                binding.progressBarAdd.visibility = View.GONE
            } // hide loading progress
        }
    }

    private fun prepareNote(): Note {
        val title = binding.etTitleDetails.text.toString()
        val description = binding.etDescriptionDetails.text.toString()
        val date = Date()
        return Note(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            date = date,
        )
    }

}