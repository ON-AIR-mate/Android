package umc.onairmate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.databinding.FragmentHomeBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.home.room.HomeEventListener
import umc.onairmate.ui.home.room.RoomRVAdapter
import umc.onairmate.ui.home.video.SearchVideoViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: RoomRVAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private val searchVideoViewModel: SearchVideoViewModel by viewModels()

    private var sortBy : String = "latest"
    private var searchType : String = "videoTitle"
    private var keyword : String = ""
    private var searchRunnable: Runnable? = null
    private val searchHandler = Handler(Looper.getMainLooper())

    private lateinit var roomData: RoomData

    private var roomFlag : Boolean = false
    private var videoFlag : Boolean = false

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
        setTextListener()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 초기 데이터 삽입
        homeViewModel.getRoomList(sortBy, searchType, keyword)
        Log.d(TAG,"Resume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTextListener(){
        binding.etInputKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val input = binding.etInputKeyword.text.toString()
                    homeViewModel.getRoomList(sortBy, searchType, input)
                }
                searchHandler.postDelayed(searchRunnable!!, 300) // 300ms 디바운스
            }
        })

        //
        binding.etInputKeyword.setOnEditorActionListener{v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                v.clearFocus() // 포커스 제거
                homeViewModel.getRoomList(sortBy, searchType, binding.etInputKeyword.text.toString())
                return@setOnEditorActionListener true
            }
            else return@setOnEditorActionListener  false
        }
    }


    // 리사이클러뷰 adapter설정
    private fun setView() {
        adapter = RoomRVAdapter(requireContext(), object : HomeEventListener{
            override fun joinRoom(data : RoomData){
                homeViewModel.getRoomDetailInfo(data.roomId)
            }

            override fun selectSortType(type: String) {
                Log.d(TAG, "정렬 기준 : ${type}")
                sortBy = type
                homeViewModel.getRoomList(sortBy = sortBy, searchType = searchType, keyword = keyword)
            }
        })
        binding.rvContents.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvContents.adapter = adapter
    }


    // viewModel 속 데이터 변화 감지
    private fun setUpObserver(){
        homeViewModel.roomListResponse.observe(viewLifecycleOwner){response ->
            if (response == null) return@observe
            if (response.continueWatching.isEmpty() && response.onAirRooms.isEmpty()){
                binding.layoutEmpty.visibility = View.VISIBLE
                val input = binding.etInputKeyword.text.toString()
                if (input.isBlank())
                    binding.layoutRecommendVideo.visibility = View.GONE
                else
                    searchVideoViewModel.getRecommendVideoList(input, 10)

            }
            else {
                binding.layoutEmpty.visibility = View.GONE
            }
            adapter.initData(response.continueWatching, response.onAirRooms)
        }

        homeViewModel.roomDetailInfo.observe(viewLifecycleOwner){data ->
            if (data == null) return@observe
            roomData = data
            showJoinRoomPopup()
            homeViewModel.clearRoomDetailInfo()
        }
        homeViewModel.smallLoading.observe(viewLifecycleOwner){ smallLoading ->
            binding.progressbarSmall.visibility = if (smallLoading) View.VISIBLE else View.GONE
        }

        searchVideoViewModel.recommendedVideos.observe(viewLifecycleOwner) {videos ->
            if(videos == null) return@observe
            binding.layoutRecommendVideo.visibility = View.VISIBLE
            recommendVideo(videos)
        }
        searchVideoViewModel.videoDetailInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            showCreateRoomPopup(data)
        }

        homeViewModel.joinRoom.observe(viewLifecycleOwner){ data ->
            if (data == null) return@observe
            (activity?.supportFragmentManager?.findFragmentByTag("JoinRoomPopup")
                    as? androidx.fragment.app.DialogFragment
                    )?.dismissAllowingStateLoss()
            if(data){
                // 방 액티비티로 전환
                val intent = Intent(requireActivity(), ChatRoomLayoutActivity::class.java).apply {
                    putExtra("room_data", roomData)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(),"방 참여에 실패했습니다.\n다시시도 해주세요", Toast.LENGTH_SHORT).show()
            }
            homeViewModel.clearJoinRoom()

        }
        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            roomFlag = isLoading
            checkRefresh()
        })
        searchVideoViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            videoFlag = isLoading
            checkRefresh()
        })
    }
    private fun checkRefresh() {
        val isLoading = roomFlag || videoFlag
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // 상단 버튼들
    private fun initClickListener(){
        binding.ivYoutubeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search_video)
        }

        binding.ivNotification.setOnClickListener {

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
                searchType = when(position) {
                    0 -> "videoTitle"
                    1 -> "roomTitle"
                    2 -> "hostNickname"
                    else -> ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // 방 참여 팝업 띄우기
    private fun showJoinRoomPopup(){
        val dialog = JoinRoomPopup(roomData, object : PopupClick {
            override fun rightClickFunction() {
                homeViewModel.joinRoom(roomData.roomId)
            }

        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "JoinRoomPopup")
        }
    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data : VideoData){
        searchVideoViewModel.clearVideoDetailInfo()

        val dialog = CreateRoomPopup(data, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                // 방 생성 api 호출
                searchVideoViewModel.createRoom(body)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateRoomPopup")
        }
    }


    // 검색결과 없을 경우 추천 영상 띄우기
    private fun recommendVideo(videoList: List<VideoData>){
        val videoAdapter = RecommendedVideoRVAdapter(videoList){ data ->
            searchVideoViewModel.getVideoDetailInfo(data.videoId)
        }
        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvVideos.adapter =  videoAdapter
    }
}