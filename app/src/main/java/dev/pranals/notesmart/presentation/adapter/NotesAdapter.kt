package dev.pranals.notesmart.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pranals.notesmart.R
import dev.pranals.notesmart.data.local.entity.NoteEntity

class NotesAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<NoteEntity, NotesAdapter.NoteViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val title = itemView.findViewById<TextView>(R.id.titleText)
        private val content = itemView.findViewById<TextView>(R.id.contentPreviewText)

        fun bind(note: NoteEntity) {
            title.text = note.title
            content.text = note.content
            itemView.setOnClickListener { onClick(note.id) }
        }
    }

    class Diff : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(a: NoteEntity, b: NoteEntity) = a.id == b.id
        override fun areContentsTheSame(a: NoteEntity, b: NoteEntity) = a == b
    }
}
