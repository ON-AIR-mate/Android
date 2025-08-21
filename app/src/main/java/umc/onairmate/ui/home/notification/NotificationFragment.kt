package umc.onairmate.ui.home.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.response.NotificationResponse
import umc.onairmate.databinding.FragmentNotificationBinding
import kotlin.getValue

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()

    private val todayAdapter = NotificationRVAdapter()
    private val yesterdayAdapter = NotificationRVAdapter()
    private val recentAdapter = NotificationRVAdapter()

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



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        initAdapter()

        viewModel.getNotificationList()
    }

    private fun initAdapter() {
        binding.rvToday.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvYesterday.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvRecentDays.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.rvToday.adapter = todayAdapter
        binding.rvYesterday.adapter = yesterdayAdapter
        binding.rvRecentDays.adapter = recentAdapter
    }

    private fun initData(data : NotificationResponse){
        todayAdapter.addData(data.today)
        yesterdayAdapter.addData(data.yesterday)
        recentAdapter.addData(data.recentDays)
    }

    private fun setObservers() {
        viewModel.notificationList.observe(viewLifecycleOwner){data ->
            if (data == null){
                binding.tvToday.visibility = View.INVISIBLE
                binding.tvYesterday.visibility = View.INVISIBLE
                binding.tvRecentDays.visibility = View.INVISIBLE
                return@observe
            }
            initData(data)
            viewModel.updateReadCount()
        }

        viewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}