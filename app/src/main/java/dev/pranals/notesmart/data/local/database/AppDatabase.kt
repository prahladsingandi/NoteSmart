package dev.pranals.notesmart.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.pranals.notesmart.data.local.dao.NoteDao
import dev.pranals.notesmart.data.local.entity.Converters
import dev.pranals.notesmart.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}
