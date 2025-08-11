package umc.onairmate.ui.lounge.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.RvItemCollectionBinding

class CollectionRVAdapter(
    private val collectionList: List<CollectionData>,
    private val collectionEventListener: CollectionEventListener
) : RecyclerView.Adapter<CollectionRVAdapter.CollectionViewHolder>() {

    inner class CollectionViewHolder(private val binding: RvItemCollectionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CollectionData) {
            binding.tvTitle.text = item.title
            binding.tvGeneratedDate.text = item.dateCreated
            binding.tvLatestModifiedDate.text = item.lastUpdated
            binding.tvPrivacy.text = item.privacy

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
        holder.bind(collectionList[position])
    }

    override fun getItemCount(): Int = collectionList.size
}

interface CollectionEventListener {
    fun deleteCollection(collection: CollectionData) {}
    fun shareCollection(collection: CollectionData) {}
}