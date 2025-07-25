package umc.OnAirMate.ui.friend.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import umc.OnAirMate.data.FriendData
import umc.OnAirMate.databinding.RvItemFriendBinding

class FriendViewHolder(
    private val binding: RvItemFriendBinding,
    private val context: Context,
    private val itemClick : FriendListRVAdapter.ItemClickListener
) :  RecyclerView.ViewHolder(binding.root) {
    fun bind(data : FriendData ){
        binding.tvUserNickname.text = data.nickname
        binding.ivOnline.visibility = if(data.isOnline) View.VISIBLE else View.GONE


        binding.ivMore.setOnClickListener {
            itemClick.clickMore()
        }
        binding.ivMessage.setOnClickListener {
            itemClick.clickMessage()
        }
    }
}