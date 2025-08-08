package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.ItemCollectionListBinding
<<<<<<< HEAD
import umc.onairmate.data.model.entity.CollectionData
=======
>>>>>>> aaae94ffd241e218d1f84548269bbe5a3530c144

class CollectionListAdapter(
    private val items: List<CollectionData>,
    private val onItemClick: (CollectionData) -> Unit
) : RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder>() {

    inner class CollectionViewHolder(val binding: ItemCollectionListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionData) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = item.dateCreated
            binding.ivThumbnail.load(item.thumbnailUrl)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
