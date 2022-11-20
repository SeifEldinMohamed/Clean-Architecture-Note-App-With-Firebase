package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentNoteDetailsBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.hide
import com.seif.cleanarchitecturenoteappwithfirebase.utils.show
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Date

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {
    private val TAG = "NoteDetailsFragment"
    lateinit var binding: FragmentNoteDetailsBinding
    private val noteDetailsViewModel: NoteDetailsViewModel by viewModels()
    private var firebaseCurrentUser: FirebaseUser? = null
    // save userId in shared preference and use it in whole app better than calling for id from firebase
    val args: NoteDetailsFragmentArgs by navArgs()
    private lateinit var note: Note
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseCurrentUser = noteDetailsViewModel.getFirebaseCurrentUser()

        updateUi()
        checkNoteUpdated()
        observe()

        binding.btnUpdateNote.setOnClickListener {
            Log.d(TAG, "onViewCreated: update button clicked !!")
            noteDetailsViewModel.updateNote(
                prepareNote()
            )
        }

        binding.etTitleDetails.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkNoteUpdated()
                Log.d(TAG, "title on text change: changing...$p0 $p1 $p2 $p3")
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etDescriptionDetails.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkNoteUpdated()
                Log.d(TAG, "description on text change: changing...$p0 $p1 $p2 $p3")
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observe() {
        noteDetailsViewModel.state.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.CREATED
        )
            .onEach { state ->
                when (state) {
                    DetailsNoteFragmentState.Init -> Unit
                    is DetailsNoteFragmentState.IsLoading -> handleLoadingState(state.isLoading)
                    is DetailsNoteFragmentState.ShowError -> binding.root.showSnackBar(state.message)
                    is DetailsNoteFragmentState.NoteId -> {
                        binding.root.showSnackBar("note updated successfully with same id ${state.noteId}")
                        //  findNavController().navigate(R.id.action_addNoteFragment_to_noteListFragment)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                Log.d(TAG, "handleLoadingState: Loading..")
                binding.progressBarDetails.show()
            } // show  progress bar
            false -> {
                Log.d(TAG, "handleLoadingState: Finish Loading !")
                binding.progressBarDetails.hide()
            } // hide progress bar
        }
    }

    private fun checkNoteUpdated() {
        val title = binding.etTitleDetails.text.toString()
        val description = binding.etDescriptionDetails.text.toString()
        Log.d(TAG, "checkNoteUpdated: title:$title, noteTitle ${note.title}")
        Log.d(
            TAG,
            "checkNoteUpdated: description:$description, noteDescripiotn ${note.description}"
        )

        binding.btnUpdateNote.isEnabled =
            !(title == note.title && description == note.description)
    }

    private fun prepareNote(): Note {
        val title = binding.etTitleDetails.text.toString()
        val description = binding.etDescriptionDetails.text.toString()
        val date = Date()
        return Note(
            id = note.id,
            userId = firebaseCurrentUser?.uid.toString(),
            title = title,
            description = description,
            date = date
        )
    }

    private fun updateUi() {
        Log.d(TAG, "updateUi: update ui....")

        note = args.note
        when (args.type) {
            "view" -> {
                binding.btnUpdateNote.hide()
            }
            "edit" -> {
                binding.btnUpdateNote.show()
            }
        }
        binding.etTitleDetails.setText(note.title)
        binding.etDescriptionDetails.setText(note.description)
    }
}
