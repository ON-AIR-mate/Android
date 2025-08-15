package umc.onairmate.ui.lounge.collection.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.entity.RoomArchiveData
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.databinding.RvItemCollectionDetailBinding
import umc.onairmate.databinding.RvItemCollectionDetailEmptyBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkViewHolder

class CollectionDetailRVAdapter(
    private val collectionDetailEventListener: CollectionDetailEventListener,
    private val bookmarkItemEventListener: BookmarkEventListener
): ListAdapter<CollectionDetailRVAdapter.RecyclerItem, RecyclerView.ViewHolder>(CollectionDetailDiffCallback) {

    // 뷰 타입 구분
    companion object {
        private const val VIEW_TYPE_COLLECTION_CARD = 0
        private const val VIEW_TYPE_BOOKMARK = 1
        private const val VIEW_TYPE_EMPTY = 2
    }

    sealed class RecyclerItem {
        // 뷰 타입 1: 컬렉션 정보 카드
        data class Card(val collectionDetailData: CollectionDetailData) : RecyclerItem()

        // 뷰 타입 2: 북마크 아이템
        data class Bookmark(val roomArchiveData: RoomArchiveData) : RecyclerItem()

        // 뷰 타입 3: 북마크가 비어있을 때 뷰
        object Empty : RecyclerItem()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerItem.Card -> VIEW_TYPE_COLLECTION_CARD
            is RecyclerItem.Bookmark -> VIEW_TYPE_BOOKMARK
            is RecyclerItem.Empty -> VIEW_TYPE_EMPTY
        }
    }

    inner class EmptyViewHolder(binding: RvItemCollectionDetailEmptyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_COLLECTION_CARD -> {
                val binding = RvItemCollectionDetailBinding.inflate(inflater, parent, false)
                CollectionDetailViewHolder(binding, collectionDetailEventListener)
            }
            VIEW_TYPE_BOOKMARK -> {
                val binding = RvItemBookmarkBinding.inflate(inflater, parent, false)
                BookmarkViewHolder(binding, bookmarkItemEventListener)
            }
            VIEW_TYPE_EMPTY -> {
                val binding = RvItemCollectionDetailEmptyBinding.inflate(inflater, parent, false)
                EmptyViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecyclerItem.Card -> (holder as CollectionDetailViewHolder).bind(item.collectionDetailData)
            is RecyclerItem.Bookmark -> (holder as BookmarkViewHolder).bind(item.roomArchiveData)
            is RecyclerItem.Empty -> (holder as EmptyViewHolder)
        }
    }

    // DisplayableItem을 비교하기 위한 DiffUtil.ItemCallback
    object CollectionDetailDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            // 아이템 타입이 다르면 무조건 다른 아이템
            if (oldItem::class != newItem::class) {
                return false
            }

            // 타입이 같을 경우, 각 타입의 고유 ID를 비교
            return when (oldItem) {
                is RecyclerItem.Card -> (newItem as RecyclerItem.Card).collectionDetailData.collectionId == oldItem.collectionDetailData.collectionId
                is RecyclerItem.Bookmark -> (newItem as RecyclerItem.Bookmark).roomArchiveData.roomData.roomId == oldItem.roomArchiveData.roomData.roomId
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            // data class의 '==' 연산자는 모든 프로퍼티를 비교하므로, 내용 비교에 적합
            return oldItem == newItem
        }
    }

    fun initData(
        collectionDetailData: CollectionDetailData,
    ) {
        val itemList = mutableListOf<RecyclerItem>()

        itemList.add(RecyclerItem.Card(collectionDetailData))
        if (collectionDetailData.bookmarks.isEmpty()) {
            itemList.add(RecyclerItem.Empty)
        } else {
            itemList.addAll(collectionDetailData.bookmarks.map { RecyclerItem.Bookmark(it) })
        }

        submitList(emptyList())
        submitList(itemList)
    }
}

