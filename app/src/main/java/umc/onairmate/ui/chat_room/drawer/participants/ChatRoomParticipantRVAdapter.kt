package umc.onairmate.ui.chat_room.drawer.participants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.PopupParticipantOptionsBinding
import umc.onairmate.databinding.RvItemChatRoomUserBinding
import umc.onairmate.ui.home.room.RoomRVAdapter.RecyclerItem
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.SharedPrefUtil

class ChatRoomParticipantRVAdapter(
    private val itemClick : ParticipantItemClickListener
) : ListAdapter<ParticipantData, ChatRoomParticipantRVAdapter.ViewHolder>(ParticipantRVAdapterDiffCallback) {

    // ViewHolder: 아이템 레이아웃과 바인딩
    inner class ViewHolder(
        private val binding: RvItemChatRoomUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val userData = SharedPrefUtil.getData("user_info") ?: UserData()

        fun bind(user: ParticipantData) {
            binding.tvUserNickname.text = user.nickname
            // 프로필 이미지 로드
            NetworkImageLoader.profileLoad(binding.ivUserProfile, user.profileImage)
            binding.tvUserTier.text = user.popularity.toString()

            if (user.userId == userData.userId) {
                binding.ivMore.visibility = View.GONE
            } else {
                binding.ivMore.visibility = View.VISIBLE
                binding.ivMore.setOnClickListener {
                    showPopupMenu(binding.ivMore, user)
                }
            }
        }

        private fun showPopupMenu(anchorView: View, data: ParticipantData){
            val popupBinding = PopupParticipantOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

            // PopupWindow 생성
            val popupWindow = PopupWindow(
                popupBinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )

            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true

            // popupBinding root 크기 측정 후 정렬 위치 계산
            popupBinding.root.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )

            val popupWidth = popupBinding.root.measuredWidth

            // 오른쪽 정렬: anchor 오른쪽 끝 기준
            val offsetX = -popupWidth + anchorView.width
            val offsetY = 0

            // 클릭 리스너 연결
            popupBinding.tvReport.setOnClickListener {
                itemClick.clickReport(data)
                popupWindow.dismiss()
            }
            popupBinding.tvRecommend.setOnClickListener {
                itemClick.clickRecommend(data)
                popupWindow.dismiss()
            }
            popupBinding.tvAddFriend.setOnClickListener {
                itemClick.clickAddFriend(data)
                popupWindow.dismiss()
            }
            popupBinding.tvBlock.setOnClickListener {
                itemClick.clickBlock(data)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemChatRoomUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ParticipantRVAdapterDiffCallback : DiffUtil.ItemCallback<ParticipantData>() {
        override fun areItemsTheSame(
            oldItem: ParticipantData,
            newItem: ParticipantData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ParticipantData,
            newItem: ParticipantData
        ): Boolean {
            return oldItem == newItem
        }
    }
}