package com.seif.cleanarchitecturenoteappwithfirebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.seif.cleanarchitecturenoteappwithfirebase.data.repository.AuthRepositoryImp
import com.seif.cleanarchitecturenoteappwithfirebase.data.repository.NoteRepositoryImp
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        firestore: FirebaseFirestore,
        storageReference: StorageReference
    ): NoteRepository {
        return NoteRepositoryImp(firestore, storageReference)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImp(firestore, auth)
    }
}
