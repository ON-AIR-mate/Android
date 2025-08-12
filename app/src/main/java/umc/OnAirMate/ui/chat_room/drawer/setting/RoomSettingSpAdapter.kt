package umc.onairmate.ui.chat_room.drawer.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import umc.onairmate.R
import umc.onairmate.databinding.SpItemSettingBinding


class RoomSettingSpAdapter(
    private val context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, R.id.tv_item_setting, items) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: SpItemSettingBinding = if (convertView == null) {
            SpItemSettingBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            SpItemSettingBinding.bind(convertView)
        }

        binding.tvItemSetting.text = items[position]

        return binding.root
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // 선택된 항목을 표시할 때는 기본 TextView만 필요하므로 따로 작성
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        textView.setTextAppearance(R.style.TextAppearance_App_Medium_12sp)
        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        return view
    }
}