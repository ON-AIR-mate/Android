package umc.onairmate.ui.lounge.bookmark.select

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
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.databinding.DialogSelectBookmarkBinding

// 북마크 선택 팝업
class BookmarkSelectDialog(
    private val bookmarkList: List<BookmarkData?>,
    private val selectBookmarkCallback: (BookmarkData) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogSelectBookmarkBinding
    private lateinit var adapter: BookmarkSelectRVAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSelectBookmarkBinding.inflate(layoutInflater)
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
        adapter = BookmarkSelectRVAdapter(bookmarkList)
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookmarks.adapter = adapter
    }

    fun setClickListener() {
        binding.btnOk.setOnClickListener {
            val selectedBookmark = adapter.getSelectedItem()

            if (selectedBookmark == null) {
                Toast.makeText(context, "북마크를 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                selectBookmarkCallback(selectedBookmark)
                dismiss()
            }
        }
    }
}