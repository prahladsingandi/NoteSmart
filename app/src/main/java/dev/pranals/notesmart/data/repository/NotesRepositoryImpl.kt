package dev.pranals.notesmart.data.repository

import dev.pranals.notesmart.data.local.dao.NoteDao
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.data.local.entity.SyncState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NotesRepository {

    override fun observeNotes(): Flow<List<NoteEntity>> =
        noteDao.observeAllNotes()

    override suspend fun getNote(id: String): NoteEntity? =
        noteDao.getNoteById(id)

    override suspend fun saveNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(noteId: String) {
        noteDao.softDeleteNote(
            noteId,
            SyncState.PENDING_DELETE,
            System.currentTimeMillis()
        )
    }

}
