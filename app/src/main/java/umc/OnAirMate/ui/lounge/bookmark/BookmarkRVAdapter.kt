package umc.onairmate.ui.lounge.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.ui.util.NetworkImageLoader

class BookmarkRVAdapter(
    private val bookmarkList: List<BookmarkData>,
    private val itemClick: (BookmarkData) -> Unit
) : RecyclerView.Adapter<BookmarkRVAdapter.BookmarkViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = RvItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
    }


    override fun getItemCount(): Int = bookmarkList.size
}