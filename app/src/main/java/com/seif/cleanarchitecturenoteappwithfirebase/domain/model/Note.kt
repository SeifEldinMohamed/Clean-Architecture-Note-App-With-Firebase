package com.seif.cleanarchitecturenoteappwithfirebase.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
data class Note(
    var id:String,
    val title:String,
    val description: String,
    val date:Date
): Parcelable
