package com.seif.cleanarchitecturenoteappwithfirebase.data.repository


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNote
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {
    override fun getNotes() = callbackFlow<Resource<List<Note>, String>> {
        firestore.collection(Constants.NOTES_COLLECTION)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("NoteRepositoryImp", "Listen failed: $error")
                    return@addSnapshotListener
                }
                val notes = arrayListOf<NoteDto>()
                if (value != null) {
                    for (document in value) {
                        val note = document.toObject(NoteDto::class.java)
                        notes.add(note)
                    }
                    trySend(
                        Resource.Success(
                            notes.map { noteDto ->
                                noteDto.toNote()
                            }
                        )
                    )
                }
            }

        awaitClose {}

    }

    override fun addNote(note: Note) = callbackFlow<Resource<String, String>> {
        firestore.collection(Constants.NOTES_COLLECTION)
            .add(note.toNoteDto())
            .addOnSuccessListener {
                trySend(Resource.Success("Note Added Successfully with id : ${it.id}"))
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


