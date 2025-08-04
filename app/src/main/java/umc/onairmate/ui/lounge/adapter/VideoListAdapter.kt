package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import umc.onairmate.databinding.ItemVideoBinding
import umc.onairmate.ui.lounge.model.VideoItem

class VideoListAdapter(private val items: List<VideoItem>) :
    RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItem) {
            binding.ivThumbnail.load(item.thumbnailUrl)
            binding.tvRoomTitle.text = item.roomTitle
            binding.tvVideoTitle.text = item.videoTitle
            binding.tvTimeInfo.text = item.timeInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
