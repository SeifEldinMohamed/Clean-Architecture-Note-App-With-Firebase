package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toNote
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class NoteRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : NoteRepository {
    override fun getNotes() = flow {
        val data = listOf(
            NoteDto("1", "first note", "first note description", Date().toString()),
            NoteDto("2", "second note", "second note description", Date().toString()),
            NoteDto("3", "third note", "third note description", Date().toString())
        )
        if (data.isEmpty()) {
            emit(Resource.Error("Data is Empty"))
        } else {
            if (data.isNotEmpty())
                emit(Resource.Success(data.map { it.toNote() }))
        }
    }
}