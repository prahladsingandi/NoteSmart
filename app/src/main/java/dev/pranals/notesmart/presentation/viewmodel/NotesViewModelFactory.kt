package dev.pranals.notesmart.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.pranals.notesmart.data.repository.NotesRepositoryImpl

class NotesViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(
                NotesRepositoryImpl(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
