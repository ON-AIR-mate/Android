package umc.onairmate.ui.lounge.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import umc.onairmate.R
import android.widget.Button

class ShareBlockedDialogFragment(
    private val collectionTitle: String,
    private val visibility: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_share_blocked, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        tvTitle.text = "[$collectionTitle]"
        tvMessage.text = "공유할 수 없는 공개범위입니다.\n현재 공개범위 : $visibility"

        btnConfirm.setOnClickListener {
            dismiss()
        }

        return view
    }
}
