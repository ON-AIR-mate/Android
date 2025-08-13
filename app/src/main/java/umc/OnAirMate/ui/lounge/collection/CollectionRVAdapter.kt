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
) : ListAdapter<CollectionData, CollectionViewHolder>(CollectionDiffCallback) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = RvItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding, collectionEventListener)
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

