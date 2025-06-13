package com.example.lyndenflood_comp304_midtermpractice

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Note(
    val title: String,
    val content: String,
    val priority: String
)

class NotesViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> get() = _notes

    fun addNote(note: Note) {
        _notes.add(note)
    }
}