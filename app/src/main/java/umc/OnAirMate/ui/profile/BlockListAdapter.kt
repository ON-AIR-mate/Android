package umc.onairmate.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.ItemBlockedUserBinding

data class BlockedUser(val nickname: String, val reason: String, val date: String)

class BlockListAdapter(
    private val onUnblockClick: (BlockedUser) -> Unit
) : ListAdapter<BlockedUser, BlockListAdapter.BlockViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = ItemBlockedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BlockViewHolder(private val binding: ItemBlockedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: BlockedUser) {
            // 데이터 바인딩
            binding.tvNickname.text = "[${user.nickname}]"
            binding.tvReason.text = "[${user.reason}]"
            binding.tvDate.text = "[${user.date}]"

            // btnMore 클릭 시 팝업 메뉴 표시
            binding.btnMore.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menuInflater.inflate(R.menu.menu_block_option, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menuUnblock -> {
                            onUnblockClick(user) // 콜백 호출
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BlockedUser>() {
            override fun areItemsTheSame(oldItem: BlockedUser, newItem: BlockedUser) =
                oldItem.nickname == newItem.nickname

            override fun areContentsTheSame(oldItem: BlockedUser, newItem: BlockedUser) =
                oldItem == newItem
        }
    }
}