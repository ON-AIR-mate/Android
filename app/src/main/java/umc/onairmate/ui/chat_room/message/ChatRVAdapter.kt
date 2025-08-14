package umc.onairmate.ui.chat_room.message

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.databinding.RvItemChatBinding
import umc.onairmate.ui.chat_room.message.ChatViewHolder
import umc.onairmate.ui.util.NetworkImageLoader

class ChatRVAdapter(
    private val userId : Int
) : RecyclerView.Adapter<ChatViewHolder>() {
    private val items = arrayListOf<ChatMessageData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val binding = RvItemChatBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int
    ) {
        holder.bind(items[position], userId, checkUser(position))
    }

    override fun getItemCount(): Int= items.size

    private fun checkUser(pos: Int) =  (pos!= 0 && (items[pos].userId == items[pos-1].userId))

    fun addChat(chat: ChatMessageData){
        items.add(chat)
        notifyItemInserted(items.size-1)
    }
    fun initChatHistory(newMessages: List<ChatMessageData>) {
        items.clear()
        items.addAll(newMessages)
        notifyDataSetChanged()
    }

}