package umc.onairmate.ui.lounge.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.RvItemCollectionBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkRVAdapter.RecyclerItem

class CollectionRVAdapter(
    private val collectionEventListener: CollectionEventListener
) : ListAdapter<CollectionData, CollectionRVAdapter.CollectionViewHolder>(CollectionDiffCallback) {

    inner class CollectionViewHolder(private val binding: RvItemCollectionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CollectionData) {
            binding.root.setOnClickListener { collectionEventListener.clickCollectionItem(item) }

            binding.tvTitle.text = item.title
            binding.tvGeneratedDate.text = "생성일 : ${item.createdAt}"
            binding.tvLatestModifiedDate.text = "마지막 수정일 : ${item.updatedAt}"
            binding.tvPrivacy.text = item.visibility

            binding.ivMore.setOnClickListener {
                val popup = PopupMenu(binding.root.context, it)
                popup.menuInflater.inflate(R.menu.menu_collection_item, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_delete -> {
                            collectionEventListener.deleteCollection(item)
                            true
                        }
                        R.id.menu_share -> {
                            collectionEventListener.shareCollection(item)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = RvItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object CollectionDiffCallback : DiffUtil.ItemCallback<CollectionData>() {
        override fun areItemsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            // data class의 '==' 연산자는 모든 프로퍼티를 비교하므로, 내용 비교에 적합
            return oldItem == newItem
        }
    }
}

interface CollectionEventListener {
    fun deleteCollection(collection: CollectionData) {}
    fun shareCollection(collection: CollectionData) {}
    fun clickCollectionItem(collection: CollectionData) {}
}