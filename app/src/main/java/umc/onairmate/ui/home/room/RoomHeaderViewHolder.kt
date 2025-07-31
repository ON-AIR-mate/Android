package umc.onairmate.ui.home.room

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.RvItemRoomHeaderBinding

class RoomHeaderViewHolder(
    private val binding : RvItemRoomHeaderBinding,
    private val context: Context,
    private val homeEventListener: HomeEventListener
):  RecyclerView.ViewHolder(binding.root) {

    // sortFlag : 정렬 드롭다운 표시 여부 -> 맨 위의 리스트가 아님 숨김
    fun bind(type: Int, sortFlag: Boolean){
        val textColor = listOf( R.color.white, R.color.disable)
        val backgroundColor= listOf(R.color.main, R.color.transparent)
        val lineColor = listOf(R.color.main, R.color.disable)
        val text = listOf("ONAIR","이어보기")

        binding.tvHeaderTitle.text = text[type]
        binding.tvHeaderTitle.setTextColor(ContextCompat.getColor(context, textColor[type]))
        binding.tvHeaderTitle.setBackgroundColor(ContextCompat.getColor(context, backgroundColor[type]))
        binding.line.setBackgroundColor(ContextCompat.getColor(context, lineColor[type]))

        // 맨위에 헤더가 아니면 정렬 드롭다운 숨기기
        binding.spinnerSortType.visibility = if(sortFlag)View.VISIBLE else View.GONE


        // 정렬 드롭다운 세팅
        val sortTypeList = listOf("최신순", "방장 인기 순")
        val adapter =HomeSortTypeSpinnerAdapter(context, sortTypeList)
        binding.spinnerSortType.adapter = adapter
        binding.spinnerSortType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val searchType = when(position) {
                    0 -> "latest"
                    1 -> "popularity"
                    else -> ""
                }
                homeEventListener.selectSortType(searchType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}