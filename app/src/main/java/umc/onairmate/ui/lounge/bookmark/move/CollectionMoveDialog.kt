package umc.onairmate.ui.lounge.bookmark.move

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.DialogMoveCollectionBinding

// 북마크의 컬렉션 이동 팝업
class CollectionMoveDialog(
    private val collectionList: List<CollectionData>,
    private val moveCollectionCallback: (CollectionData) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogMoveCollectionBinding
    private lateinit var adapter: CollectionMoveRVAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMoveCollectionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        initAdapter()
        setClickListener()

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

    fun initAdapter() {
        adapter = CollectionMoveRVAdapter(collectionList)
        binding.rvCollectionList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCollectionList.adapter = adapter
    }

    // 팝업 클릭 리스너
    fun setClickListener() {
        binding.tvMoveCollection.setOnClickListener {
            val selectedCollection = adapter.getSelectedItem()

            if (selectedCollection == null) {
                Toast.makeText(context, "이동할 컬렉션을 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                moveCollectionCallback(selectedCollection)
                dismiss()
            }
        }
    }
}