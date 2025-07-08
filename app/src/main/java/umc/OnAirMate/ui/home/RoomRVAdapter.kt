package umc.OnAirMate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.RvItemRoomBinding

class RoomRVAdapter(
    private val items : List<String>
) : RecyclerView.Adapter<RoomRVAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RvItemRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos : Int){
            binding.tvRoomName.text = items[pos]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams  =  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val binding = RvItemRoomBinding.inflate(inflater,parent,false)
        binding.root.layoutParams =layoutParams
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(position)
    }
}