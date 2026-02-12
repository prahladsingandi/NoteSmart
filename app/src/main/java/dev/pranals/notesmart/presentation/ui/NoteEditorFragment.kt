package dev.pranals.notesmart.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pranals.notesmart.R
import dev.pranals.notesmart.data.local.entity.NoteEntity
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModel
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModelFactory
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var saveButton: Button

/**
 * A simple [Fragment] subclass.
 * Use the [NoteEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteEditorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private val viewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_editor, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        saveButton = view.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
         viewModel.saveNote(NoteEntity(
             1.toString(), "", 123.toString(), 23454321,
             createdAt = TODO(),
             isDeleted = TODO(),
             syncState = TODOxzzzzxz
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoteEditorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?) =
            NoteEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}