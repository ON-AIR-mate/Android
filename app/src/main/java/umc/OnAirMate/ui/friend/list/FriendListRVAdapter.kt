package umc.OnAirMate.ui.friend.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.OnAirMate.data.FriendData
import umc.OnAirMate.data.RequestedFriendData
import umc.OnAirMate.databinding.RvItemFriendBinding
import umc.OnAirMate.databinding.RvItemFriendRequestBinding
import umc.OnAirMate.ui.friend.list.FriendListRVAdapter.RecyclerItem

class FriendListRVAdapter(
    private val context : Context,
): ListAdapter<RecyclerItem, RecyclerView.ViewHolder>(FriendRVAdapterDiffCallback) {
    private lateinit var itemClick: ItemClickListener

    interface ItemClickListener{
        fun clickMessage()
        fun clickMore()
        fun acceptRequest()
    }

    fun setItemClickListener(listener : ItemClickListener){
        itemClick = listener
    }

    companion object{
        private const val VIEW_TYPE_LIST = 0
        private const val VIEW_TYPE_REQUEST = 1
    }

    sealed class  RecyclerItem{
        data class ListTypeItem(val data : FriendData ) : RecyclerItem()
        data class RequestTypeItem(val data : RequestedFriendData ) :  RecyclerItem()
    }

    override fun getItemViewType(position: Int): Int {
        // 리스트속 아이템의 타입에 따라 다른 타입으로 맵핑
        return when (getItem(position)) {
            is RecyclerItem.ListTypeItem -> VIEW_TYPE_LIST
            is RecyclerItem.RequestTypeItem -> VIEW_TYPE_REQUEST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return when (viewType) {
            VIEW_TYPE_LIST -> {
                val binding = RvItemFriendBinding.inflate(inflater, parent, false)
                binding.root.layoutParams = layoutParams
                return FriendViewHolder(binding, context, itemClick)
            }

            VIEW_TYPE_REQUEST -> {
                val binding = RvItemFriendRequestBinding.inflate(inflater, parent, false)
                binding.root.layoutParams = layoutParams
                return FriendRequestViewHolder(binding, context, itemClick)
            }

            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)){
            is RecyclerItem.ListTypeItem -> (holder as FriendViewHolder).bind(item.data)
            is RecyclerItem.RequestTypeItem -> (holder as FriendRequestViewHolder).bind(item.data)
        }
    }

    fun initFriendList(items : List<FriendData>){
        submitList(emptyList())
        val itemList = mutableListOf<RecyclerItem>()
        itemList.addAll(items .map { RecyclerItem.ListTypeItem(it)}) // 방 리스트 아이템
        submitList(itemList)
    }
    fun initRequestList(items : List<RequestedFriendData>){
        submitList(emptyList())
        val itemList = mutableListOf<RecyclerItem>()
        itemList.addAll(items.map { RecyclerItem.RequestTypeItem(it)}) // 방 리스트 아이템
        submitList(itemList)
    }

    object FriendRVAdapterDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
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