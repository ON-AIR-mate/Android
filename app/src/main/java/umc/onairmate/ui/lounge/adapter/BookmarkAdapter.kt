package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.databinding.RvItemBookmarkBinding

interface OnBookmarkActionListener {
    fun onDeleteBookmark(bookmark: Bookmark)
    fun onMoveBookmark(bookmark: Bookmark)
}

class BookmarkAdapter(
    private val actionListener: OnBookmarkActionListener
) : ListAdapter<Bookmark, BookmarkAdapter.BookmarkViewHolder>(DiffCallback()) {

    inner class BookmarkViewHolder(private val binding: RvItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: Bookmark) {
            binding.tvVideoTitle.text = bookmark.title
            binding.tvBookmarkTime.text = bookmark.time

            binding.btnMore.setOnClickListener { anchor ->
                val popup = PopupMenu(anchor.context, anchor)
                popup.menuInflater.inflate(R.menu.menu_bookmark_more, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_delete -> {
                            actionListener.onDeleteBookmark(bookmark)
                            true
                        }
                        R.id.menu_move -> {
                            actionListener.onMoveBookmark(bookmark)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = RvItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem.thumbnailResId == newItem.thumbnailResId
        }

        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem == newItem
        }
    }
}
