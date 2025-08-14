package umc.onairmate.ui.lounge.collection.detail

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.DialogEditTitleBinding

class CollectionEditTitleDialog (
    private val title: String,
    private val popupClick: (String) -> Unit
): DialogFragment() {
    lateinit var binding: DialogEditTitleBinding

    var textLength: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditTitleBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        binding.etTitle.setText(title)
        setOnClickListener()
        setTextListener()

        // 뒤로가기 가능
        setCancelable(true)

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

    private fun setTextListener(){
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textLength = s?.length ?: 0
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setOnClickListener() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnChangeTitle.setOnClickListener {
            val input = binding.etTitle.text.toString()

            if (input.length in 3..20) {
                popupClick(input)
                dismiss()
            } else {
                binding.tvTitleLengthErrorMsg.visibility = View.VISIBLE
            }
        }
    }
}