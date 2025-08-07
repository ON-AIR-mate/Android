package umc.onairmate.ui.lounge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.ItemLoungeCardBinding
import umc.onairmate.ui.lounge.model.LoungeItem
import umc.onairmate.ui.lounge.model.Collection



class PersonalLoungeAdapter (
    private val itemList: List<Collection>,
    private val onCopyClick: (Collection) -> Unit,
    private val onShareClick: (Collection) -> Unit,
    private val onImportClicked: (nickname: String, collectionTitle: String) -> Unit
): RecyclerView.Adapter<PersonalLoungeAdapter.ViewHolder>() {

    private var expandedPosition = -1

    inner class ViewHolder(private val binding: ItemLoungeCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Collection, position: Int) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "작성일: ${item.date}"
            binding.tvOwner.text = item.ownerName
            binding.tvDescription.text = item.description ?: ""

            // 소개글 토글
            binding.tvDescription.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
            binding.root.setOnClickListener {
                expandedPosition = if (expandedPosition == position) -1 else position
                notifyDataSetChanged()
            }

            binding.btnMore.setOnClickListener {
                showPopup(it, item)
            }
        }

        private fun showPopup(anchor: View, item: Collection) {
            val popup = PopupMenu(anchor.context, anchor)
            popup.menuInflater.inflate(R.menu.menu_personal_lounge, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_copy -> {
                        onImportClicked(item.ownerName, item.title)
                        true
                    }
                    R.id.action_share -> {
                        onShareClick(item)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLoungeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item,position)
    }

    override fun getItemCount(): Int = itemList.size
}
