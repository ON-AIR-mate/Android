package umc.onairmate.ui.lounge.bookmark.move

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.RvItemMoveCollectionBinding

class CollectionMoveRVAdapter (
    private val collectionList: List<CollectionData>
) : RecyclerView.Adapter<CollectionMoveRVAdapter.CollectionViewHolder>() {

    private var selectedItemId: Int? = null

    inner class CollectionViewHolder(val binding: RvItemMoveCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collection: CollectionData) {
            binding.cbCheckbox.isSelected = collection.collectionId == selectedItemId
            binding.tvCount.text = collection.bookmarkCount.toString()
            binding.tvCollectionTitle.text = collection.title

            binding.cbCheckbox.setOnClickListener {
                val previousSelectedPosition = collectionList.indexOfFirst { collection.collectionId == selectedItemId }

                val currentState = it.isSelected
                it.isSelected = !currentState

                if (currentState) {
                    // isSelected == true 인데 눌렀음 -> false로 바꾸기
                    selectedItemId = collection.collectionId
                } else {
                    // isSelected == false 인데 눌렀음 -> true로 바꾸기
                    selectedItemId = null
                }

                // 이전에 선택되었던 아이템의 뷰를 갱신 (선택 해제)
                if (previousSelectedPosition != -1) {
                    notifyItemChanged(previousSelectedPosition)
                }
                // 새로 선택된 아이템의 뷰를 갱신
                notifyItemChanged(adapterPosition)
            }
        }
    }

    fun getSelectedItem(): Int? = selectedItemId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = RvItemMoveCollectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(collectionList[position])
    }

    override fun getItemCount(): Int = collectionList.size
}