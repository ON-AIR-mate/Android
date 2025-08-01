package umc.onairmate.ui.chat_room.message

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.RvItemChatBinding

class ChatRVAdapter()
    : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {
        private val items = arrayListOf<String>()

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
        holder.bind(items[position],0)
    }

    override fun getItemCount(): Int= items.size

    inner class ViewHolder(val binding : RvItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : String, type: Int){
            binding.tvContent.text = data
            when(type){
                0 -> binding.layoutUserInfo.gravity = Gravity.END
                1 -> binding.layoutUserInfo.gravity = Gravity.START
                else -> {}
            }
        }

    }

    fun addChat(chat: String){
        items.add(chat)
        notifyItemInserted(items.size-1)
    }
    fun addChatList(chat: List<String>){
        items.addAll(chat)
        notifyDataSetChanged()
    }
}