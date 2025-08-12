package umc.onairmate.ui.lounge.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.RvItemCollectionShareFriendBinding
import umc.onairmate.ui.util.NetworkImageLoader


class CollectionSendRVAdapter(
    private val friendList: List<FriendData>,
    private val onSendClick: (FriendData) -> Unit
) : RecyclerView.Adapter<CollectionSendRVAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(val binding: RvItemCollectionShareFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: FriendData) {
            NetworkImageLoader.profileLoad(binding.ivProfile, friend.profileImage)
            binding.tvUserNickname.text = friend.nickname

            binding.ivSend.setOnClickListener {
                onSendClick(friend)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = RvItemCollectionShareFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friendList[position])
    }

    override fun getItemCount(): Int = friendList.size
}
