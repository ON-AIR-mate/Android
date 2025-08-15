package umc.onairmate.ui.friend.chat

import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.RvItemInviteChatBinding
import umc.onairmate.ui.util.NetworkImageLoader

class InviteChatViewHolder(
    val binding : RvItemInviteChatBinding,
    private val eventListener: FriendChatEventListener
) : RecyclerView.ViewHolder(binding.root){
    fun bind(data : RoomData ,  isMyMessage : Boolean){
        // 크기 제한
        val dm = itemView.context.resources.displayMetrics
        val targetWidth = (dm.widthPixels * 0.6f).toInt()

        val params = binding.container.layoutParams
        params.width = targetWidth
        binding.container.layoutParams = params


        // 내가 보낸 메시지
        binding.root.gravity = if(isMyMessage) Gravity.END else Gravity.START


        binding.tvRoomName.text = data.roomTitle
        binding.tvRoomUserNum.text = "${data.currentParticipants} / ${data.maxParticipants}"
        binding.tvPlayTime.text = data.duration
        binding.tvManagerNickname.text = data.hostNickname
        binding.tvManagerPopularity.text = data.hostPopularity.toString()

        NetworkImageLoader.profileLoad(binding.ivManagerProfile, data.hostProfileImage)
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail,data.videoThumbnail)


        binding.root.setOnClickListener {
            eventListener.invite(data)
        }

    }

}