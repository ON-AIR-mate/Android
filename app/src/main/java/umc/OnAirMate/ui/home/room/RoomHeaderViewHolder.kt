package umc.OnAirMate.ui.home.room

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import umc.OnAirMate.R
import umc.OnAirMate.databinding.RvItemRoomHeaderBinding

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
                // 선택된 값의 String (정렬 타입) 리턴
                val selectedItem = parent?.getItemAtPosition(position) as String
                homeEventListener.selectSortType(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}