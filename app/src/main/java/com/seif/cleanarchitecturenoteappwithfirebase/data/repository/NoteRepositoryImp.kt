package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNote
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {
    override fun getNotes() = flow {
        var response: Resource<List<Note>, String>? = null
        firestore.collection(Constants.NOTES_COLLECTION)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<NoteDto>()
                for (document in it) {
                    val note = document.toObject(NoteDto::class.java)
                    notes.add(note)
                }
                response = Resource.Success(
                    notes.map {noteDto ->
                        noteDto.toNote()
                    }
                )
            }
            .addOnFailureListener {
                response = Resource.Error(it.message.toString())
            }
        response?.let {
            emit(it)
        }
    }

    override fun addNote(note: Note) = flow {
        var response: Resource<String, String>? = null
        firestore.collection(Constants.NOTES_COLLECTION)
            .add(note)
            .addOnSuccessListener {
                response = Resource.Success(it.id)
            }
            .addOnFailureListener {
                response = Resource.Error(it.message.toString())
            }
        response?.let {
            emit(it)
        }
    }
}
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