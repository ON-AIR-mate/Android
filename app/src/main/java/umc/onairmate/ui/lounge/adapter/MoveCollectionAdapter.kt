package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.RvItemMoveCollectionBinding

class MoveCollectionAdapter(
    private val onSelect: (CollectionData) -> Unit
) : ListAdapter<CollectionData, MoveCollectionAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemMoveCollectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == selectedPosition)

        holder.itemView.setOnClickListener {
//            val newPos = holder.bindingAdapterPosition
//            if (newPos == RecyclerView.NO_POSITION) return@setOnClickListener
//
//            val previous = selectedPosition
//            if (previous != newPos) {
//                selectedPosition = newPos
//                if (previous != RecyclerView.NO_POSITION) {
//                    notifyItemChanged(previous)
//                }
//                notifyItemChanged(selectedPosition)
//                onSelect(getItem(newPos))
//            }
        }
    }

    inner class ViewHolder(private val binding: RvItemMoveCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(collection: CollectionData, isSelected: Boolean) {
            binding.tvCollectionTitle.text = collection.title
            binding.tvCount.text = collection.visibility
            binding.root.isSelected = isSelected
            binding.root.alpha = if (isSelected) 1f else 0.6f
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CollectionData>() {
        override fun areItemsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            return oldItem.title == newItem.title // 유니크하지 않다면 id 필드 추가 고려
        }

        override fun areContentsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            return oldItem == newItem
        }
    }
}
