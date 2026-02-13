package dev.pranals.notesmart.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import dev.pranals.notesmart.R
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class NoteEditorFragment : Fragment(R.layout.fragment_note_editor) {

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var titleEt: TextInputEditText
    private lateinit var contentEt: TextInputEditText
    private lateinit var toolbar: MaterialToolbar
    private lateinit var saveFab: FloatingActionButton

    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = arguments?.getString(ARG_NOTE_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        titleEt = view.findViewById(R.id.titleEditText)
        contentEt = view.findViewById(R.id.contentEditText)
        saveFab = view.findViewById(R.id.saveNoteFab)

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        saveFab.setOnClickListener {
            saveNote()
            parentFragmentManager.popBackStack()
        }

        loadNoteIfEditing()
    }

    private fun loadNoteIfEditing() {
        noteId?.let { id ->
            lifecycleScope.launch {
                viewModel.getNote(id)?.let { note ->
//                    titleEt.setText(note.title)
                    contentEt.setText(note.content)
                }
            }
        }
    }

    private fun saveNote() {
        val title = titleEt.text.toString().trim()
        val content = contentEt.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) return

        val note = NoteEntity(
            id = noteId ?: UUID.randomUUID().toString(),
            title = title,
            content = content,
            createdAt = System.currentTimeMillis(),
            lastModified = System.currentTimeMillis()
        )

        viewModel.saveNote(note)
    }

    companion object {
        private const val ARG_NOTE_ID = "note_id"

        fun newInstance(noteId: String?) =
            NoteEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NOTE_ID, noteId)
                }
            }
    }
}
