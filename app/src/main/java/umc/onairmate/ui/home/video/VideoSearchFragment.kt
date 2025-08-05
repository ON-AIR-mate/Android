package umc.onairmate.ui.home.video

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.databinding.FragmentVideoSearchBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.chat_room.drawer.ChatRoomParticipantRVAdapter
import umc.onairmate.ui.home.SearchRoomViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick

@AndroidEntryPoint
class VideoSearchFragment : Fragment() {

    private lateinit var binding: FragmentVideoSearchBinding
    private val searchRoomViewModel: SearchRoomViewModel by viewModels()
    private val searchVideoViewModel: SearchVideoViewModel by viewModels()
    private lateinit var adapter: SearchedVideoRVAdapter

    private var searchRunnable: Runnable? = null
    private val searchHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoSearchBinding.inflate(layoutInflater)

        setTextListener()
        setVideo()

        return binding.root
    }

    private fun setTextListener(){
        binding.etInputKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val input = binding.etInputKeyword.text.toString()
                    // todo: 동영상 리미트 몇으로 하지 - 무한로딩이 낫지 않을까..?
                    searchVideoViewModel.searchVideoList(input, 10)
                }
                searchHandler.postDelayed(searchRunnable!!, 300) // 300ms 디바운스
            }
        })
    }

    private fun setVideo() {
        searchVideoViewModel.searchedVideos.observe(viewLifecycleOwner) { data ->
            val videoList = data ?: emptyList()
            Log.d("VideoSearch", "Video list size: ${videoList.size}")

            // 어댑터가 이미 연결되어 있다면 데이터만 갱신
            if (::adapter.isInitialized) {
                adapter.submitList(videoList)
            } else {
                adapter = SearchedVideoRVAdapter(object : SearchVideoEventListener {
                    override fun createRoom(data: VideoData) {
                        Log.d("Check", "createRoom called. videoId: ${data.videoId}")
                        searchVideoViewModel.getVideoDetailInfo(data.videoId)
                    }
                })
                binding.rvContents.adapter = adapter
                binding.rvContents.layoutManager = LinearLayoutManager(context)
                adapter.submitList(videoList)
            }

            // 비어있을 때 검색 유도 UI 보여주기
            if (videoList.isEmpty()) {
                binding.rvContents.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.rvContents.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            }
        }
        searchVideoViewModel.videoDetailInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            Log.d("Check", "observe triggered with data: $data")
            showCreateRoomPopup(data)
            searchVideoViewModel.clearVideoDetailInfo()
         }
    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data : VideoData){
        Log.d("Check", "showCreateRoomPopup called")
        val dialog = CreateRoomPopup(data, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                // 방 생성 api 호출
                searchVideoViewModel.createRoom(body)
                val roomId = searchVideoViewModel.createdRoomInfo.value!!.roomId

                // 방 정보 받아와 채팅방 화면 열기
                searchRoomViewModel.getRoomDetailInfo(roomId)
                searchRoomViewModel.roomDetailInfo.observe(viewLifecycleOwner) { data ->
                    val roomData = data ?: RoomData()

                    val intent = Intent(requireActivity(), ChatRoomLayoutActivity::class.java).apply {
                        putExtra("room_data", roomData)
                    }
                    startActivity(intent)
                }
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateRoomPopup")
        }
    }

}