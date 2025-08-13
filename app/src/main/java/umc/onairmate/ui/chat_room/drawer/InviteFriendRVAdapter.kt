package umc.onairmate.ui.chat_room.drawer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.RvItemInviteFriendBinding
import umc.onairmate.ui.util.NetworkImageLoader

class InviteFriendRVAdapter(
    private var friendList: List<FriendData>,
    private var onClickInviteFriend: (FriendData) -> Unit
): RecyclerView.Adapter<InviteFriendRVAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: RvItemInviteFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(friend: FriendData) {
            NetworkImageLoader.profileLoad(binding.ivProfile, friend.profileImage)
            binding.tvUserNickname.text = friend.nickname

            binding.ivInvite.setOnClickListener {
                onClickInviteFriend(friend)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemInviteFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = friendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}