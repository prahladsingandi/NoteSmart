package dev.pranals.notesmart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.data.local.entity.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // ---------- READ ----------

    @Query("""
        SELECT * FROM notes
        WHERE isDeleted = 0
        ORDER BY lastModified DESC
    """)
    fun observeAllNotes(): Flow<List<NoteEntity>>


    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): NoteEntity?


    // ---------- WRITE ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)


    @Update
    suspend fun updateNote(note: NoteEntity)


    // ---------- DELETE (Soft Delete) ----------

    @Query("""
        UPDATE notes
        SET isDeleted = 1,
            syncState = :syncState,
            lastModified = :time
        WHERE id = :noteId
    """)
    suspend fun softDeleteNote(
        noteId: String,
        syncState: SyncState,
        time: Long
    )


    // ---------- SYNC SUPPORT ----------

    @Query("""
        SELECT * FROM notes
        WHERE syncState != 'SYNCED'
    """)
    suspend fun getPendingSyncNotes(): List<NoteEntity>


    @Query("""
        UPDATE notes
        SET syncState = :state
        WHERE id = :noteId
    """)
    suspend fun updateSyncState(
        noteId: String,
        state: SyncState
    )


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(notes: List<NoteEntity>)
}
