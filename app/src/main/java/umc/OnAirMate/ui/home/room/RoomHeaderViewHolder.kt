package umc.onairmate.ui.home.room

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.RvItemRoomHeaderBinding

class RoomHeaderViewHolder(
    private val binding : RvItemRoomHeaderBinding,
    private val context: Context,
    private val itemClick: ItemClick
):  RecyclerView.ViewHolder(binding.root) {

    fun bind(type: Int){
        val textColor = listOf( R.color.white, R.color.disable)
        val backgroundColor= listOf(R.color.main, R.color.transparent)
        val lineColor = listOf(R.color.main, R.color.disable)
        val text = listOf("ONAIR","이어보기")

        binding.tvHeaderTitle.text = text[type]
        binding.tvHeaderTitle.setTextColor(ContextCompat.getColor(context, textColor[type]))
        binding.tvHeaderTitle.setBackgroundColor(ContextCompat.getColor(context, backgroundColor[type]))
        binding.line.setBackgroundColor(ContextCompat.getColor(context, lineColor[type]))
    }
}