package umc.onairmate.ui.home.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.RvItemNotificationBinding


class NotificationRVAdapter(
    private val items : List<String>
): RecyclerView.Adapter<NotificationRVAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val binding = RvItemNotificationBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: RvItemNotificationBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.tvTime.text = items[pos]
            binding.tvMessage.text = items[pos]
        }
    }
}