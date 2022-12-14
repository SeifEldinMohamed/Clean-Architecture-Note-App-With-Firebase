package com.seif.cleanarchitecturenoteappwithfirebase.data.mapper

import androidx.core.net.toUri
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.NoteDto
import com.seif.cleanarchitecturenoteappwithfirebase.data.remote.dto.UserDto
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.Note
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User

fun NoteDto.toNote(): Note {
    return Note(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        date = this.date,
        images = this.images.map { it.toUri() }
    )
}

fun Note.toNoteDto(): NoteDto {
    return NoteDto(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        date = this.date,
        images = this.images.map { it.toString() }
    )
}

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
        password = this.password,
        subscribed = this.subscribed,
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = this.id,
        username = this.username,
        email = this.email,
        password = this.password,
        subscribed = this.subscribed
    )
}
