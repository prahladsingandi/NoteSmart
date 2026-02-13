package dev.pranals.notesmart.data.repository

import dev.pranals.notesmart.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun observeNotes(): Flow<List<NoteEntity>>
    suspend fun getNote(id: String): NoteEntity?
    suspend fun saveNote(note: NoteEntity)
    suspend fun deleteNote(noteId: String)
}
