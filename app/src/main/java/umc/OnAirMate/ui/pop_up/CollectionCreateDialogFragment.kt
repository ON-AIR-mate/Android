package umc.onairmate.ui.pop_up

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import umc.onairmate.R
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class CollectionCreateDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.dialog_create_collection, null)

        val etNewTitle = view.findViewById<EditText>(R.id.etNewTitle)
        val rowPrivacy = view.findViewById<LinearLayout>(R.id.rowPrivacy)
        val tvPrivacy  = view.findViewById<TextView>(R.id.tvPrivacy)
        val ivClose    = view.findViewById<ImageView>(R.id.ivClose)
        val btnCreate  = view.findViewById<Button>(R.id.btnChangeTitle)

        var privacy = "비공개"
        tvPrivacy.text = privacy

        // 공개범위 선택 (다이얼로그)
        val options = arrayOf("비공개", "공개")
        val showPrivacyPicker = {
            AlertDialog.Builder(requireContext())
                .setItems(options) { _, which ->
                    privacy = options[which]
                    tvPrivacy.text = privacy
                }
                .show()
        }

        // 텍스트/드롭다운/행 전체 클릭 모두 동일 동작
        rowPrivacy.setOnClickListener { showPrivacyPicker() }
        tvPrivacy.setOnClickListener { showPrivacyPicker() }
        view.findViewById<ImageView>(R.id.ivPrivacyDropdown)
            .setOnClickListener { showPrivacyPicker() }

        // 닫기
        ivClose.setOnClickListener { dismiss() }

        // 생성 버튼
        btnCreate.setOnClickListener {
            val title = etNewTitle.text.toString().trim()
            if (title.length !in 3..20) {
                Toast.makeText(requireContext(), "제목은 3~20자여야 해요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isPrivate = (privacy == "비공개")
            // TODO: 여기서 실제 생성 API 호출/콜백 전달
            Toast.makeText(
                requireContext(),
                "컬렉션 생성: $title (${if (isPrivate) "비공개" else "공개"})",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}
