package com.seif.cleanarchitecturenoteappwithfirebase.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun View.disable() {
    isEnabled = false
}

fun View.enabled() {
    isEnabled = true
}

fun Fragment.toast(msg: String?) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun Date.formatDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.getDefault())
    return formatter.format(this)
}

fun ChipGroup.addChip(
    text: String,
    isTouchTargeSize: Boolean = false,
    closeIconListener: View.OnClickListener? = null,
    root: ViewGroup
) {
    val chip: Chip = LayoutInflater.from(context).inflate(R.layout.item_chip, root, false) as Chip
    chip.text = if (text.length > 9) text.substring(0, 9) + "..." else text
    chip.isClickable = false
    chip.setEnsureMinTouchTargetSize(isTouchTargeSize)
    if (closeIconListener != null) {
        chip.closeIcon = ContextCompat.getDrawable(context, R.drawable.ic_cancel)
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener(closeIconListener)
    }
    addView(chip)
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}

fun Note.validNote(): Resource<String, String> {
    val note = this
    return if (note.title.length < 5) {
        Resource.Error("title is too short min char = 5")
    } else if (note.title.length > 40) {
        Resource.Error("title is too long max char = 40")
    } else if (note.description.length < 20) {
        Resource.Error("description is too short min char = 20")
    } else if (note.description.length > 200) { // to long ""
        Resource.Error("description is too long max char = 200")
    } else {
        Resource.Success("valid note")
    }
}

fun User.isValidUserInput(): Resource<String, String> {
    val user = this
    return if (user.username.isEmpty()) {
        Resource.Error("username can't be empty !")
    } else if (user.email.isEmpty()) {
        Resource.Error("email can't be empty !")
    } else if (!isValidEmail(user.email)) {
        Resource.Error("please enter a valid email !")
    } else if (user.password.isEmpty()) {
        Resource.Error("password can't be empty !")
    } else if (!isValidPasswordFormat(user.password)) {
        Resource.Error("please enter a valid password !")
    } else {
        Resource.Success("valid User")
    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPasswordFormat(password: String): Boolean {
    val passwordRegex = Pattern.compile(
        "^" +
            "(?=.*[0-9])" + // at least 1 digit
            // "(?=.*[a-z])" + // at least 1 lower case letter
            //  "(?=.*[A-Z])" + // at least 1 upper case letter
            //  "(?=.*[a-zA-Z])" +      // any letter
            //  "(?=.*[@#$%^&+=])" +    // at least 1 special character
            "(?=\\S+$)" + // no white spaces
            ".{6,}" + // at least 8 characters
            "$"
    )
    return passwordRegex.matcher(password).matches()
}
