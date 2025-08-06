package umc.onairmate.ui.chat_room.message

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.databinding.RvItemChatBinding
import umc.onairmate.ui.util.NetworkImageLoader

class ChatRVAdapter(
    private val userId : Int
) : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {
    private val items = arrayListOf<ChatMessageData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val binding = RvItemChatBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int= items.size

    inner class ViewHolder(val binding : RvItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(pos : Int){
            val data = items[pos]
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
            if(pos!= 0 && (data.userId == items[pos-1].userId))
                binding.layoutUserInfo.visibility = View.GONE

        }

    }

    fun addChat(chat: ChatMessageData){
        items.add(chat)
        notifyItemInserted(items.size-1)
    }
    fun setMessages(newMessages: List<ChatMessageData>) {
        items.clear()
        items.addAll(newMessages)
        notifyDataSetChanged()
    }

}