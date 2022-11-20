package com.seif.cleanarchitecturenoteappwithfirebase.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
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
        firestore: FirebaseFirestore
    ): NoteRepository {
        return NoteRepositoryImp(firestore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImp(firestore, auth, sharedPreferences, gson)
    }
}
