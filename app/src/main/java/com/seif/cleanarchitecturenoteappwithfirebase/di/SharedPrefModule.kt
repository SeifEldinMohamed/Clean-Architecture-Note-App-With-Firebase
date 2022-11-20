package com.seif.cleanarchitecturenoteappwithfirebase.di

import android.content.Context
import android.content.SharedPreferences
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.SHARED_PREF_CONSTANT
import com.seif.cleanarchitecturenoteappwithfirebase.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_CONSTANT, Context.MODE_PRIVATE)
        // we use mode private bec we need to store new values in the place of old values
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPrefs {
        return SharedPrefs(context)
    }
}