package umc.onairmate.ui.lounge.bookmark.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.RvItemSelectBookmarkBinding


class BookmarkSelectRVAdapter(
    private val bookmarkList: List<BookmarkData?>
) : RecyclerView.Adapter<BookmarkSelectRVAdapter.BookmarkViewHolder>() {

    private var selectedItemId: Int? = null

    inner class BookmarkViewHolder(val binding: RvItemSelectBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // [개선] 리스너는 ViewHolder 생성 시 한 번만 설정하는 것이 효율적입니다.
            binding.root.setOnClickListener {
                // adapterPosition이 유효하지 않으면 아무것도 하지 않음 (안전장치)
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                // 1. 현재 클릭된 아이템의 ID와 이전에 선택됐던 ID를 가져옴
                val clickedId = bookmarkList[adapterPosition]?.bookmarkId
                val previousSelectedId = selectedItemId

                // 2. 선택 상태 업데이트
                if (selectedItemId == clickedId) {
                    // 이미 선택된 아이템을 다시 클릭하면 선택 해제
                    selectedItemId = null
                } else {
                    // 새로운 아이템을 클릭하면 선택 변경
                    selectedItemId = clickedId
                }

                // 3. UI 갱신
                // 이전에 선택됐던 아이템의 위치를 찾아 갱신 (체크 해제)
                if (previousSelectedId != null) {
                    val previousPosition = bookmarkList.indexOfFirst { it?.bookmarkId == previousSelectedId }
                    if (previousPosition != -1) {
                        notifyItemChanged(previousPosition)
                    }
                }
                // 새로 선택된 아이템(또는 선택 해제된 아이템)을 갱신
                notifyItemChanged(adapterPosition)
            }
        }

        fun bind(bookmarkData: BookmarkData) {
            binding.tvMessage.text = bookmarkData.message

            // [수정] CheckBox의 선택 상태는 'isChecked' 속성을 사용합니다.
            // 현재 아이템의 ID와 선택된 ID가 같은지 비교하여 상태를 결정합니다.
            binding.cbCheckbox.isChecked = (bookmarkData.bookmarkId == selectedItemId)
        }
    }

    fun getSelectedItem(): BookmarkData? {
        return bookmarkList.find { it?.bookmarkId == selectedItemId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = RvItemSelectBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        bookmarkList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = bookmarkList.size
}