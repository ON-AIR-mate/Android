package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.ItemCollectionBinding
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionItem
import umc.onairmate.R
import android.widget.PopupMenu



class CollectionAdapter(
    private val onDeleteClick: (CollectionData) -> Unit,
    private val onShareClick: (CollectionData) -> Unit,
    private val collectionList: List<CollectionData>,
    private val onMoreClick: (CollectionData) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    inner class CollectionViewHolder(private val binding: ItemCollectionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CollectionData) {
            binding.tvTitle.text = item.title
            binding.dateText.text = item.dateCreated
            binding.latestText.text = item.lastUpdated
            binding.openScopeText.text = item.privacy

            fun bind(item: CollectionItem) = with(binding) {
                val count = item.bookmarkCount
                tvCountBadge.text = if (count > 99) "99+" else count.toString()
                tvCountBadge.visibility = if (count > 0) View.VISIBLE else View.GONE
            }

            binding.moreButton.setOnClickListener {
                val popup = PopupMenu(binding.root.context, it)
                popup.menuInflater.inflate(R.menu.menu_collection_item, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_delete -> {
                            onDeleteClick(item)
                            true
                        }
                        R.id.menu_share -> {
                            onShareClick(item)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
        private fun showPopupMenu(anchor: View, item: CollectionData) {
            val popup = PopupMenu(anchor.context, anchor)
            popup.menuInflater.inflate(R.menu.menu_collection_item, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        onDeleteClick(item)
                        true
                    }
                    R.id.menu_share -> {
                        onShareClick(item)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(collectionList[position])
    }

    override fun getItemCount(): Int = collectionList.size
}
