package umc.onairmate.ui.lounge.personal.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.DialogPersonalLoungeImportBinding // 레이아웃 파일은 직접 생성해야 합니다.

// 내 라운지에 컬렉션 가져오기 다이얼로그
class PersonalLoungeImportDialog(
    private val collectionId: Int,
    private val importCallback: (Int) -> Unit, // 가져오기 완료 후 호출할 콜백
    private val title: String= ""
) : DialogFragment() {

    private lateinit var binding: DialogPersonalLoungeImportBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPersonalLoungeImportBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        if (!title.isBlank()) binding.tvCollectionTitle.text = title

        setClickListener()
        setupDialogWindow(dialog)

        return dialog
    }

    private fun setClickListener() {
        // '가져오기' 버튼 클릭 시 콜백 호출 후 다이얼로그 닫기
        binding.btnImport.setOnClickListener {
            importCallback(collectionId)
            dismiss()
        }

        // '취소' 버튼 클릭 시 다이얼로그 닫기
        binding.btnCancel.setOnClickListener {
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