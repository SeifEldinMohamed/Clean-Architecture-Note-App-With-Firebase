package com.seif.cleanarchitecturenoteappwithfirebase.presentation.add_note.adapter

interface OnImageItemClick<T> {
    fun onRemoveImageItemClick(item: T, position: Int)
}
