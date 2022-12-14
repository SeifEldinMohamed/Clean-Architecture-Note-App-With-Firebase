package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNote
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.USER_ID
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference
) : NoteRepository {
    override fun getNotes(userId: String) = callbackFlow<Resource<List<Note>, String>> {
// whenever we use order by and whereEqualTo, we need to create index from firestore ( exception will give us link to direct us to firestore and make index
        Log.d("Note Repository", "getNotes: user id = $userId")
        firestore.collection(Constants.NOTES_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<NoteDto>()
                for (document in it) {
                    val note = document.toObject(NoteDto::class.java)
                    notes.add(note)
                }
                Log.d("NoteRepository", "getNotes: $notes")
                trySend(
                    Resource.Success(
                        notes.map { noteDto ->
                            noteDto.toNote()
                        }
                    )
                )
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose {}
    }

    override suspend fun addNote(note: Note): Resource<String, String> {
        return when (val result = uploadMultipleImages(note.images)) {
            is Resource.Error -> {
                Resource.Error(result.message)
            }
            is Resource.Success -> {
                try {
                    val document = firestore.collection(Constants.NOTES_COLLECTION).document()
                    note.id = document.id
                    document.set(note.toNoteDto()).await()
                    Resource.Success("Note Added Successfully with id : ${document.id}")
                } catch (e: Exception) {
                    Resource.Error(e.message.toString())
                }
            }
        }
    }

    override fun updateNote(note: Note) = callbackFlow<Resource<String, String>> {
        val document = firestore.collection(Constants.NOTES_COLLECTION).document(note.id)
        document.set(note.toNoteDto())
            .addOnSuccessListener {
                trySend(Resource.Success("Note Updated Successfully with same id : ${document.id}"))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose {}
    }

    override fun deleteNote(note: Note) = callbackFlow<Resource<String, String>> {
        val document = firestore.collection(Constants.NOTES_COLLECTION).document(note.id)
        document.delete()
            .addOnSuccessListener {
                trySend(Resource.Success("Note Deleted Successfully with id : ${document.id}"))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose {}
    }

    override suspend fun uploadSingleImage(imageUri: Uri) = flow {
        try {
            val uri = withContext(Dispatchers.IO) {
                storageReference.putFile(imageUri)
                    .await() // will upload image and then wait unitl it uploaded to firebase storage
                    .storage.downloadUrl.await() // then we download what we are getting from storage we downloaded it
            }
            emit(Resource.Success(uri))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun uploadMultipleImages(imagesUri: List<Uri>): Resource<List<Uri>, String> {
        return try {
            val uri: List<Uri> = withContext(Dispatchers.IO) {
                // 1,2,3,4
                // 4 async blocks (upload first then download it's url then upload second ....)
                imagesUri.map { imageUri -> // we will map the whole list into the async blocks
                    async {
                        storageReference.child(
                            imageUri.lastPathSegment ?: "${System.currentTimeMillis()}"
                        )
                            .putFile(imageUri)
                            .await()
                            .storage
                            .downloadUrl
                            .await()
                    }
                }.awaitAll()
            }
            Resource.Success(uri)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}

//        firestore.collection(Constants.NOTES_COLLECTION)
//            .get()
//            .addOnSuccessListener {
//                val notes = arrayListOf<NoteDto>()
//                for (document in it) {
//                    val note = document.toObject(NoteDto::class.java)
//                    notes.add(note)
//                }
//                // Log.d("TAG", "getNotes: $notes")
//                trySend(
//                    Resource.Success(
//                        notes.map { noteDto ->
//                            noteDto.toNote()
//                        }
//                    )
//                )
//            }
//            .addOnFailureListener {
//                trySend(Resource.Error(it.message.toString()))
//            }

//        val data = listOf(
//            NoteDto("1", "first note", "first note description", Date().toString()),
//            NoteDto("2", "second note", "second note description", Date().toString()),
//            NoteDto("3", "third note", "third note description", Date().toString())
//        )
//        if (data.isEmpty()) {
//            emit(Resource.Error("Data is Empty"))
//        } else {
//            if (data.isNotEmpty())
//                emit(Resource.Success(data.map { it.toNote() }))
//        }

/** Snapshot query listener**/
/*
//        firestore.collection(Constants.NOTES_COLLECTION)
//            .orderBy("date", Query.Direction.DESCENDING)
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    Log.d("NoteRepositoryImp", "Listen failed: $error")
//                    return@addSnapshotListener
//                }
//                val notes = arrayListOf<NoteDto>()
//                if (value != null) {
//                    for (document in value) {
//                        val note = document.toObject(NoteDto::class.java)
//                        notes.add(note)
//                    }
//                    Log.d("Note", "retrieved notes ${notes.size} = $notes")
//                    trySend(
//                        Resource.Success(
//                            notes.map { noteDto ->
//                                noteDto.toNote()
//                            }
//                        )
//                    )
//                }
//            }
//
//        awaitClose {}
*/
