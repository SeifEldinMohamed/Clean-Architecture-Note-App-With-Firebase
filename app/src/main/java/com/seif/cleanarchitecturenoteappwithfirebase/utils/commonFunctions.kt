package com.seif.cleanarchitecturenoteappwithfirebase.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Note.validNote(): Resource<String, String> {
    val note = this
    return if (note.title.length < 5) {
        Resource.Error("title is too short min char = 5")
    } else if (note.title.length > 40) {
        Resource.Error("title is to0 long max char = 40")
    } else if (note.description.length < 20) {
        Resource.Error("description is too short min char = 20")
    } else if (note.description.length > 200) { // to long ""
        Resource.Error("description is too long max char = 200")
    } else {
        Resource.Success("valid note")
    }
}
