package umc.onairmate.ui.friend.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.RvItemChatBinding
import umc.onairmate.databinding.RvItemCollectionChatBinding
import umc.onairmate.databinding.RvItemInviteChatBinding
import umc.onairmate.ui.chat_room.message.ChatViewHolder

class FriendChatRVAdapter(
    private val user : UserData,
    private val friend : FriendData,
    private val eventListener: FriendChatEventListener
) : ListAdapter<FriendChatRVAdapter.RecyclerItem, RecyclerView.ViewHolder>(FriendChatRVAdapterDiffCallback) {
    private val itemList = mutableListOf<RecyclerItem>()

    companion object {
        // ViewHolderType
        private const val VIEW_TYPE_GENERAL = 0
        private const val VIEW_TYPE_INVITE = 1
        private const val VIEW_TYPE_COLLECTION = 2


    }

    // 하나의 리사이클러뷰에 여러 아이템을 넣기 위한 클래스
    sealed class RecyclerItem {
        data class GeneralChatItem(val data: ChatMessageData): RecyclerItem()
        data class InviteChatItem(val data: ChatMessageData, val room: RoomData ) : RecyclerItem()
        data class CollectionChatItem(val data: ChatMessageData, val collection : CollectionData) : RecyclerItem()
    }

    override fun getItemViewType(position: Int): Int {
        // 리스트속 아이템의 타입에 따라 다른 타입으로 맵핑
        return when (getItem(position)) {
            is RecyclerItem.GeneralChatItem -> VIEW_TYPE_GENERAL
            is RecyclerItem.InviteChatItem -> VIEW_TYPE_INVITE
            is RecyclerItem.CollectionChatItem -> VIEW_TYPE_COLLECTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams  =  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return when (viewType){
            VIEW_TYPE_GENERAL ->{
                val binding = RvItemChatBinding.inflate(inflater,parent, false)
                binding.root.layoutParams = layoutParams
                return ChatViewHolder(binding)
            }
            VIEW_TYPE_INVITE -> {
                val binding = RvItemInviteChatBinding.inflate(inflater,parent, false)
                binding.root.layoutParams = layoutParams
                return InviteChatViewHolder(binding, eventListener)
            }
            VIEW_TYPE_COLLECTION-> {
                val binding = RvItemCollectionChatBinding.inflate(inflater,parent, false)
                binding.root.layoutParams = layoutParams
                return CollectionChatViewHolder(binding, eventListener)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 리스트속 아이템의 타입에 따라 다른 viewHolder 할당
        when(val item = getItem(position)){
            is RecyclerItem.GeneralChatItem -> (holder as ChatViewHolder).bind(item.data, user.userId, checkUser(position))
            is RecyclerItem.InviteChatItem -> (holder as InviteChatViewHolder).bind(item.room, checkMyMessage(position))
            is RecyclerItem.CollectionChatItem -> (holder as CollectionChatViewHolder).bind(item.collection, checkMyMessage(position))
        }
    }

    private fun checkUser(pos: Int) = (pos != 0) && (getUserId(getItem(pos)) == getUserId(getItem(pos-1)))
    private fun checkMyMessage(pos : Int) =(getUserId(getItem(pos)) == user.userId)

    private fun getUserId(item : RecyclerItem) : Int{
        return when(item){
            is RecyclerItem.GeneralChatItem -> item.data.userId
            is RecyclerItem.InviteChatItem -> item.data.userId
            is RecyclerItem.CollectionChatItem -> item.data.userId
        }
    }

    fun addChat(data: DirectMessageData) {
        val newList = currentList.toMutableList()
        val name = if (data.senderId == user.userId) user.nickname else friend.nickname
        val profile = if (data.senderId == user.userId) user.profileImage else friend.profileImage
        val chat = ChatMessageData(
            messageId = 0,
            userId= data.senderId,
            nickname = name,
            profileImage = profile?:"",
            content= data.content,
            messageType =  data.messageType,
            timestamp = data.timestamp)
        newList.add(RecyclerItem.GeneralChatItem(chat))
        when(data.messageType){
            "collectionShare" ->   newList.add(RecyclerItem.CollectionChatItem(chat, data.collection!!))
            "roomInvite" ->   newList.add(RecyclerItem.InviteChatItem(chat,data.room!!))
            else -> {}
        }
        submitList(newList)
    }

    // diffCallback을 별도로 뺀다
    object FriendChatRVAdapterDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(
            oldItem: RecyclerItem,
            newItem: RecyclerItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RecyclerItem,
            newItem: RecyclerItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}
