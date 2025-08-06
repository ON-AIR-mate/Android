package umc.onairmate.ui.lounge.dialog

import android.app.Dialog
import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.FragmentShareToUserBinding
import umc.onairmate.ui.lounge.model.Collection
import umc.onairmate.ui.lounge.model.User  // 사용자 모델이 있다면
import umc.onairmate.ui.lounge.adapter.UserAdapter  // 사용자 리스트 어댑터

// Optional
import android.view.WindowManager
import android.graphics.drawable.ColorDrawable
import android.graphics.Color


class ShareToUserDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentShareToUserBinding
    private lateinit var collection: Collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collection = arguments?.getParcelable("collection") ?: error("Missing collection")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShareToUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = "[${collection.ownerName}]의 ${collection.title}"

        val dummyUsers = List(size = 10) {
            User(
                id = it.toLong(),
                nickname = "사용자 $it",
                profileImage = "" // 또는 테스트용 이미지 URL
            )
        }

        binding.recyclerView.adapter = UserAdapter(dummyUsers) { user ->
            // TODO: 유저 선택 시 처리할 로직
            Log.d("UserClick", "Selected: ${user.nickname}")
        }
        binding.btnDone.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(collection: Collection): ShareToUserDialogFragment {
            val args = Bundle().apply {
                putParcelable("collection", collection)
            }
            return ShareToUserDialogFragment().apply {
                arguments = args
            }
        }
    }

}
