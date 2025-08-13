package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import android.view.View
import umc.onairmate.R
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.RvItemBookmarkBinding

interface OnBookmarkActionListener {
    fun onDeleteBookmark(bookmark: BookmarkData)
    fun onMoveBookmark(bookmark: BookmarkData)
}

class BookmarkAdapter(
    private val actionListener: OnBookmarkActionListener
) : ListAdapter<BookmarkData, BookmarkAdapter.BookmarkViewHolder>(DiffCallback()) {

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

    inner class BookmarkViewHolder(
        private val binding: RvItemBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: BookmarkData) = with(binding) {
            // 썸네일 (null/빈 문자열 대비)
            ivThumbnail.load(bookmark.videoThumbnail) {
                //placeholder(R.drawable.placeholder_image)
                //error(R.drawable.error_image)
            }

            // 컬렉션 배지 (없으면 숨김)
            val collection = bookmark.collectionTitle.orEmpty()
            tvCollection.text = collection
            tvCollection.visibility = if (collection.isBlank()) View.GONE else View.VISIBLE

            // 방 제목 (roomTitle이 없으면 빈 문자열)
            tvRoomTitle.text = bookmark.collectionTitle ?: ""

            // 영상 제목
            tvVideoTitle.text = bookmark.videoTitle ?: ""

            // 타임라인 (초 -> mm:ss)
            tvBookmarkTime.text = formatSeconds(bookmark.timeline)


            // 점 3개 메뉴
            btnMore.setOnClickListener { anchor ->
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

    private fun formatSeconds(totalSec: Int): String {
        val m = totalSec / 60
        val s = totalSec % 60
        return String.format("%d:%02d", m, s)
    }

    private class DiffCallback : DiffUtil.ItemCallback<BookmarkData>() {
        override fun areItemsTheSame(oldItem: BookmarkData, newItem: BookmarkData): Boolean {
            // 가능하면 고유 id 필드를 쓰세요 (예: oldItem.id == newItem.id)
            // 임시로 대표 필드 조합
            return oldItem.videoTitle == newItem.videoTitle &&
                    oldItem.createdAt  == newItem.createdAt &&
                    oldItem.timeline   == newItem.timeline
        }

        override fun areContentsTheSame(oldItem: BookmarkData, newItem: BookmarkData): Boolean {
            return oldItem == newItem
        }
    }
}
