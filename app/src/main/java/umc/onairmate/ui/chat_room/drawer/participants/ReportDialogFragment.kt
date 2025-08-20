package umc.onairmate.ui.chat_room.drawer.participants

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.databinding.DialogReportBinding

class ReportDialogFragment(
    private val participant: ParticipantData,
    private val onSubmit: (reasons: List<String>, extraReason: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // layoutInflater 사용
        _binding = DialogReportBinding.inflate(layoutInflater)

        // 타이틀 세팅
        binding.tvTitle.text = "[${participant.nickname}]의 신고 사유"

        // 닫기 버튼
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // 제출 버튼
        binding.btnSubmit.setOnClickListener {
            val reasons = mutableListOf<String>()
            if (binding.cbSpam.isChecked) reasons.add("스팸, 도배")
            if (binding.cbSpoiler.isChecked) reasons.add("스포성 채팅")
            if (binding.cbAbuse.isChecked) reasons.add("욕설")
            if (binding.cbHate.isChecked) reasons.add("혐오 발언")
            if (binding.cbNickname.isChecked) reasons.add("부적절한 닉네임 사용")

            val extraReason = binding.etDetail.text.toString()

            onSubmit(reasons, extraReason)
            dismiss()
        }

        // Dialog 직접 생성
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)

        // 다이얼로그 크기 고정 (피그마에서 요구한 크기)
        dialog.window?.setLayout(320.dpToPx(), 494.dpToPx()) // dp → px 변환 필요

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}