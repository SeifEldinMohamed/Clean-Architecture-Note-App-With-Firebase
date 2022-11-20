package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteListBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list.adapter.NoteListAdapter
import com.seif.cleanarchitecturenoteappwithfirebase.utils.OnItemClickRecyclerView
import com.seif.cleanarchitecturenoteappwithfirebase.utils.SharedPrefs
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class NoteListFragment : Fragment(), OnItemClickRecyclerView<Note> {
    private val TAG = "NoteListFragment"
    private lateinit var binding: FragmentNoteListBinding
    private val noteListViewModel: NoteListViewModel by viewModels()
    private val noteListAdapter: NoteListAdapter by lazy { NoteListAdapter() }
    private var deletedNotePosition: Int? = null
    private var noteList: MutableList<Note> = arrayListOf()
    private var firebaseCurrentUser: FirebaseUser ? = null
    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseCurrentUser = noteListViewModel.getFirebaseCurrentUser()

        noteListAdapter.onItemClickRecyclerView = this
        if (sharedPrefs.get("firstTime", Boolean::class.java)) {
            firebaseCurrentUser?.let { currentUser ->
                noteListViewModel.getNotes(currentUser.uid) // we will make it at first time only
            }
            sharedPrefs.put("firstTime", false)
        }
        observe()
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }
        binding.swiptToRefresh.setOnRefreshListener {
            firebaseCurrentUser?.let { currentUser ->
                noteListViewModel.getNotes(currentUser.uid) // we will make it at first time only
            }
            binding.swiptToRefresh.isRefreshing = false
        }

        binding.fabLogout.setOnClickListener {
            noteListViewModel.logout()
        }
        // TODO: issue: when submitting new list the rv not scrolling to the first item
        // delete note
    }

    private fun observe() {
        initRecyclerView()
        observeState()
    }

    private fun initRecyclerView() {
        binding.rvNotes.apply {
            adapter = noteListAdapter
            itemAnimator = LandingAnimator()
        }
    }

    private fun observeState() {
        noteListViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                handleState(state)
            }.launchIn(lifecycleScope)
    }

    private fun handleState(state: NoteListFragmentState) {
        when (state) {
            NoteListFragmentState.Init -> Unit
            is NoteListFragmentState.IsLoading -> handleLoading(state.isLoading)
            is NoteListFragmentState.ShowError -> {
                Log.d(TAG, "handleState: Error: ${state.message}")
                binding.root.showSnackBar(state.message)
            }
            is NoteListFragmentState.ShowToast -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is NoteListFragmentState.Notes -> {
                Log.d(TAG, "handleState: notes: ${state.notes}")
                val notes = state.notes
                noteList = notes.toMutableList()
                if (notes.isNotEmpty()) {
                    noteListAdapter.submitList(notes)
                } else
                    Toast.makeText(requireContext(), "no notes yet!", Toast.LENGTH_SHORT).show()
            }
            is NoteListFragmentState.NoteDeleted -> {
                deletedNotePosition?.let {
                    binding.root.showSnackBar(state.message)
                    noteList.removeAt(it)
                    noteListAdapter.submitList(noteList)
                    deletedNotePosition = null
                }
            }
            is NoteListFragmentState.Logout -> {
                findNavController().navigate(R.id.action_noteListFragment_to_loginFragment)
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                // show loading progress circle
                Log.d(TAG, "handleLoading: Loading...")
                binding.progressBar.visibility = View.VISIBLE
            }
            false -> {
                // hide loading progress circle
                Log.d(TAG, "handleLoading: not loading")
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onEditItemClick(item: Note) {
        // go to details fragment to edit note
        val action =
            NoteListFragmentDirections.actionNoteListFragmentToNoteDetailsFragment(item, "edit")
        findNavController().navigate(action)
        Log.d(TAG, "onEditItemClick: edit clicked")
    }

    override fun onDeleteItemClick(item: Note, position: Int) {
        // delete note
        Log.d(TAG, "onDeleteItemClick: delete clicked")
        deletedNotePosition = position
        noteListViewModel.deleteNote(item)
    }

    override fun onNoteItemClick(item: Note) {
        // go to details fragment to see note details
        val action =
            NoteListFragmentDirections.actionNoteListFragmentToNoteDetailsFragment(item, "view")
        findNavController().navigate(action)
        Log.d(TAG, "onNoteItemClick: note clicked")
    }
}
