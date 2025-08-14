package umc.onairmate.ui.friend.chat

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.RvItemChatBinding
import umc.onairmate.databinding.RvItemCollectionChatBinding
import umc.onairmate.ui.home.room.HomeEventListener
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.TimeFormatter

class CollectionChatViewHolder(
    val binding : RvItemCollectionChatBinding,
    private val eventListener: FriendChatEventListener
) : RecyclerView.ViewHolder(binding.root){
    fun bind(collection: CollectionData,  isMyMessage : Boolean){
        // 크기 제한
        val parentWidth = (itemView.parent as View).width
        val targetWidth = (parentWidth * 0.6f).toInt()
        binding.container.layoutParams.width = targetWidth

        // 내가 보낸 메시지
        binding.root.gravity = if(isMyMessage) Gravity.END else Gravity.START

        binding.root.setOnClickListener {
            eventListener.collectionClick(collection)
        }

        binding.tvTitle.text = collection.title
        binding.tvGeneratedDate.text = "생성일 : ${TimeFormatter.formatCollectionDate(collection.createdAt)}"
        binding.tvLatestModifiedDate.text = "마지막 수정일 : ${TimeFormatter.formatCollectionDate(collection.updatedAt)}"
        binding.tvPrivacy.text = CollectionVisibility.fromApiName(collection.visibility)?.displayName ?: collection.visibility
        binding.tvCountBadge.text = collection.bookmarkCount.toString()
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, collection.coverImage)


    }

}