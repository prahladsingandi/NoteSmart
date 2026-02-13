package dev.pranals.notesmart.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dev.pranals.notesmart.R
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var titleTv: TextView
    private lateinit var contentTv: TextView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var editFab: FloatingActionButton

    private lateinit var noteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = requireArguments().getString(ARG_NOTE_ID)!!
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        titleTv = view.findViewById(R.id.detailTitle)
        contentTv = view.findViewById(R.id.detailContent)
        editFab = view.findViewById(R.id.editNoteFab)

        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        editFab.setOnClickListener { openEditor() }

        loadNote()
    }

    private fun loadNote() {
        lifecycleScope.launch {
            viewModel.getNote(noteId)?.let {
                toolbar.title = it.title
                titleTv.text = it.title
                contentTv.text = it.content
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            viewModel.deleteNote(noteId)
            parentFragmentManager.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openEditor() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, NoteEditorFragment.newInstance(noteId))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_NOTE_ID = "note_id"

        fun newInstance(noteId: String) =
            NoteDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NOTE_ID, noteId)
                }
            }
    }
}
