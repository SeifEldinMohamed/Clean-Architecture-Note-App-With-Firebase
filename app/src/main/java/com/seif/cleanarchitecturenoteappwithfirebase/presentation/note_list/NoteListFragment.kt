package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private val TAG = "NoteListFragment"
    private lateinit var binding: FragmentNoteListBinding
    private val noteListViewModel: NoteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        observeState()
        observeNotes()
    }

    private fun observeState() {
        noteListViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun handleState(state: NoteListFragmentState) {
        when (state) {
            NoteListFragmentState.Init -> Unit
            is NoteListFragmentState.IsLoading -> handleLoading(state.isLoading)
            is NoteListFragmentState.ShowError -> {
                Log.d(TAG, "handleState: Error: ${state.message}")
            }
            is NoteListFragmentState.ShowToast -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                Log.d(TAG, "handleLoading: Loading...")
            } // show loading progress circle
            false -> {
                Log.d(TAG, "handleLoading: not loading")
            }// hide loading progress circle
        }
    }

    private fun observeNotes() {
        noteListViewModel.notes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach {
                Log.d(TAG, "observeNotes: data = $it")
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}