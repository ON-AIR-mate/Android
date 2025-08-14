package umc.onairmate.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.RvItemBookmarkBinding
import umc.onairmate.ui.profile.OnRoomActionListener

data class ParticipatedRoomItem(
    val roomId: Long,
    val roomtitle: String,
    val videotitle: String,
    val bookmarktime: Int,
    val lastEnteredAt: String?
    // 필요 필드 추가
)

class ParticipatedRoomsAdapter(
    private val listener: OnRoomActionListener
) : ListAdapter<ParticipatedRoomItem, ParticipatedRoomsAdapter.VH>(Diff()) {

    inner class VH(val binding: RvItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: ParticipatedRoomItem) {
            binding.tvRoomTitle.text = item.roomtitle
            binding.tvVideoTitle.text = item.videotitle
            binding.tvBookmarkTime.text = item.bookmarktime.toString()

                // 카드 전체 클릭 → 방 입장
                root.setOnClickListener { listener.onRoomClick(item.roomId) }

                // 삭제 버튼(또는 more 메뉴) 클릭 시 콜백
                binding.btnMore.setOnClickListener { anchor ->
                    val popup = PopupMenu(anchor.context, anchor)
                    popup.menuInflater.inflate(R.menu.menu_bookmark_delete, popup.menu)
                    popup.setOnMenuItemClickListener { mi ->
                        when (mi.itemId) {
                            R.id.action_deleteFromHistory -> {
                                listener.onDeleteClick(item.roomId)
                                true
                            }
                            else -> false
                        }
                    }
                    popup.show()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = RvItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    private class Diff : DiffUtil.ItemCallback<ParticipatedRoomItem>() {
        override fun areItemsTheSame(a: ParticipatedRoomItem, b: ParticipatedRoomItem) =
            a.roomId == b.roomId
        override fun areContentsTheSame(a: ParticipatedRoomItem, b: ParticipatedRoomItem) = a == b
    }
}

// 유틸: 초 → "mm:ss" (1시간 이상이면 "HH:mm:ss")
private fun formatToMmSs(secondsTotal: Int): String {
    val h = secondsTotal / 3600
    val m = (secondsTotal % 3600) / 60
    val s = secondsTotal % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

// 유틸: "2025-08-14T16:20:00Z" 또는 "yyyy-MM-dd HH:mm:ss" → "방금/분 전/시간 전/일 전"
private fun toRelative(dateStr: String): String = try {
    val instant = try { java.time.Instant.parse(dateStr) } catch (_: Exception) {
        val f = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        java.time.LocalDateTime.parse(dateStr, f).atZone(java.time.ZoneId.systemDefault()).toInstant()
    }
    val mins = java.time.Duration.between(instant, java.time.Instant.now()).toMinutes()
    when {
        mins < 1 -> "방금"
        mins < 60 -> "${mins}분 전"
        mins < 60 * 24 -> "${mins / 60}시간 전"
        else -> "${mins / 60 / 24}일 전"
    }
} catch (_: Exception) { dateStr }
