package umc.onairmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.databinding.RvItemRecommendedVideoBinding
import umc.onairmate.ui.util.NetworkImageLoader

class RecommendedVideoRVAdapter(
    private val items : List<VideoData>,
    private val itemClick: (VideoData) -> Unit
) : RecyclerView.Adapter<RecommendedVideoRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val binding = RvItemRecommendedVideoBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int  = items.size

    inner class ViewHolder(private val binding : RvItemRecommendedVideoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: VideoData){
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, data.thumbnail)
            binding.root.setOnClickListener {
                itemClick(data)
            }
        }
    }
}