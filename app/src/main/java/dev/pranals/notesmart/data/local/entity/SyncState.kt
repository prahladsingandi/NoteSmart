package dev.pranals.notesmart.data.local.entity

import androidx.room.TypeConverter

enum class SyncState {
    SYNCED,
    PENDING_UPLOAD,
    PENDING_DELETE,
    FAILED
}

class Converters {

    @TypeConverter
    fun fromSyncState(value: SyncState): String = value.name

    @TypeConverter
    fun toSyncState(value: String): SyncState = SyncState.valueOf(value)
}
