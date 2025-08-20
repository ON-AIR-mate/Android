package umc.onairmate.ui.lounge.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.DialogPersonalShareListBinding

class PersonalShareListAdapter : ListAdapter<FriendData, PersonalShareListAdapter.VH>(DiffCallback()) {

    // 선택된 친구들의 ID를 저장하는 Set
    private val selectedFriendIds = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = DialogPersonalShareListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    // 외부에서 선택된 친구들의 ID 목록을 가져올 때 사용
    fun getSelectedFriendIds(): List<Int> {
        return selectedFriendIds.toList()
    }

    inner class VH(private val binding: DialogPersonalShareListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendData) {
            with(binding) {
                // 친구 이름 설정
                tvFriendName.text = item.nickname

                // 선택 상태에 따라 UI 업데이트
                // isSelected 속성을 사용하여 선택 상태를 시각적으로 표시할 수 있습니다.
                root.isSelected = selectedFriendIds.contains(item.userId)

                // 클릭 리스너 설정
                root.setOnClickListener {
                    if (selectedFriendIds.contains(item.userId)) {
                        selectedFriendIds.remove(item.userId)
                    } else {
                        selectedFriendIds.add(item.userId)
                    }
                    // 뷰의 상태를 즉시 갱신
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<FriendData>() {
        override fun areItemsTheSame(oldItem: FriendData, newItem: FriendData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FriendData, newItem: FriendData): Boolean {
            return oldItem == newItem
        }
    }
}