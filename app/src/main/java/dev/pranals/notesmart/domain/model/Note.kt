package dev.pranals.notesmart.domain.model
data class Note(val id: Int, val content: String,
val lastModified: String,
val isSynced: Boolean,
val isDeleted: Boolean)