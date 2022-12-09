package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseUser
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentAddNoteBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.utils.hide
import com.seif.cleanarchitecturenoteappwithfirebase.utils.show
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import com.seif.cleanarchitecturenoteappwithfirebase.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Date

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private val TAG = "AddNoteFragment"
    private lateinit var binding: FragmentAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    private var firebaseCurrentUser: FirebaseUser? = null
    var imageUris: MutableList<Uri> = arrayListOf()
    var objNote: Note? = null
    private val uploadedImagesAdapter by lazy { UploadedImagesAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseCurrentUser = addNoteViewModel.getFirebaseCurrentUser()

        observe()
        binding.btnAddNote.setOnClickListener {
            // addNoteViewModel.uploadMultipleImages(imageUris)
            val note = prepareNote()
            addNoteViewModel.addNote(note)
        }
        binding.rvUploadedImages.adapter = uploadedImagesAdapter

        binding.ivUploadImage.setOnClickListener {
            ImagePicker.with(this)
                // .crop()
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                imageUris.add(fileUri)
                uploadedImagesAdapter.updateImages(imageUris)
                binding.progressBarAdd.hide()
                binding.rvUploadedImages.show()
                binding.tvNoImagesYet.hide()
            }
            ImagePicker.RESULT_ERROR -> {
                binding.progressBarAdd.hide()
                toast(ImagePicker.getError(data))
            }
            else -> {
                binding.progressBarAdd.hide()
                Log.e(TAG, "Task Cancelled")
            }
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
                    is AddNoteFragmentState.ImageUploaded -> {
                        toast("image uri = ${state.uri}")
                        // when image uploaded successfully on storage then prepare note to upload it on fireStore
//                        val note = prepareNote()
//                        addNoteViewModel.addNote(note)
                    }
                    is AddNoteFragmentState.ImagesUploaded -> {
                        toast("images uri = ${state.imagesUri}")
                        // when image uploaded successfully on storage then prepare note to upload it on fireStore
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
        Log.d(TAG, "prepareNote: userId = ${firebaseCurrentUser?.uid}")
        return Note(
            id = "",
            userId = firebaseCurrentUser?.uid.toString(),
            title = title,
            description = description,
            date = date,
            images = imageUris
        )
    }
}
