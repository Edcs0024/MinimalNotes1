package com.example.minimalnotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimalnotes.data.Note
import com.example.minimalnotes.data.NoteDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val noteDataStore = NoteDataStore(application)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        viewModelScope.launch {
            noteDataStore.notesFlow.collect { savedNotes ->
                _notes.value = savedNotes
            }
        }
    }

    fun addNote(title: String, content: String, category: String) {
        if (title.isBlank() && content.isBlank()) return

        val newNote = Note(
            title = title,
            content = content,
            category = if (category.isBlank()) "General" else category
        )

        val updatedNotes = _notes.value + newNote
        saveNotes(updatedNotes)
    }

    fun deleteNote(note: Note) {
        val updatedNotes = _notes.value.filter { it.id != note.id }
        saveNotes(updatedNotes)
    }

    fun toggleFavorite(note: Note) {
        val updatedNotes = _notes.value.map {
            if (it.id == note.id) {
                it.copy(isFavorite = !it.isFavorite)
            } else {
                it
            }
        }

        saveNotes(updatedNotes)
    }

    private fun saveNotes(notes: List<Note>) {
        viewModelScope.launch {
            noteDataStore.saveNotes(notes)
        }
    }
}