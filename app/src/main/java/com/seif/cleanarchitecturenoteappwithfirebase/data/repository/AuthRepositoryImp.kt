package com.seif.cleanarchitecturenoteappwithfirebase.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.seif.cleanarchitecturenoteappwithfirebase.data.mapper.toUserDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.domain.repository.AuthRepository
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Constants.Companion.USER_FireStore_Collection
import com.seif.cleanarchitecturenoteappwithfirebase.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
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
                            trySend(Resource.Success(user))
                            Log.d(TAG, "register: Registered Successfully $it")
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
                result(Resource.Success("User Added Successfully"))
            }
            .addOnFailureListener {
                result(Resource.Success(it.message.toString()))
            }
    }

    override fun loginInUser(email: String, password: String) =
        callbackFlow<Resource<String, String>> {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    trySend(Resource.Success(it.user?.uid.toString()))
                    Log.d(TAG, "login: Login Successfully ${it.user}")
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
        return Resource.Success("Logged Out")
    }

    override fun getFirebaseCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}