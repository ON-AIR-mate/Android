package umc.onairmate.ui.friend.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.databinding.RvItemFriendRequestBinding
import umc.onairmate.ui.util.NetworkImageLoader

class FriendRequestViewHolder(
    private val binding: RvItemFriendRequestBinding,
    private val context: Context,
    private val itemClick : FriendListRVAdapter.ItemClickListener
) :  RecyclerView.ViewHolder(binding.root) {
    fun bind(data : RequestedFriendData ){
        binding.tvUserNickname.text = data.nickname
        binding.tvRequestedTime.text = data.requestedAt
        NetworkImageLoader.profileLoad(binding.ivProfile, data.profileImage)

        binding.root.setOnClickListener {
            itemClick.acceptRequest(data)
        }
    }
}