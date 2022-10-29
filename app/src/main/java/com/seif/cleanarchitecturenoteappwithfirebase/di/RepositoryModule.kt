package com.seif.cleanarchitecturenoteappwithfirebase.di

import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.repository.NoteRepositoryImp
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
        firestore: FirebaseFirestore
    ): NoteRepository {
        return NoteRepositoryImp(firestore)
    }
}