package umc.onairmate.ui.home.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.databinding.RvItemSearchedVideoBinding
import umc.onairmate.ui.util.NetworkImageLoader

class SearchedVideoRVAdapter(
    // private var videoList: List<VideoData>
) : ListAdapter<VideoData, SearchedVideoRVAdapter.ViewHolder>(SearchedVideoRVAdapterDiffCallback) {

    // ViewHolder: 아이템 레이아웃과 바인딩
    inner class ViewHolder(
        private val binding: RvItemSearchedVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoData) {
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, video.thumbnail)
            binding.tvVideoTitle.text = video.title
            binding.tvChannelName.text = video.channelName
            binding.tvViewCount.text = "조회수 ${formatNumberClean(video.viewCount)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemSearchedVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun formatNumberClean(num: Int): String {
        return when {
            num < 1_000 -> "$num"
            num < 1_000_000 -> "%.1f".format(num / 1000.0).removeSuffix(".0") + "k"
            else -> "%.1f".format(num / 1_000_000.0).removeSuffix(".0") + "M"
        }
    }


    // diffCallback을 별도로 뺀다
    object SearchedVideoRVAdapterDiffCallback : DiffUtil.ItemCallback<VideoData>() {

        override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean = oldItem == newItem
    }
}