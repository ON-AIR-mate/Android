package umc.onairmate.ui.lounge.collection.detail

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.databinding.RvItemCollectionDetailBinding
import umc.onairmate.ui.chat_room.drawer.setting.RoomSettingSpAdapter
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.TimeFormatter

class CollectionDetailViewHolder (
    private val binding: RvItemCollectionDetailBinding,
    private val collectionDetailEventListener: CollectionDetailEventListener
) : RecyclerView.ViewHolder(binding.root) {

    val visibilityList = CollectionVisibility.entries.map { it.displayName }
    var selectedVisibility: String = visibilityList[0]

    private var descriptionRunnable: Runnable? = null
    private val descriptionHandler = Handler(Looper.getMainLooper())

    fun bind(item: CollectionDetailData) {
        binding.tvTitle.text = item.title
        binding.tvGeneratedDate.text = "생성일 : ${TimeFormatter.formatCollectionDate(item.createdAt)}"
        binding.tvCount.text = item.bookmarkCount.toString()
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, item.coverImage)

        binding.tvModifyTitle.setOnClickListener {
            collectionDetailEventListener.onTitleModifyClicked()
        }

        initSpinner()
        setTextListener()

        val visibilityPosition =
            visibilityList.indexOf(CollectionVisibility.fromApiName(item.visibility)?.apiName)
        selectedVisibility = visibilityList[visibilityPosition]
        binding.spVisibility.setSelection(visibilityPosition)
    }

    private fun setTextListener(){
        binding.etInputDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                descriptionRunnable?.let { descriptionHandler.removeCallbacks(it) }
                descriptionRunnable = Runnable {
                    val input = binding.etInputDescription.text.toString()
                    collectionDetailEventListener.onDescriptionModifyed(input)
                }
                descriptionHandler.postDelayed(descriptionRunnable!!, 300) // 300ms 디바운스
            }
        })
    }

    private fun initSpinner() {
        val adapter = RoomSettingSpAdapter(binding.root.context, visibilityList)
        binding.spVisibility.adapter = adapter
        binding.spVisibility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedVisibility = visibilityList[position]
                collectionDetailEventListener.onVisibilitySelected(selectedVisibility)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}

interface CollectionDetailEventListener {
    fun onVisibilitySelected(selectedVisibility: String) {}
    fun onTitleModifyClicked() {}
    fun onDescriptionModifyed(input: String) {}
}