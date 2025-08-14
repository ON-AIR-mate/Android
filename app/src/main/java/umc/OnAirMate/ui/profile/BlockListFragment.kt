package umc.onairmate.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.R
import umc.onairmate.databinding.FragmentBlockListBinding

class BlockListFragment : Fragment() {

    private var _binding: FragmentBlockListBinding? = null
    private val binding get() = _binding!!

    private lateinit var blockListAdapter: BlockListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlockListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("BlockListFragment", "view created, binding.ivBack=${binding.ivBack}")
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        blockListAdapter = BlockListAdapter { blockedUser ->
            // 차단 해제 클릭 시 처리
        }
        binding.rvBlockList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = blockListAdapter
        }

        // 테스트 데이터
        blockListAdapter.submitList(
            listOf(
                BlockedUser("차단한 사용자 닉네임", "차단 사유", "0000년 00월 00일"),
                BlockedUser("차단한 사용자 닉네임", "차단 사유", "0000년 00월 00일"),
                BlockedUser("차단한 사용자 닉네임", "차단 사유", "0000년 00월 00일")
            )
        )

    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}