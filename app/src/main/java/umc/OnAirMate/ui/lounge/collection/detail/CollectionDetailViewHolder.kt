package umc.onairmate.ui.lounge.collection.detail

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

    // [개선] TextWatcher는 ViewHolder의 멤버로, 한 번만 생성합니다.
    private val textWatcher: TextWatcher
    private var currentOriginalDescription: String? = null

    init {
        val adapter = RoomSettingSpAdapter(binding.root.context, visibilityList)
        binding.spVisibility.adapter = adapter

        binding.spVisibility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // adapterPosition이 유효한지 먼저 확인 (안전장치)
                if (adapterPosition == RecyclerView.NO_POSITION) return

                // 즉, '사용자가 직접 선택'했을 때만 이벤트를 처리합니다.
                if (binding.spVisibility.tag != position) {
                    val selectedDisplayName = visibilityList[position]
                    collectionDetailEventListener.onVisibilitySelected(selectedDisplayName)
                }
                // 현재 선택된 위치를 다시 tag에 업데이트합니다.
                binding.spVisibility.tag = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- 텍스트 리스너 설정 (생성 시 한 번만) ---
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // todo: 매번 텍스트가 바뀔때마다 api를 호출하는데.. 너무 자주 호출하는거 아닌가?
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val currentInput = s.toString()
                    if (currentInput != currentOriginalDescription) {
                        collectionDetailEventListener.onDescriptionModified(currentInput)
                    }
                }
            }
        }
        binding.etInputDescription.addTextChangedListener(textWatcher)
    }

    fun bind(item: CollectionDetailData) {
        binding.tvTitle.text = item.title
        binding.tvGeneratedDate.text = "생성일 : ${TimeFormatter.formatCollectionDate(item.createdAt)}"
        binding.tvCount.text = item.bookmarkCount.toString()
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, item.coverImage)

        binding.tvModifyTitle.setOnClickListener {
            collectionDetailEventListener.onTitleModifyClicked()
        }

        // --- 스피너 상태 업데이트 로직 ---
        // 1. 서버 데이터에 맞는 초기 위치를 찾습니다.
        val position = visibilityList.indexOf(
            CollectionVisibility.fromApiName(item.visibility)?.displayName
        ).takeIf { it >= 0 } ?: 0

        // [핵심 3] 리스너를 실행시키지 않고 선택 위치를 UI에 반영하는 방법
        binding.spVisibility.tag = position // 먼저 현재 위치를 tag에 저장
        binding.spVisibility.setSelection(position) // 그 다음, 선택 위치를 설정

        // 2. EditText 상태 업데이트 (리스너 호출 방지)
        currentOriginalDescription = item.description
        binding.etInputDescription.removeTextChangedListener(textWatcher)
        binding.etInputDescription.setText(item.description)
        binding.etInputDescription.addTextChangedListener(textWatcher)

    }

}

interface CollectionDetailEventListener {
    fun onVisibilitySelected(selectedVisibility: String) {}
    fun onTitleModifyClicked() {}
    fun onDescriptionModified(input: String) {}
}