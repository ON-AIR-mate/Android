package umc.onairmate.ui.lounge.personal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.ui.util.ImageLoader
import umc.onairmate.databinding.RvItemCollectionBinding
import umc.onairmate.data.model.entity.CollectionData

class PersonalCollectionAdapter(
    private val listener: OnCollectionActionListener
) : ListAdapter<CollectionData, PersonalCollectionAdapter.VH>(Diff()) {

    interface OnCollectionActionListener {
        fun onItemClick(item: CollectionData)
        fun onMoreClick(view: View, item: CollectionData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCollectionBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val b: RvItemCollectionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: CollectionData) = with(b) {

            // 썸네일
            Glide.with(root).load(item.thumbnailUrl)
                .into(ivThumbnail)

            // 제목
            tvTitle.text = item.title

            // 생성/수정일
            tvGeneratedDate.text = "생성일 : ${item.createdAt}"
            tvLatestModifiedDate.text = "마지막 수정일 : ${item.updatedAt}"

            // 공개범위
            tvPrivacy.text = item.visibility

            // ex) 더보기
            ivMore.setOnClickListener { listener.onMoreClick(it, item) }

            root.setOnClickListener { listener.onItemClick(item) }
        }
    }

    private class Diff : DiffUtil.ItemCallback<CollectionData>() {
        override fun areItemsTheSame(oldItem: CollectionData, newItem: CollectionData) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CollectionData, newItem:CollectionData) =
            oldItem == newItem
    }
}
