package com.example.minimalnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minimalnotes.screens.HomeScreen
import com.example.minimalnotes.ui.theme.MinimalNotesTheme
import com.example.minimalnotes.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinimalNotesTheme {
                val notesViewModel: NotesViewModel = viewModel()
                HomeScreen(viewModel = notesViewModel)
            }
        }
    }
}