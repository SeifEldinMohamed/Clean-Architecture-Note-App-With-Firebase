package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import android.net.Uri
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadSingleImageUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(fileUri: Uri): Flow<Resource<Uri,String>>{
        return noteRepository.uploadSingleImage(fileUri)
    }
}