package dev.pranals.notesmart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.data.local.entity.SyncState
import dev.pranals.notesmart.data.repository.NotesRepository
import dev.pranals.notesmart.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val notes = repository.observeNotes()
        .combine(searchQuery) { notes, query ->
            if (query.isBlank()) {
                notes
            } else {
                notes.filter { it.title.contains(query, true) || it.content.contains(query, true) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun searchNotes(query: String) {
        searchQuery.value = query
    }

    fun saveNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.saveNote(note)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun deleteNotes(noteIds: List<String>) {
        viewModelScope.launch {
            noteIds.forEach { repository.deleteNote(it) }
        }
    }

    suspend fun getNote(id: String): Note? {
        return repository.getNote(id)?.let { noteEntity ->
            Note(
                id = noteEntity.id,
                title = noteEntity.title,
                content = noteEntity.content,
                lastModified = noteEntity.lastModified.toString(),
                isSynced = noteEntity.syncState == SyncState.SYNCED,
                isDeleted = noteEntity.isDeleted,
                color = noteEntity.color,
                reminderTime = noteEntity.reminderTime
            )
        }
    }
}
