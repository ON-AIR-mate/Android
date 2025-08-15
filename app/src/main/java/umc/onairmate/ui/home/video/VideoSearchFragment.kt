package umc.onairmate.ui.home.video

import android.content.Context
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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.databinding.FragmentVideoSearchBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup

@AndroidEntryPoint
class VideoSearchFragment : Fragment() {

    private lateinit var binding: FragmentVideoSearchBinding
    private val searchRoomViewModel: HomeViewModel by viewModels()
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
        // 엔터누르면 입력 왼료되도록
        binding.etInputKeyword.setOnEditorActionListener{v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                // 검색
                val input = binding.etInputKeyword.text.toString()
                if (input.isNotEmpty()) {
                    searchVideoViewModel.searchVideoList(input, 10)
                }

                v.clearFocus() // 포커스 제거
                return@setOnEditorActionListener true
            }
            else return@setOnEditorActionListener  false
        }
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

        searchVideoViewModel.createdRoomInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            // 방 정보 받아오기
            searchRoomViewModel.getRoomDetailInfo(data.roomId)
            searchVideoViewModel.clearCreatedRoomInfo()
        }

        // 채팅방 화면 열기
        searchRoomViewModel.roomDetailInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            else {
                (activity?.supportFragmentManager?.findFragmentByTag("CreateRoomPopup")
                        as? androidx.fragment.app.DialogFragment
                        )?.dismissAllowingStateLoss()
                // 방 액티비티로 전환
                val intent = Intent(requireActivity(), ChatRoomLayoutActivity::class.java).apply {
                    putExtra("room_data", data)
                }
                startActivity(intent)
            }
            searchRoomViewModel.clearJoinRoom()
            searchRoomViewModel.clearRoomDetailInfo()
        }
    }



    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data : VideoData){
        Log.d("Check", "showCreateRoomPopup called")

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

    override fun onDestroy() {
        super.onDestroy()
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
    }

}