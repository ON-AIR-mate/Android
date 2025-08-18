package umc.onairmate.ui.profile.participated_room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.ParticipatedRoomData
import umc.onairmate.data.model.entity.RoomArchiveData
import umc.onairmate.databinding.LlItemTimelineTextBinding
import umc.onairmate.databinding.PopupBookmarkOptionsBinding
import umc.onairmate.databinding.PopupParticipatedRoomOptionsBinding
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.ui.util.NetworkImageLoader

class ParticipatedRoomsAdapter(
    private val listener: ParticipatedRoomEventListener
) : ListAdapter<ParticipatedRoomData, ParticipatedRoomsAdapter.ParticipatedRoomViewHolder>(ParticipatedRoomDiffCallback) {

    inner class ParticipatedRoomViewHolder(val binding: RvItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false

        fun bind(item: ParticipatedRoomData) {
            binding.tvRoomTitle.text = item.roomTitle
            binding.tvVideoTitle.text = item.videoTitle
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, item.videoThumbnail)

            val inflater = LayoutInflater.from(itemView.context)

            // 북마크 타임라인 처리
            binding.llBookmarkText.removeAllViews()
            item.bookmarks.forEachIndexed { index, bookmark ->
                val itemBinding =
                    LlItemTimelineTextBinding.inflate(inflater, binding.llBookmarkText, false)
                itemBinding.tvBookmarkTime.text = bookmark.message

                if (index >= 2) itemBinding.root.visibility = View.GONE

                binding.llBookmarkText.addView(itemBinding.root)
            }

            // 펼치기/접기 클릭 이벤트 설정
            if (item.bookmarks.size > 2) {
                binding.ivOpenBookmarks.visibility = View.VISIBLE
                binding.ivCloseBookmarks.visibility = View.GONE

                dropDownClickListener()
            } else {
                binding.ivOpenBookmarks.visibility = View.GONE
                binding.ivCloseBookmarks.visibility = View.GONE
            }

            // 더보기 버튼 클릭 리스너
            binding.btnMore.setOnClickListener {
                showPopupMenu(binding.btnMore, item)
            }
        }

        private fun dropDownClickListener() {
            binding.ivOpenBookmarks.setOnClickListener {
                isExpanded = true
                for (i in 2 until binding.llBookmarkText.childCount) {
                    binding.llBookmarkText.getChildAt(i).visibility = View.VISIBLE
                }
                binding.ivOpenBookmarks.visibility = View.GONE
                binding.ivCloseBookmarks.visibility = View.VISIBLE
            }
            binding.ivCloseBookmarks.setOnClickListener {
                isExpanded = false
                for (i in 2 until binding.llBookmarkText.childCount) {
                    binding.llBookmarkText.getChildAt(i).visibility = View.GONE
                }
                binding.ivOpenBookmarks.visibility = View.VISIBLE
                binding.ivCloseBookmarks.visibility = View.GONE
            }
        }

        private fun showPopupMenu(anchorView: View, data: ParticipatedRoomData){
            val popupBinding = PopupParticipatedRoomOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

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
            popupBinding.tvDeleteParticipatedRoom.setOnClickListener {
                listener.onDeleteClick(data.roomId)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipatedRoomViewHolder {
        val b = RvItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ParticipatedRoomViewHolder(b)
    }

    override fun onBindViewHolder(holder: ParticipatedRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object ParticipatedRoomDiffCallback : DiffUtil.ItemCallback<ParticipatedRoomData>() {
        override fun areItemsTheSame(a: ParticipatedRoomData, b: ParticipatedRoomData) =
            a.roomId == b.roomId
        override fun areContentsTheSame(a: ParticipatedRoomData, b: ParticipatedRoomData) = a == b
    }
}