package umc.onairmate.ui.home.video

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.databinding.RvItemSearchedVideoBinding
import umc.onairmate.ui.util.NetworkImageLoader

class SearchedVideoRVAdapter(
    private val searchVideoEventListener: SearchVideoEventListener
) : ListAdapter<VideoData, SearchedVideoRVAdapter.ViewHolder>(SearchedVideoRVAdapterDiffCallback) {

    // ViewHolder: 아이템 레이아웃과 바인딩
    inner class ViewHolder(
        private val binding: RvItemSearchedVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoData) {
            Log.d("Check", "binding video: ${video.title}")
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, video.thumbnail)
            binding.tvVideoTitle.text = video.title
            binding.tvChannelName.text = video.channelName
            binding.tvViewCount.text = "조회수 ${formatNumberClean(video.viewCount)}"

            binding.root.setOnClickListener {
                Log.d("Check", "Clicked video: ${video.title}")
                searchVideoEventListener.createRoom(video)
            }
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

    fun formatNumberClean(num: Long): String {
        return when {
            num < 1_000 -> "$num"
            num < 1_000_000 -> "%.1f".format(num / 1000.0).removeSuffix(".0") + "k"
            num < 1_000_000_000 -> "%.1f".format(num / 1_000_000.0).removeSuffix(".0") + "M"
            else -> "%.1f".format(num / 1_000_000_000.0).removeSuffix(".0") + "B"
        }
    }


    // diffCallback을 별도로 뺀다
    object SearchedVideoRVAdapterDiffCallback : DiffUtil.ItemCallback<VideoData>() {

        override fun areItemsTheSame(
            oldItem: VideoData,
            newItem: VideoData
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: VideoData,
            newItem: VideoData
        ): Boolean = oldItem == newItem
    }
}

// 유튜브 영상 검색화면 이벤트 리스너
interface SearchVideoEventListener {
    fun createRoom(data : VideoData){
        return
    }
}