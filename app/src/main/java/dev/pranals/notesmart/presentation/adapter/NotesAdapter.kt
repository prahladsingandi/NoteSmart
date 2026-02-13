package dev.pranals.notesmart.presentation.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dev.pranals.notesmart.R
import dev.pranals.notesmart.data.local.entity.NoteEntity

class NotesAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<NoteEntity, NotesAdapter.NoteViewHolder>(Diff()) {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, tracker?.isSelected(position.toLong()) ?: false)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.noteTitleTextView)
        private val content: TextView = itemView.findViewById(R.id.noteContentTextView)
        private val card: MaterialCardView = itemView as MaterialCardView

        fun bind(note: NoteEntity, isSelected: Boolean) {
            title.text = note.title
            content.text = note.content
            card.isChecked = isSelected

            itemView.setOnClickListener {
                if (tracker?.hasSelection() == true) {
                    tracker?.select(itemId)
                } else {
                    onClick(note.id)
                }
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = bindingAdapterPosition
            override fun getSelectionKey(): Long? = itemId
            override fun inSelectionHotspot(e: MotionEvent): Boolean = true
        }
    }

    class Diff : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(a: NoteEntity, b: NoteEntity) = a.id == b.id
        override fun areContentsTheSame(a: NoteEntity, b: NoteEntity) = a == b
    }
}