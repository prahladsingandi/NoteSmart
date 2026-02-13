package dev.pranals.notesmart.domain.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val lastModified: String,
    val isSynced: Boolean,
    val isDeleted: Boolean,
    val color: Int,
    val reminderTime: Long?
)
