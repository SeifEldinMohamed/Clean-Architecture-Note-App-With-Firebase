package com.seif.cleanarchitecturenoteappwithfirebase.utils

interface OnItemClickRecyclerView<T> {
    fun onEditItemClick(item:T)
    fun onDeleteItemClick(item: T)
    fun onNoteItemClick(item: T)
}