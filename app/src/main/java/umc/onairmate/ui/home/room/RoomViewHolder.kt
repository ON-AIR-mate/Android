package umc.onairmate.ui.home.room

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.RvItemRoomBinding

class RoomViewHolder(
    private val binding : RvItemRoomBinding,
    private val context: Context,
    private val homeEventListener: HomeEventListener
):  RecyclerView.ViewHolder(binding.root) {
    fun bind(data : RoomData){
        binding.tvRoomName.text = data.roomTitle
        binding.tvRoomUserNum.text = "${data.currentParticipants} / ${data.maxParticipants}"
        binding.tvPlayTime.text = data.duration
        binding.tvManagerNickname.text = data.hostNickname


        binding.root.setOnClickListener {
            homeEventListener.joinRoom(data)
        }
    }
}