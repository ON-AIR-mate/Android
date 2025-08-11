package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import umc.onairmate.databinding.ItemBookmarkBinding
import umc.onairmate.data.model.entity.VideoItem

class VideoListAdapter(private val items: List<VideoItem>) :
    RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItem) {
            binding.thumbnailImage.load(item.thumbnailUrl)
            binding.titleText.text = item.host
            binding.vidiotitleText.text = item.title
            binding.timeText.text = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
