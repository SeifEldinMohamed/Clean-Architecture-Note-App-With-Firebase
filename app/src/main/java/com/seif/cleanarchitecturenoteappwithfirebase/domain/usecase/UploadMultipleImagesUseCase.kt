package com.seif.cleanarchitecturenoteappwithfirebase.domain.usecase

import android.net.Uri
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import javax.inject.Inject

class UploadMultipleImagesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(imagesUri: List<Uri>): Resource<List<Uri>, String> {
        return if (imagesUri.isNotEmpty())
            noteRepository.uploadMultipleImages(imagesUri)
        else
            Resource.Error("You have to upload at least one image.")
    }
}