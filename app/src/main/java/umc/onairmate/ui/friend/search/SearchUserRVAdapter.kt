package umc.onairmate.ui.friend.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.RvItemSearchFriendBinding
import umc.onairmate.ui.util.NetworkImageLoader

class SearchUserRVAdapter(
    private val itemClick : (UserData) -> Unit
) : RecyclerView.Adapter<SearchUserRVAdapter.ViewHolder>() {
    private val items = ArrayList<UserData>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val binding = RvItemSearchFriendBinding.inflate(inflater,parent,false)
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder( val binding : RvItemSearchFriendBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : UserData){
            binding.tvUserNickname.text = data.nickname
            NetworkImageLoader.profileLoad(binding.ivProfile, data.profileImage)
            binding.btnRequestFriend.setOnClickListener {
                itemClick(data)
            }
        }
    }

    fun initData(data : List<UserData>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}