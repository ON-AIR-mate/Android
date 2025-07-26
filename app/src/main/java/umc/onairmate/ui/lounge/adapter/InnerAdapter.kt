package umc.OnAirMate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.OnAirMate.databinding.ItemBookmarkBinding
import umc.onairmate.model.VideoItem

class InnerAdapter(
    private val videoList: List<VideoItem>
) : RecyclerView.Adapter<InnerAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]

        with(holder.binding) {
            titleText.text = video.title      // 방 제목
            descText.text = video.host        // 영상 제목
            timeText.text = video.time

//            Glide.with(thumbnailImage.context)
//                .load(video.thumbnailUrl)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(thumbnailImage)

            // 더보기 버튼 이벤트 (필요 시 추가)
            moreButton.setOnClickListener {
                // 팝업 or 메뉴 처리
            }
        }
    }

    override fun getItemCount(): Int = videoList.size
}
