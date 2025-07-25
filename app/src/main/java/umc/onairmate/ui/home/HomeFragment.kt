package umc.onairmate.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentHomeBinding
import umc.onairmate.ui.home.room.HomeEventListener
import umc.onairmate.ui.home.room.RoomRVAdapter
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick

class HomeFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: RoomRVAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setView()
        setUpObserver()
        setSearchSpinner()
        initClickListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 초기 데이터 삽입
        searchViewModel.getRoomList(1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 리사이클러뷰 adapter설정
    private fun setView() {
        adapter = RoomRVAdapter(requireContext(), object : HomeEventListener{
            override fun joinRoom(data : RoomData){
                showJoinRoomPopup(data)
            }

            override fun selectSortType(type: String) {
                Log.d(TAG, "정렬 기준 : ${type}")
            }
        })
        binding.rvContents.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvContents.adapter = adapter
    }


    // viewHolder속 데이터 변화 감지
    private fun setUpObserver(){
        searchViewModel.roomData.observe(viewLifecycleOwner){ list ->
            if (list == null) return@observe
            if (list.isEmpty()) {
                searchViewModel.getRecommendedVideo()
                binding.layoutEmpty.visibility = View.VISIBLE
            }else{
                binding.layoutEmpty.visibility = View.GONE
            }
            adapter.initData(list,list)
        }
        searchViewModel.recommendedVideo.observe(viewLifecycleOwner) {videos ->
            if(videos == null) return@observe
            recommendVideo(videos)
        }
    }

    // 상단 버튼들
    // 일단 데이터 변화를 테스트 하는데 활용 -> 나중에 Intent로 변환할 예정
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
    private fun showJoinRoomPopup(data : RoomData){
        val dialog = JoinRoomPopup(data, object : PopupClick {
            override fun rightClickFunction() {
                // 방 액티비티로 전환
            }

        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "JoinRoomPopup")
        }
    }


    // 검색결과 없을 경우 추천 영상 띄우기
    private fun recommendVideo(videoList: List<String>){
        val videoAdapter = RecommendedVideoRVAdapter(videoList){ _ ->
            Log.d(TAG,"추천 영상 클릭")
        }
        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvVideos.adapter =  videoAdapter
    }
}