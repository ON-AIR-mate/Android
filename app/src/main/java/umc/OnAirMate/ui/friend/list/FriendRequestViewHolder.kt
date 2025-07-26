package umc.OnAirMate.ui.friend.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import umc.OnAirMate.data.RequestedFriendData
import umc.OnAirMate.databinding.RvItemFriendRequestBinding

class FriendRequestViewHolder(
    private val binding: RvItemFriendRequestBinding,
    private val context: Context,
    private val itemClick : FriendListRVAdapter.ItemClickListener
) :  RecyclerView.ViewHolder(binding.root) {
    fun bind(data : RequestedFriendData ){
        binding.tvUserNickname.text = data.nickname
        binding.tvRequestedTime.text = data.requestedAt

        binding.root.setOnClickListener {
            itemClick.acceptRequest()
        }
    }
}