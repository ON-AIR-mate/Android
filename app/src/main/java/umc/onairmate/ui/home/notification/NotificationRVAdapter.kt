package umc.onairmate.ui.home.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.NotificationData
import umc.onairmate.databinding.RvItemNotificationBinding


class NotificationRVAdapter(

): RecyclerView.Adapter<NotificationRVAdapter.ViewHolder>()
{
    private val items = arrayListOf<NotificationData>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
            Log.d("bind", "itemd ${items[pos]}")
            binding.tvTime.text = items[pos].createdAt
            binding.tvMessage.text = items[pos].message
        }
    }

    fun addData(list: List<NotificationData>){
        items.addAll(list)
        notifyDataSetChanged()
    }
}