package umc.onairmate.ui.lounge.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.databinding.RvItemBookmarkHeaderBinding
import umc.onairmate.ui.util.NetworkImageLoader

class BookmarkRVAdapter(
    private val bookmarkList: List<DisplayableItem>,
    private val itemClick: (BookmarkData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 뷰 타입 구분
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BOOKMARK = 1
    }

    // 리스트의 각 아이템 종류에 따라 뷰 타입을 반환
    override fun getItemViewType(position: Int): Int {
        return when (bookmarkList[position]) {
            is DisplayableItem.Header -> VIEW_TYPE_HEADER
            is DisplayableItem.Bookmark -> VIEW_TYPE_BOOKMARK
        }
    }

    inner class BookmarkViewHolder(
        private val binding: RvItemBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: BookmarkData) {
            // 썸네일 이미지 로드
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, bookmark.videoThumbnail)

            binding.tvVideoTitle.text = bookmark.videoTitle
            // api에서 방 제목을 안넘겨주는 듯..???
            binding.tvRoomTitle.text = bookmark.bookmarkId.toString()
            binding.tvBookmarkTime.text = bookmark.message

            binding.btnMore.setOnClickListener { button ->
                val popup = PopupMenu(button.context, button)
                popup.menuInflater.inflate(R.menu.bookmark_popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_delete_bookmark -> {
                            // TODO: 삭제 기능 호출
                            Toast.makeText(button.context, "북마크 삭제 클릭됨", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    // 헤더를 위한 뷰홀더
    class HeaderViewHolder(private val binding: RvItemBookmarkHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: DisplayableItem.Header) {
            binding.tvHeaderTitle.text = header.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = RvItemBookmarkHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_BOOKMARK -> {
                val binding = RvItemBookmarkBinding.inflate(inflater, parent, false)
                BookmarkViewHolder(binding, itemClick) // 클릭 리스너 전달
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val binding = RvItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
    }


    override fun getItemCount(): Int = bookmarkList.size
}

sealed class DisplayableItem {
    // 뷰 타입 1: 헤더
    data class Header(val title: String) : DisplayableItem()

    // 뷰 타입 2: 북마크 아이템
    data class Bookmark(val bookmarkData: BookmarkData) : DisplayableItem()
}