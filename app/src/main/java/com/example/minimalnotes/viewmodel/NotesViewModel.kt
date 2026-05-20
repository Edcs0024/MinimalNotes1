package com.example.minimalnotes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.minimalnotes.data.FirebaseNoteRepository
import com.example.minimalnotes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotesViewModel : ViewModel() {

    private val repository = FirebaseNoteRepository()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        loadNotes()
    }

    private fun loadNotes() {
        repository.listenNotes(
            onSuccess = { notesList ->
                _notes.value = notesList
            },
            onError = { exception ->
                _errorMessage.value = exception.message ?: "Error al cargar notas"
            }
        )
    }

    fun addNote(title: String, content: String, category: String) {
        if (title.isBlank() && content.isBlank()) return

        repository.addNote(
            title = title,
            content = content,
            category = category,
            onSuccess = {
                _errorMessage.value = ""
            },
            onError = { exception ->
                _errorMessage.value = exception.message ?: "Error al guardar nota"
            }
        )
    }

    fun deleteNote(note: Note) {
        repository.deleteNote(
            noteId = note.id,
            onSuccess = {
                _errorMessage.value = ""
            },
            onError = { exception ->
                _errorMessage.value = exception.message ?: "Error al eliminar nota"
            }
        )
    }

    fun toggleFavorite(note: Note) {
        repository.toggleFavorite(
            note = note,
            onSuccess = {
                _errorMessage.value = ""
            },
            onError = { exception ->
                _errorMessage.value = exception.message ?: "Error al actualizar favorito"
            }
        )
    }
}