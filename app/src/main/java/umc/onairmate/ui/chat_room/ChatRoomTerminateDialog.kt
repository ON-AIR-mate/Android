package umc.onairmate.ui.chat_room

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.DialogTerminateRoomBinding
import umc.onairmate.ui.pop_up.PopupClick

/**
 * ChatRoomTerminateDialog: 비디오가 끝까지 재생되었거나 방장이 퇴장하며 방이 종료되었을 때의 팝업
 * - 리소스: dialog_terminate_room
 * - 매개변수
 *   1. clickFunc: 팝업에서 특정 버튼을 눌렀을 때의 상호작용 지정
 */
class ChatRoomTerminateDialog(
    private val clickFunc: PopupClick
) : DialogFragment() {
    lateinit var binding: DialogTerminateRoomBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTerminateRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        setClickListener()

        // 취소 불가능
        setCancelable(false)

        // 배경 투명 + 밝기 조절 (0.7)
        dialog.window?.let { window ->
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.7f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = layoutParams
        }

        return dialog
    }

    private fun setClickListener() {
        binding.btnLeft.setOnClickListener {
            clickFunc.leftClickFunction()
        }
    }
}