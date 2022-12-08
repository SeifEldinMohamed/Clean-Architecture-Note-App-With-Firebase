package com.seif.cleanarchitecturenoteappwithfirebase.presentation.note_list.adapter

interface OnNoteItemClick<T> {
    fun onEditItemClick(item: T)
    fun onDeleteItemClick(item: T, position: Int)
    fun onNoteItemClick(item: T)
}
