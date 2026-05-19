package com.example.minimalnotes.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.noteDataStore by preferencesDataStore(name = "notes_datastore")

class NoteDataStore(
    private val context: Context
) {
    private val gson = Gson()

    companion object {
        private val NOTES_KEY = stringPreferencesKey("notes_list")
    }

    val notesFlow: Flow<List<Note>> = context.noteDataStore.data.map { preferences ->
        val json = preferences[NOTES_KEY] ?: "[]"
        val type = object : TypeToken<List<Note>>() {}.type

        try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)

        context.noteDataStore.edit { preferences ->
            preferences[NOTES_KEY] = json
        }
    }
}