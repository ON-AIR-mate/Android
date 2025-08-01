package umc.onairmate.ui.chat_room.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.FragmentVideoChatBinding

@AndroidEntryPoint
class FragmentVideoChat: Fragment() {
    private val TAG = javaClass.simpleName

    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private val adapter = ChatRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            adapter.addChat(text)
            binding.etInputChat.setText("")
        }

        return binding.root
    }
}