package com.example.minimalnotes.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirebaseNoteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val notesCollection = db.collection("notes")

    fun listenNotes(
        onSuccess: (List<Note>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        notesCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val notes = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Note::class.java)?.copy(id = document.id)
                } ?: emptyList()

                onSuccess(notes)
            }
    }

    fun addNote(
        title: String,
        content: String,
        category: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val note = hashMapOf(
            "title" to title,
            "content" to content,
            "category" to if (category.isBlank()) "General" else category,
            "isFavorite" to false,
            "createdAt" to System.currentTimeMillis()
        )

        notesCollection
            .add(note)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun deleteNote(
        noteId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        notesCollection
            .document(noteId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }

    fun toggleFavorite(
        note: Note,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        notesCollection
            .document(note.id)
            .update("isFavorite", !note.isFavorite)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }
}