package dev.pranals.notesmart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.data.repository.NotesRepository
import dev.pranals.notesmart.data.repository.NotesRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
//import javax.inject.Inject
//@HiltViewModel
class NotesViewModel(
    private val repository: NotesRepositoryImpl
) : ViewModel() {

    val notes: StateFlow<List<NoteEntity>> =
        repository.observeNotes()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    suspend fun getNote(id: String) =
        repository.getNote(id)

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
}
