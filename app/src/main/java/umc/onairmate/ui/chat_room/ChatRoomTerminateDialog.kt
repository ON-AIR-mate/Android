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