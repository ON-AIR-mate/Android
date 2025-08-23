package umc.onairmate.ui.lounge.personal.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.DialogPersonalShareListBinding // 레이아웃 파일은 직접 생성해야 합니다.
import umc.onairmate.ui.lounge.personal.PersonalShareListAdapter // 친구 목록을 위한 어댑터도 필요합니다.

// 컬렉션 공유 다이얼로그
class PersonalShareListDialog(
    private val collectionId: Int,
    private val friendList: List<FriendData>,
    private val shareCallback: (Int, List<Int>) -> Unit // 공유 완료 후 호출할 콜백
) : DialogFragment() {

    private lateinit var binding: DialogPersonalShareListBinding
    private lateinit var adapter: PersonalShareListAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPersonalShareListBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        initAdapter()
        setClickListener()
        setupDialogWindow(dialog)

        return dialog
    }

    private fun initAdapter() {
        adapter = PersonalShareListAdapter()
        binding.rvItemCollectionShareList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemCollectionShareList.adapter = adapter

        // ⭐️ 전달받은 실제 친구 목록을 어댑터에 제출
        adapter.submitList(friendList)
    }

    private fun setClickListener() {
        binding.btnConfirm.setOnClickListener {
            // TODO: 선택된 친구들의 ID를 가져와서 콜백에 전달
            val selectedFriendIds = listOf(456, 789) // 예시
            shareCallback(collectionId, selectedFriendIds)
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