package umc.onairmate.ui.lounge.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.databinding.PopupCollectionOptionsBinding
import umc.onairmate.databinding.RvItemCollectionBinding
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.TimeFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CollectionRVAdapter(
    private val collectionEventListener: CollectionEventListener
) : ListAdapter<CollectionData, CollectionRVAdapter.CollectionViewHolder>(CollectionDiffCallback) {

    inner class CollectionViewHolder(private val binding: RvItemCollectionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CollectionData) {
            binding.root.setOnClickListener { collectionEventListener.clickCollectionItem(item) }

            binding.tvTitle.text = item.title
            binding.tvGeneratedDate.text = "생성일 : ${TimeFormatter.formatCollectionDate(item.createdAt)}"
            binding.tvLatestModifiedDate.text = "마지막 수정일 : ${TimeFormatter.formatCollectionDate(item.updatedAt)}"
            binding.tvPrivacy.text = CollectionVisibility.fromApiName(item.visibility)?.displayName ?: item.visibility
            binding.tvCountBadge.text = item.bookmarkCount.toString()
            NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, item.coverImage)

            binding.ivMore.setOnClickListener {
                showPopupMenu(binding.ivMore, item)
            }
        }

        private fun showPopupMenu(anchorView: View, data: CollectionData){
            val popupBinding = PopupCollectionOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

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
            popupBinding.tvDeleteCollection.setOnClickListener {
                collectionEventListener.deleteCollection(data)
                popupWindow.dismiss()
            }
            popupBinding.tvShareCollection.setOnClickListener {
                collectionEventListener.shareCollection(data)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = RvItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object CollectionDiffCallback : DiffUtil.ItemCallback<CollectionData>() {
        override fun areItemsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            // data class의 '==' 연산자는 모든 프로퍼티를 비교하므로, 내용 비교에 적합
            return oldItem == newItem
        }
    }
}

interface CollectionEventListener {
    fun deleteCollection(collection: CollectionData) {}
    fun shareCollection(collection: CollectionData) {}
    fun clickCollectionItem(collection: CollectionData) {}
}