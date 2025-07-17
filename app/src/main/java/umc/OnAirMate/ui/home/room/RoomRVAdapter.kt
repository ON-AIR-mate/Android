package umc.onairmate.ui.home.room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.RoomData
import umc.onairmate.databinding.RvItemRoomBinding
import umc.onairmate.databinding.RvItemRoomHeaderBinding

class RoomRVAdapter(
    private val context : Context,
    private val homeEventListener: HomeEventListener
) : ListAdapter<RoomRVAdapter.RecyclerItem, RecyclerView.ViewHolder>(RoomRVAdapterDiffCallback) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ROOM = 1

        private const val ROOM_ON_AIR = 0
        private const val ROOM_CONTINUING = 1
    }

    sealed class RecyclerItem {
        data class HeaderTypeItem(val type : Int, val sortFlag : Boolean = true): RecyclerItem()
        data class RoomTypeItem(val data: RoomData)  : RecyclerItem()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerItem.HeaderTypeItem -> VIEW_TYPE_HEADER
            is RecyclerItem.RoomTypeItem -> VIEW_TYPE_ROOM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams  =  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return when (viewType){
            VIEW_TYPE_HEADER ->{
                val binding = RvItemRoomHeaderBinding.inflate(inflater,parent, false)
                binding.root.layoutParams = layoutParams
                return RoomHeaderViewHolder(binding, context, homeEventListener)
            }
            VIEW_TYPE_ROOM -> {
                val binding = RvItemRoomBinding.inflate(inflater, parent, false)
                binding.root.layoutParams = layoutParams
                return RoomViewHolder(binding, context, homeEventListener)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(val item = getItem(position)){
           is RecyclerItem.HeaderTypeItem -> (holder as RoomHeaderViewHolder).bind(item.type, item.sortFlag)
           is RecyclerItem.RoomTypeItem -> (holder as RoomViewHolder).bind(item.data)
       }
    }

    fun initData(
        continuingRooms: List<RoomData>,
        onAirRooms: List<RoomData>
    ) {
        val itemList = mutableListOf<RecyclerItem>()

        if (continuingRooms.isNotEmpty()) {
            itemList.add(RecyclerItem.HeaderTypeItem(ROOM_CONTINUING)) // 진행중 방 헤더
            itemList.addAll(continuingRooms.map { RecyclerItem.RoomTypeItem(it) })
        }

        if (onAirRooms.isNotEmpty()) {
            itemList.add(RecyclerItem.HeaderTypeItem(ROOM_ON_AIR, continuingRooms.isEmpty())) // onAir 방 헤더
            itemList.addAll(onAirRooms.map { RecyclerItem.RoomTypeItem(it) })
        }
        submitList(emptyList())
        submitList(itemList)
    }

    // diffCallback을 별도로 뺀다
    object RoomRVAdapterDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
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
