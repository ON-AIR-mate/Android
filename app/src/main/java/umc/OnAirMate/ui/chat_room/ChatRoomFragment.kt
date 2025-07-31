package umc.onairmate.ui.chat_room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umc.onairmate.R
import umc.onairmate.databinding.FragmentChatRoomBinding

class ChatRoomFragment : Fragment() {

    lateinit var binding: FragmentChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        onClickSetting()
        Log.d("바인딩 체크", binding.ivSetting.toString())

        return binding.root
    }

    fun onClickSetting() {
        binding.ivSetting.setOnClickListener {
            Log.d("클릭체크", "chat room fragment - iv setting")
            (activity as? ChatRoomLayoutActivity)?.openDrawer()
        }
    }
}