package umc.onairmate.ui.lounge.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.LlItemTimelineTextBinding
import umc.onairmate.databinding.PopupBookmarkOptionsBinding
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.databinding.RvItemBookmarkHeaderBinding
import umc.onairmate.ui.util.NetworkImageLoader

class BookmarkRVAdapter(
    private val bookmarkEventListener: BookmarkEventListener
) : ListAdapter<BookmarkRVAdapter.RecyclerItem, RecyclerView.ViewHolder>(BookmarkDiffCallback) {

    // 뷰 타입 구분
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BOOKMARK = 1

        private const val UNCATEGORIZED_BOOKMARK = "정리되지 않은 북마크"
        private const val ALL_BOOKMARK = "모든 북마크"
    }

    sealed class RecyclerItem {
        // 뷰 타입 1: 헤더
        data class Header(val title: String) : RecyclerItem()

        // 뷰 타입 2: 북마크 아이템
        data class Bookmark(val bookmarkData: BookmarkData) : RecyclerItem()
    }

    // 리스트의 각 아이템 종류에 따라 뷰 타입을 반환
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerItem.Header -> VIEW_TYPE_HEADER
            is RecyclerItem.Bookmark -> VIEW_TYPE_BOOKMARK
        }
    }

    inner class BookmarkViewHolder(
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

    // 헤더를 위한 뷰홀더
    class HeaderViewHolder(private val binding: RvItemBookmarkHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: RecyclerItem.Header) {
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
                BookmarkViewHolder(binding, bookmarkEventListener) // 클릭 리스너 전달
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // 뷰홀더의 종류에 따라 데이터를 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecyclerItem.Header -> (holder as HeaderViewHolder).bind(item)
            is RecyclerItem.Bookmark -> (holder as BookmarkViewHolder).bind(item.bookmarkData)
        }
    }

    // DisplayableItem을 비교하기 위한 DiffUtil.ItemCallback
    object BookmarkDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            // 아이템 타입이 다르면 무조건 다른 아이템
            if (oldItem::class != newItem::class) {
                return false
            }

            // 타입이 같을 경우, 각 타입의 고유 ID를 비교
            return when (oldItem) {
                is RecyclerItem.Header -> (newItem as RecyclerItem.Header).title == oldItem.title
                is RecyclerItem.Bookmark -> (newItem as RecyclerItem.Bookmark).bookmarkData.bookmarkId == oldItem.bookmarkData.bookmarkId
            }
        }

        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            // data class의 '==' 연산자는 모든 프로퍼티를 비교하므로, 내용 비교에 적합
            return oldItem == newItem
        }
    }

    // 데이터 삽입 -> 각각의 리스트가 비어 있는 경우에 따라 다른 ITEM 삽입
    fun initData(
        uncategorizedBookmarks: List<BookmarkData>,
        allBookmarks: List<BookmarkData>
    ) {
        val itemList = mutableListOf<RecyclerItem>()

        if (uncategorizedBookmarks.isNotEmpty()) {
            itemList.add(RecyclerItem.Header(UNCATEGORIZED_BOOKMARK)) // 정리되지 않은 북마크 헤더
            itemList.addAll(uncategorizedBookmarks.map { RecyclerItem.Bookmark(it) }) // 정리안된 북마크 아이템
        }

        if (allBookmarks.isNotEmpty()) {
            itemList.add(RecyclerItem.Header(ALL_BOOKMARK)) // 모든 북마크 헤더
            itemList.addAll(allBookmarks.map { RecyclerItem.Bookmark(it) }) // 모든 북마크 아이템
        }

        submitList(emptyList()) // 기존 데이터 초기화 -> Empty리스트가 들어올 경우 대비
        submitList(itemList)
    }
}

