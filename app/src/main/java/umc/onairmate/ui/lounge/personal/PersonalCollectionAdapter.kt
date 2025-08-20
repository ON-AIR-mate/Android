package umc.onairmate.ui.lounge.personal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.RvItemCollectionBinding
import umc.onairmate.ui.util.NetworkImageLoader // NetworkImageLoader import

class PersonalCollectionAdapter(
    private val listener: OnCollectionActionListener
) : ListAdapter<CollectionData, PersonalCollectionAdapter.VH>(DiffCallback()) {

    interface OnCollectionActionListener {
        fun onItemClick(item: CollectionData)
        fun onMoreClick(view: View, item: CollectionData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RvItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: RvItemCollectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionData) {
            with(binding) {
                // NetworkImageLoader를 사용하여 썸네일 이미지 로드
                NetworkImageLoader.thumbnailLoad(ivThumbnail, item.coverImage)

                // 텍스트 데이터 설정
                tvTitle.text = item.title
                tvGeneratedDate.text = "생성일 : ${item.createdAt}"
                tvLatestModifiedDate.text = "마지막 수정일 : ${item.updatedAt}"

                //공개범위
                tvPrivacy.text = item.visibility

                // 클릭 리스너 설정
                root.setOnClickListener { listener.onItemClick(item) }
                ivMore.setOnClickListener { listener.onMoreClick(it, item) }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CollectionData>() {
        override fun areItemsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            // 'id'가 고유 식별자라고 가정합니다.
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CollectionData, newItem: CollectionData): Boolean {
            // data class의 equals()를 사용하여 내용이 같은지 확인합니다.
            return oldItem == newItem
        }
    }
}