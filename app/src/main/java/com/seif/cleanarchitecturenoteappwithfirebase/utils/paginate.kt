package com.seif.cleanarchitecturenoteappwithfirebase.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Query.paginate(lastVisibleItem: Flow<Int>): Flow<List<DocumentSnapshot>> = flow {
    val documents = mutableListOf<DocumentSnapshot>()
    documents.addAll(
        suspendCoroutine { c ->
            this@paginate.limit(7).get().addOnSuccessListener { c.resume(it.documents) }
        }
    )
    emit(documents)
    lastVisibleItem.transform { lastVisible ->
        if (lastVisible == documents.size && documents.size > 0) {
            documents.addAll(
                suspendCoroutine { c ->
                    this@paginate.startAfter(documents.last())
                        .limit(7)
                        .get()
                        .addOnSuccessListener {
                           c.resume(it.documents) 
                        }
                }
            )
            emit(documents)
        }
    }.collect { docs ->
        emit(docs)
    }
}

// Here we immediately fetch the top 7 users and store them in a mutable list,
// then we map them to local entities and emit them. Further we listen to lastVisibleItem with a
// transform, and whenever it is updated we check if it is equal to the size of the list of documents
// we currently have, and if it is, we fetch the next 7 users by using startAfter with the last document
// we previously fetched. We then add the newly fetched documents to our list and emit the whole
// list of documents.