package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteListBinding
import com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list.adapter.NoteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NoteListFragment : Fragment() {
    private val TAG = "NoteListFragment"
    private lateinit var binding: FragmentNoteListBinding
    private val noteListViewModel: NoteListViewModel by viewModels()
    private val noteListAdapter: NoteListAdapter by lazy { NoteListAdapter() }

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
        setProgressBarAccordingToLoadState()
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }
    }

    private fun observe() {
        initRecyclerView()
        observeState()
    }

    private fun initRecyclerView() {
        binding.rvNotes.apply {
            adapter = noteListAdapter
        }
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
        lifecycleScope.launch {
            when (state) {
                NoteListFragmentState.Init -> Unit
                is NoteListFragmentState.IsLoading -> handleLoading(state.isLoading)
                is NoteListFragmentState.ShowError -> {
                    Log.d(TAG, "handleState: Error: ${state.message}")
                }
                is NoteListFragmentState.ShowToast -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is NoteListFragmentState.Notes -> {
                    val notes = state.notes
                    //  if (notes.isNotEmpty())
                    notes.flow.collect{
                        Log.d(TAG, "handleState: $it")
                        noteListAdapter.submitData(it)
                    }
                    //   else
                    //       Toast.makeText(requireContext(), "no notes yet!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun setProgressBarAccordingToLoadState() {
        lifecycleScope.launch {
            noteListAdapter.loadStateFlow.collectLatest {
                Log.d(TAG, "setProgressBarAccordingToLoadState: ${LoadState.Loading}")
                Log.d(TAG, "----------------------------------------------------------------------")
                Log.d(TAG, "setProgressBarAccordingToLoadState:append -> ${it.append}")
                binding.progressBar.isVisible = it.append is LoadState.Loading
                binding.view.isVisible = it.append is LoadState.Loading
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                // show loading progress circle
                Log.d(TAG, "handleLoading: Loading...")
            }
            false -> {
                // hide loading progress circle
                Log.d(TAG, "handleLoading: not loading")
            }
        }
    }

}