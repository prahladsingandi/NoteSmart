package dev.pranals.notesmart.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import dagger.hilt.android.AndroidEntryPoint
import dev.pranals.notesmart.R
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.presentation.adapter.NotesAdapter
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModel
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModelFactory
import kotlinx.coroutines.launch
import java.util.UUID

//@AndroidEntryPoint
class NotesListFragment : Fragment(R.layout.fragment_notes_list) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fab: FloatingActionButton

    private val adapter = NotesAdapter { noteId ->
        openNoteDetail(noteId)
    }


    private val viewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.notesRecyclerView)
        emptyView = view.findViewById(R.id.emptyView)
        fab = view.findViewById(R.id.addNoteFab)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fab.setOnClickListener {
            openEditor(null)
        }

        observeNotes()
    }

    private fun observeNotes() {
        lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                adapter.submitList(notes)
                emptyView.visibility =
                    if (notes.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun openEditor(noteId: String?) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, NoteEditorFragment.newInstance(noteId))
            .addToBackStack(null)
            .commit()
    }

    private fun openNoteDetail(noteId: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, NoteDetailFragment.newInstance(noteId))
            .addToBackStack(null)
            .commit()
    }
}
