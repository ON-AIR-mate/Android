package umc.onairmate.ui.lounge.bookmark

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.LlItemTimelineTextBinding
import umc.onairmate.databinding.PopupBookmarkOptionsBinding
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.ui.util.NetworkImageLoader

class BookmarkViewHolder(
    private val binding: RvItemBookmarkBinding,
    private val bookmarkEventListener: BookmarkEventListener
) : RecyclerView.ViewHolder(binding.root) {

    private var isExpanded = false

    fun bind(bookmark: BookmarkData) {
        // 아이템 전체 클릭 리스너 - 방 생성해야함
        binding.root.setOnClickListener { bookmarkEventListener.createRoomWithBookmark(bookmark) }

        // 썸네일 이미지 로드
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, bookmark.videoThumbnail)

        binding.tvVideoTitle.text = bookmark.videoTitle
        // api에서 방 제목을 안넘겨주는 듯..???
        binding.tvRoomTitle.text = bookmark.bookmarkId.toString()
        if (bookmark.collectionTitle != null) {
            binding.tvCollection.text = bookmark.collectionTitle
            binding.tvCollection.visibility = View.VISIBLE
        }

        val inflater = LayoutInflater.from(itemView.context)

        // 북마크 타임라인 처리
        binding.llBookmarkText.removeAllViews()
        bookmark.message.forEachIndexed { index, message ->
            val itemBinding =
                LlItemTimelineTextBinding.inflate(inflater, binding.llBookmarkText, false)
            itemBinding.tvBookmarkTime.text = message

            if (index >= 2) itemBinding.root.visibility = View.GONE

            binding.llBookmarkText.addView(itemBinding.root)
        }

        // 펼치기/접기 클릭 이벤트 설정
        if (bookmark.message.size > 2) {
            binding.ivOpenBookmarks.visibility = View.VISIBLE
            binding.ivCloseBookmarks.visibility = View.GONE

            dropDownClickListener()
        } else {
            binding.ivOpenBookmarks.visibility = View.GONE
            binding.ivCloseBookmarks.visibility = View.GONE
        }

        // 더보기 버튼 클릭 리스너
        binding.btnMore.setOnClickListener {
            showPopupMenu(binding.btnMore, bookmark)
        }
    }

    private fun dropDownClickListener() {
        binding.ivOpenBookmarks.setOnClickListener {
            isExpanded = true
            for (i in 2 until binding.llBookmarkText.childCount) {
                binding.llBookmarkText.getChildAt(i).visibility = View.VISIBLE
            }
            binding.ivOpenBookmarks.visibility = View.GONE
            binding.ivCloseBookmarks.visibility = View.VISIBLE
        }
        binding.ivCloseBookmarks.setOnClickListener {
            isExpanded = false
            for (i in 2 until binding.llBookmarkText.childCount) {
                binding.llBookmarkText.getChildAt(i).visibility = View.GONE
            }
            binding.ivOpenBookmarks.visibility = View.VISIBLE
            binding.ivCloseBookmarks.visibility = View.GONE
        }
    }

    private fun showPopupMenu(anchorView: View, data: BookmarkData){
        val popupBinding = PopupBookmarkOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // popupBinding root 크기 측정 후 정렬 위치 계산
        popupBinding.root.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        val popupWidth = popupBinding.root.measuredWidth

        // 오른쪽 정렬: anchor 오른쪽 끝 기준
        val offsetX = -popupWidth + anchorView.width
        val offsetY = 0

        // 클릭 리스너 연결
        popupBinding.tvDeleteBookmark.setOnClickListener {
            bookmarkEventListener.deleteBookmark(data)
            popupWindow.dismiss()
        }
        popupBinding.tvMoveCollection.setOnClickListener {
            bookmarkEventListener.moveCollection(data)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
    }
}