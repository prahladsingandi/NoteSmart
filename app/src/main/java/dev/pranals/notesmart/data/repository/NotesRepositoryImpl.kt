package dev.pranals.notesmart.data.repository

import android.content.Context
import dev.pranals.notesmart.data.local.dao.NoteDao
import dev.pranals.notesmart.data.local.database.AppDatabaseProvider
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.data.local.entity.SyncState
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(context: Context) {

    private val noteDao: NoteDao =
        AppDatabaseProvider.getDatabase(context).noteDao()

    fun observeNotes(): Flow<List<NoteEntity>> =
        noteDao.observeAllNotes()

    suspend fun getNote(id: String): NoteEntity? =
        noteDao.getNoteById(id)

    suspend fun saveNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(noteId: String) {
        noteDao.softDeleteNote(
            noteId,
            SyncState.PENDING_DELETE,
            System.currentTimeMillis()
        )
    }
}
