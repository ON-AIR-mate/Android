package umc.onairmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.RvItemRecommendedVideoBinding

class RecommendedVideoRVAdapter(
    private val items : List<String>,
    private val itemClick: (String) -> Unit
) : RecyclerView.Adapter<RecommendedVideoRVAdapter.ViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val binding = RvItemRecommendedVideoBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int  = items.size

    inner class ViewHolder(private val binding : RvItemRecommendedVideoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.root.setOnClickListener {
                itemClick(items[pos])
            }
        }
    }
}