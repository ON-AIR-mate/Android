package umc.onairmate.ui.lounge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.Bookmark

class BookmarkAdapter(private val bookmarkList: List<Bookmark>) :
    RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.thumbnailImage)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val descText: TextView = itemView.findViewById(R.id.descText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
        val moreButton: ImageButton = itemView.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val item = bookmarkList[position]
        holder.thumbnailImage.setImageResource(item.thumbnailResId)
        holder.titleText.text = item.title
        holder.descText.text = item.description
        holder.timeText.text = item.time

        holder.moreButton.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.bookmark_popup_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete_bookmark -> {
                        // TODO: 삭제 기능 호출
                        Toast.makeText(view.context, "북마크 삭제 클릭됨", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }


    override fun getItemCount(): Int = bookmarkList.size
}