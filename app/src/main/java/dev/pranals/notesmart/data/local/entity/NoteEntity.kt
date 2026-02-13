package dev.pranals.notesmart.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    indices = [
        Index(value = ["lastModified"]),
        Index(value = ["syncState"])
    ]
)
data class NoteEntity(

    @PrimaryKey
    val id: String,

    val title: String,

    val content: String,

    val lastModified: Long,

    val createdAt: Long,

    val isDeleted: Boolean = false,

    val syncState: SyncState = SyncState.PENDING_UPLOAD,

    val color: Int = 0,

    val reminderTime: Long? = null
)
