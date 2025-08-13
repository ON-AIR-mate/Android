package umc.onairmate.ui.lounge.collection.delete

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.DialogDeleteCollectionBinding
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.TimeFormatter

class CollectionDeleteDialog (
    private val collectionData: CollectionData,
    private val popupClick: PopupClick
): DialogFragment() {
    lateinit var binding: DialogDeleteCollectionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteCollectionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        initScreen()
        setOnClickListener()

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

    private fun initScreen() {
        binding.tvCount.text = collectionData.bookmarkCount.toString()
        binding.tvCollectionTitle.text = collectionData.title
        binding.tvCreatedDate.text = "생성일 : ${TimeFormatter.formatCollectionDate(collectionData.createdAt)}"
        binding.tvUpdatedDate.text = "마지막 수정일 : ${TimeFormatter.formatCollectionDate(collectionData.updatedAt)}"
    }

    private fun setOnClickListener() {
        binding.icClose.setOnClickListener {
            dismiss()
        }
        binding.btnDelete.setOnClickListener {
            popupClick.leftClickFunction()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}