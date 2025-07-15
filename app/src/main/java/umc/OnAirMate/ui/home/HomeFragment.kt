package umc.onairmate.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.databinding.FragmentHomeBinding
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick

class HomeFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val list : ArrayList<String> = arrayListOf()
        for(i in 1..5) list.add("room${i}")

        val adapter = RoomRVAdapter(list) {info ->
            showJoinRoomPopup(info)
        }
        binding.rvContinuingRooms.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvContinuingRooms.adapter = adapter

        binding.rvOnairRooms.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvOnairRooms.adapter = adapter

        setUpObserver()
        setUpObserver()
        setSearchSpinner()
        initClickListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObserver(){
        searchViewModel.testData.observe(viewLifecycleOwner){ list ->
            if (list == null) return@observe
            if (list.isEmpty()) {
                binding.layoutOnAir.visibility = View.GONE
                binding.rvOnairRooms.visibility = View.GONE

                binding.layoutEmpty.visibility = View.VISIBLE
                searchViewModel.getRecommendedVideo()
            }else{
                binding.layoutOnAir.visibility = View.VISIBLE
                binding.rvOnairRooms.visibility = View.VISIBLE

                binding.layoutContinuing.visibility = View.GONE
                binding.rvContinuingRooms.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE


                val adapter = RoomRVAdapter(list) {info ->
                    showJoinRoomPopup(info)
                }
                binding.rvOnairRooms.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.rvOnairRooms.adapter = adapter
            }
        }

        searchViewModel.recommendedVideo.observe(viewLifecycleOwner) {videos ->
            if(videos == null) return@observe
            recommendVideo(videos)
        }
    }


    // 상단 버튼들
    private fun initClickListener(){
        binding.ivYoutubeSearch.setOnClickListener {
            searchViewModel.getRoomList(0)
        }

        binding.ivNotification.setOnClickListener {
            searchViewModel.getRoomList(1)
        }
    }

    // 방 검색 드롭다운 조절
    private fun setSearchSpinner(){
        val searchTypeList = listOf("영상 제목", "방 제목", "방장 닉네임")
        val searchAdapter = SearchTypeSpinnerAdapter(requireContext(),searchTypeList)
        binding.spinnerSearchType.adapter = searchAdapter
        binding.spinnerSearchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                Log.d(TAG,"selected Item ${selectedItem}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // 방 참여 팝업 띄우기
    private fun showJoinRoomPopup(info : String){
        val dialog = JoinRoomPopup(info, object : PopupClick {
            override fun rightClickFunction() {

            }

        })
        dialog.show(activity?.supportFragmentManager!!, "JoinRoomPopup")
    }


    // 검색결과 없을 경우 추천 영상 띄우기
    private fun recommendVideo(videoList: List<String>){
        binding.tvContinue.text = "추천영상"
        binding.layoutContinuing.visibility = View.VISIBLE
        binding.rvContinuingRooms.visibility = View.VISIBLE

        val adapter = RecommendedVideoRVAdapter(videoList){ info ->
            Log.d(TAG,"추천 영상 클릭")
        }
        binding.rvContinuingRooms.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvContinuingRooms.adapter = adapter
    }
}