package umc.onairmate.ui.lounge.personal.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.DialogPersonalLoungePrivacyInfoBinding

class PersonalLoungePrivacyInfoDialog(
    private val onConfirmCallback: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogPersonalLoungePrivacyInfoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPersonalLoungePrivacyInfoBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        setClickListener()
        setupDialogWindow(dialog)

        return dialog
    }

    private fun setClickListener() {
        binding.btnConfirm.setOnClickListener {
            onConfirmCallback() // 확인 버튼 클릭 시 다음 동작(공유 다이얼로그 띄우기) 실행
            dismiss()
        }
    }

    private fun setupDialogWindow(dialog: Dialog) {
        dialog.window?.let { window ->
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.7f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = layoutParams
        }
    }
}