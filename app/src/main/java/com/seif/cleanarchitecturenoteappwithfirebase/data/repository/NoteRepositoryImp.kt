package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNote
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.USER_ID
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
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

    override fun addNote(note: Note) = callbackFlow<Resource<String, String>> {
        val document = firestore.collection(Constants.NOTES_COLLECTION).document()
        note.id = document.id
        document.set(note.toNoteDto())
            .addOnSuccessListener {
                trySend(Resource.Success("Note Added Successfully with id : ${document.id}"))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
            }
        awaitClose {}
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
