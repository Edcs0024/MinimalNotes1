package com.example.minimalnotes.data

data class Note(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val content: String = "",
    val category: String = "General",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)