package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toUser
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toUserDto
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.UserDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.USER_FireStore_Collection
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.USER_SESSION
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : AuthRepository {
    private val TAG = "AuthRepositoryImp"

    override fun registerUser(user: User) = callbackFlow<Resource<User, String>> {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                it.user?.let { firebaseUser ->
                    user.id = firebaseUser.uid
                }
                addUser(user) { result ->
                    when (result) {
                        is Resource.Success -> {
                            storeSession(userId = it.user?.uid ?: "") { userDto ->
                                if (userDto == null) {
                                    trySend(Resource.Error("Registered Successfully but session failed to store"))
                                } else {
                                    trySend(Resource.Success(user))
                                    Log.d(TAG, "register: Registered Successfully $it")
                                }
                            }
                        }
                        is Resource.Error -> {
                            trySend(Resource.Error(result.message))
                        }
                    }
                }
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
                Log.d(TAG, "register: Registered Successfully $it")
            }
        awaitClose {}
    }

    private fun addUser(user: User, result: (Resource<String, String>) -> Unit) {
        val document = firestore.collection(USER_FireStore_Collection).document(user.id)
        document
            .set(user.toUserDto())
            .addOnSuccessListener {
                //   trySend(Resource.Success(""))
                result(Resource.Success("User Added Successfully"))
            }
            .addOnFailureListener {
                //    trySend(Resource.Error(it.message.toString()))
                result(Resource.Success(it.message.toString()))
            }
        // awaitClose {}
    }

    override fun loginInUser(email: String, password: String) =
        callbackFlow<Resource<String, String>> {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    storeSession(userId = it.user?.uid ?: "") { userDto ->
                        if (userDto == null) {
                            trySend(Resource.Error("login but failed to store session"))
                        } else {
                            trySend(Resource.Success(it.user?.uid.toString()))
                            Log.d(TAG, "login: Login Successfully ${it.user}")
                        }
                    }
                }
                .addOnFailureListener {
                    trySend(Resource.Error(it.message.toString()))
                    Log.d(TAG, "login: login Failed ${it.localizedMessage}")
                }
            awaitClose {}
        }

    override fun forgetPassword(email: String) = callbackFlow {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                trySend(Resource.Success("email send successfully"))
                Log.d(TAG, "forget password: send email to reset password successfully")
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message.toString()))
                Log.d(TAG, "forget password:failed to send email to reset password")
            }
        awaitClose {}
    }

    override suspend fun logOut(): Resource<String, String> {
        auth.signOut()
        sharedPreferences.edit().putString(USER_SESSION, null).apply()
        return Resource.Success("Logged Out")
    }

    override fun getSession(): User? {
        val userString = sharedPreferences.getString(USER_SESSION, null)
        return if (userString == null) {
            null
        } else {
            val userDto = gson.fromJson(userString, UserDto::class.java)
            userDto.toUser()
        }
    }

    private fun storeSession(userId: String, result: (UserDto?) -> Unit) {
        firestore.collection(USER_FireStore_Collection).document(userId)
            .get()
            .addOnSuccessListener {
                val userDto = it.toObject(UserDto::class.java)
                sharedPreferences.edit().putString(USER_SESSION, gson.toJson(userDto)).apply()
                result.invoke(userDto)
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }
}