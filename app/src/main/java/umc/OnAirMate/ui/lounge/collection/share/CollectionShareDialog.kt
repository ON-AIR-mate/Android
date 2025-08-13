package umc.onairmate.ui.lounge.collection.share

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
import umc.onairmate.databinding.DialogCollectionShareBinding

class CollectionShareDialog(
    private val friendList: List<FriendData>,
    private val sendCollectionCallback: (FriendData) -> Unit
) : DialogFragment() {

    private var _binding: DialogCollectionShareBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionShareRVAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCollectionShareBinding.inflate(layoutInflater)
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
        adapter = CollectionShareRVAdapter(friendList, { data ->
            sendCollectionCallback(data)
        })

        binding.rvFriendList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriendList.adapter = adapter
    }

    fun setClickListener() {
        binding.btnOk.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}