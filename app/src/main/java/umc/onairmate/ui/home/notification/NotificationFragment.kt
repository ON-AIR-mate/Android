package umc.onairmate.ui.home.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.NotificationData
import umc.onairmate.databinding.FragmentNotificationBinding

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 여기서 원하는 동작 실행 (아이콘과 동일하게)
                    findNavController().popBackStack()
                }
            }
        )

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()  // 현재(Search) 제거 → 홈으로 복귀
        }

        binding.rvToday.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvYesterday.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecentDays.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()

    }

    private fun initAdapter(data: NotificationData) {
        binding.rvToday.adapter = NotificationRVAdapter(data.today)
        binding.rvYesterday.adapter = NotificationRVAdapter(data.yesterday)
        binding.rvRecentDays.adapter = NotificationRVAdapter(data.recentDays)
    }

    private fun setObservers() {

    }

}