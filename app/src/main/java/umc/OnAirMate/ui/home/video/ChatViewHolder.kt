package umc.onairmate.ui.home.video

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.databinding.RvItemChatBinding
import umc.onairmate.ui.util.NetworkImageLoader

class ChatViewHolder(val binding : RvItemChatBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(data : ChatMessageData, userId: Int, isSameUser : Boolean){
        binding.tvContent.text = data.content
        binding.tvNickname.text =  data.nickname

        // 내가 보낸 메시지
        if(data.userId == userId){
            binding.root.gravity = Gravity.END
            binding.ivProfileLeft.visibility = View.GONE
            NetworkImageLoader.profileLoad(binding.ivProfileRight, data.profileImage)
        }
        else {
            binding.root.gravity = Gravity.START
            binding.ivProfileRight.visibility = View.GONE
            NetworkImageLoader.profileLoad(binding.ivProfileLeft, data.profileImage)
        }

        // 첫 전송이 아니고, 한사람이 연속해서 메시지를 보낸 경우
        if(isSameUser)binding.layoutUserInfo.visibility = View.GONE

    }

}