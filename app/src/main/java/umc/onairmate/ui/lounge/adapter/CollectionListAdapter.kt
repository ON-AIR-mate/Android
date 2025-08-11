package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.ItemBookmarkBinding


class CollectionListAdapter(
    private val items: List<CollectionData>,
    private val onItemClick: (CollectionData) -> Unit
) : RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder>() {

    inner class CollectionViewHolder(val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionData) {
            binding.titleText.text = item.title //titleText아닐수도..
            binding.timeText.text = item.dateCreated
            binding.thumbnailImage.load(item.thumbnailUrl)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
