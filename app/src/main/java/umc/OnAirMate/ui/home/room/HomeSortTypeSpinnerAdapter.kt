package umc.onairmate.ui.home.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import umc.onairmate.R
import umc.onairmate.databinding.SpItemHomeSortTypeBinding

class HomeSortTypeSpinnerAdapter(
    private val context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, 0, items) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: SpItemHomeSortTypeBinding = if (convertView == null) {
            SpItemHomeSortTypeBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            SpItemHomeSortTypeBinding.bind(convertView)
        }

        binding.tvSortType.text = items[position]
        return binding.root
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // 선택된 항목을 표시할 때는 기본 TextView만 필요하므로 따로 작성
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        textView.setTextAppearance(R.style.TextAppearance_App_Medium_10sp)
        textView.setTextColor(ContextCompat.getColor(context, R.color.main))
        return view
    }
}