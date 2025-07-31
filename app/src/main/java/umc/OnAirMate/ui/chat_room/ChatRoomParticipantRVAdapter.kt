package umc.onairmate.ui.chat_room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.databinding.RvItemChatRoomUserBinding

class ChatRoomParticipantRVAdapter(
    private val context: Context,
    private var userList: List<ParticipantData>
) : RecyclerView.Adapter<ChatRoomParticipantRVAdapter.ViewHolder>() {

    // ViewHolder: 아이템 레이아웃과 바인딩
    inner class ViewHolder(
        private val binding: RvItemChatRoomUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ParticipantData) {
            binding.tvUserNickname.text = user.nickname
            // todo: 썸네일 로더를 이미지 로더로 이름 변경 후 프로필 이미지 로드
            // todo: 인기도를 어떻게 표현해야되는지..? 잘 모르겠슴

        }
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemChatRoomUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}