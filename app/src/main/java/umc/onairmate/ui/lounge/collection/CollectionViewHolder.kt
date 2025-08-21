package umc.onairmate.ui.lounge.collection

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.databinding.PopupCollectionOptionsBinding
import umc.onairmate.databinding.RvItemCollectionBinding
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.TimeFormatter

class CollectionViewHolder(
    private val binding: RvItemCollectionBinding,
    private val collectionEventListener: CollectionEventListener,
    private val isOthers : Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CollectionData ) {
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


        // 남의 라운지에서 재활용 하기 위한 임시 로짇
        if (isOthers){
            popupBinding.tvDeleteCollection.text = "내 라운지에 가져오기"
        }


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

interface CollectionEventListener {
    fun deleteCollection(collection: CollectionData) {}
    fun shareCollection(collection: CollectionData) {}
    fun clickCollectionItem(collection: CollectionData) {}
}