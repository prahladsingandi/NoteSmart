package dev.pranals.notesmart.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dev.pranals.notesmart.R
import dev.pranals.notesmart.presentation.adapter.NotesAdapter
import dev.pranals.notesmart.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesListFragment : Fragment(R.layout.fragment_notes_list) {

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private var actionMode: ActionMode? = null

    private val adapter = NotesAdapter { noteId ->
        openNoteDetail(noteId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.notesRecyclerView)
        emptyView = view.findViewById(R.id.emptyView)
        fab = view.findViewById(R.id.addNoteFab)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        setupMenu()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val tracker = SelectionTracker.Builder(
            "note-selection",
            recyclerView,
            NoteKeyProvider(adapter),
            NoteDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (tracker.hasSelection()) {
                    if (actionMode == null) {
                        actionMode = (activity as AppCompatActivity).startSupportActionMode(ActionModeCallback())
                    }
                    actionMode?.title = "${tracker.selection.size()} selected"
                } else {
                    actionMode?.finish()
                    actionMode = null
                }
            }
        })

        fab.setOnClickListener {
            openEditor(null)
        }

        observeNotes()
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collect { notes ->
                    adapter.submitList(notes)
                    emptyView.visibility =
                        if (notes.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_notes_list, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.searchNotes(newText.orEmpty())
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

    private class NoteKeyProvider(private val adapter: NotesAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
        override fun getKey(position: Int): Long? = adapter.getItemId(position)
        override fun getPosition(key: Long): Int = key.toInt()
    }

    private class NoteDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as NotesAdapter.NoteViewHolder).getItemDetails()
            }
            return null
        }
    }

    private inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_note_selection, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> {
                    val selectedIds = mutableListOf<String>()
                    adapter.tracker?.selection?.forEach { key ->
                        val position = key.toInt()
                        if (position >= 0 && position < adapter.currentList.size) {
                            selectedIds.add(adapter.currentList[position].id)
                        }
                    }
                    viewModel.deleteNotes(selectedIds)
                    mode?.finish()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            adapter.tracker?.clearSelection()
            actionMode = null
        }
    }
}
