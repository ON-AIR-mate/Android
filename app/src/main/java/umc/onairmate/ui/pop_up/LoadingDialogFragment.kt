package umc.onairmate.ui.pop_up

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.core.content.ContextCompat
import umc.onairmate.R
import androidx.core.graphics.drawable.toDrawable

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        // 프로그레스바 생성
        val progressBar = ProgressBar(requireContext()).apply {
            indeterminateTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.main) // 로딩 색상
        }
        dialog.setContentView(progressBar)

        // 배경 흰색 + 전체 화면
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(Color.WHITE.toDrawable())
        }

        dialog.setCancelable(false) // 뒤로가기 막기
        return dialog
    }
}