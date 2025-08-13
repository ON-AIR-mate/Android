package umc.onairmate.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            binding.tvNickname.text = "[${user.nickname}]"
            binding.tvReason.text = user.reason
            binding.tvDate.text = user.date

            binding.btnUnblock.setOnClickListener {
                onUnblockClick(user)
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BlockedUser>() {
            override fun areItemsTheSame(oldItem: BlockedUser, newItem: BlockedUser) = oldItem.nickname == newItem.nickname
            override fun areContentsTheSame(oldItem: BlockedUser, newItem: BlockedUser) = oldItem == newItem
        }
    }
}