package umc.onairmate.ui.home.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.databinding.RvItemSearchedVideoBinding
import umc.onairmate.ui.util.NetworkImageLoader

class SearchedVideoRVAdapter(
    private var videoList: List<VideoData>
) : RecyclerView.Adapter<SearchedVideoRVAdapter.ViewHolder>() {

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

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    fun formatNumberClean(num: Int): String {
        return when {
            num < 1_000 -> "$num"
            num < 1_000_000 -> "%.1fk".format(num / 1000.0).removeSuffix(".0k") + "k"
            else -> "%.1fM".format(num / 1_000_000.0).removeSuffix(".0M") + "M"
        }
    }

    fun updateList(newList: List<VideoData>) {
        videoList = newList
        notifyDataSetChanged()
    }
}