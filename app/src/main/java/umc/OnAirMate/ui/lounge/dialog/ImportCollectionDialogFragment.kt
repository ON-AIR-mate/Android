package umc.onairmate.ui.lounge.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import umc.onairmate.databinding.DialogImportCollectionBinding

class ImportCollectionDialogFragment(
    private val nickname: String,
    private val collectionTitle: String,
    private val onConfirm: () -> Unit
) : DialogFragment() {

    private var _binding: DialogImportCollectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogImportCollectionBinding.inflate(LayoutInflater.from(context))

        binding.tvDialogTitle.text = "내 라운지에 가져오기"
        binding.tvDialogContent.text = "[$nickname]의 $collectionTitle\n해당 컬렉션을 가져오시겠습니까?"

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .create().apply {
                binding.btnCancel.setOnClickListener { dismiss() }
                binding.btnConfirm.setOnClickListener {
                    onConfirm()
                    dismiss()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
