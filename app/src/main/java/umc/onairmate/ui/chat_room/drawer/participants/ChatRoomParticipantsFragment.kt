package umc.onairmate.ui.chat_room.drawer.participants

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.FragmentChatRoomParticipantsBinding
import umc.onairmate.databinding.PopupParticipantOptionsBinding
import umc.onairmate.ui.chat_room.ChatVideoViewModel
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.pop_up.TwoButtonPopup
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.SharedPrefUtil

// 채팅방 참가자 목록 프래그먼트
@AndroidEntryPoint
class ChatRoomParticipantsFragment : Fragment() {

    private val chatRoomViewModel: ChatVideoViewModel by activityViewModels()
    private val videoChatViewModel: VideoChatViewModel by activityViewModels()
    private val friendViewModel: FriendViewModel by activityViewModels()

    lateinit var binding: FragmentChatRoomParticipantsBinding

    private lateinit var adapter: ChatRoomParticipantRVAdapter
    var roomData: RoomData? = null
    private var hostData : ParticipantData? = null
    private val user = SharedPrefUtil.getData("user_info") ?: UserData()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomParticipantsBinding.inflate(layoutInflater)

        roomData = arguments?.getParcelable("room_data", RoomData::class.java)
        Log.d("data", "room : ${roomData}")

        initScreen()
        setupAdapter()
        setParticipants()

        return binding.root
    }

    fun initScreen() {
        // 방 참가자 목록 api 호출
        chatRoomViewModel.getParticipantDataInfo(roomData!!.roomId)

        // host 정보 주입
        binding.itemRoomManager.tvUserNickname.text = roomData!!.hostNickname
        binding.itemRoomManager.tvUserTier.text = roomData!!.hostPopularity.toString()
        if (user.nickname == roomData!!.hostNickname) {
            // host일 경우 host item 더보기 버튼 빼기
            binding.itemRoomManager.ivMore.visibility = View.GONE
        } else {
            // host가 아닐 경우 host item 더보기 버튼-팝업 연결
            binding.itemRoomManager.ivMore.setOnClickListener { showPopupMenu( binding.itemRoomManager.ivMore, hostData!! ) }
        }

        NetworkImageLoader.profileLoad(binding.itemRoomManager.ivUserProfile, roomData!!.hostProfileImage)
    }

    // 방 참가자 리사이클러뷰 어댑터 설정
    fun setupAdapter() {
        adapter = ChatRoomParticipantRVAdapter( object : ParticipantItemClickListener {
            // 팝업 - 신고 클릭
            override fun clickReport(data: ParticipantData) {
                val text = data.nickname+"님을 신고하시겠습니까?"
                val textList = listOf(text,"예","아니오")
                showPopup(text =textList, left = {
                    Toast.makeText(requireContext(),"신고 접수 되었습니다", Toast.LENGTH_SHORT).show()
                }, right = {} )
            }

            // 팝업 - 추천하기 클릭
            override fun clickRecommend(data: ParticipantData) {
                Toast.makeText(requireContext(),"${data.nickname}님을 추천했습니다.", Toast.LENGTH_SHORT).show()
            }

            // 팝업 - 친구 추가 클릭
            override fun clickAddFriend(data: ParticipantData) {
                friendViewModel.requestFriend(data.userId)
            }

            // 팝업 - 차단하기 클릭
            override fun clickBlock(data: ParticipantData) {
                val text = data.nickname+"님을 차단하시겠습니까?"
                val textList = listOf(text,"예","아니오")
                showPopup(text =textList, left = {
                    Toast.makeText(requireContext(),"${data.nickname}님을 차단했습니다.", Toast.LENGTH_SHORT).show()
                }, right = {} )
            }
        })

        // 뷰와 어댑터 연결
        binding.rvParticipants.adapter = adapter
        binding.rvParticipants.layoutManager = LinearLayoutManager(context)
    }

    // 방 참가자 명단과 어댑터 연결
    private fun setParticipants() {
        // 초기 userList 삽입
        chatRoomViewModel.participantDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe

            val userList = data.filter { !it.isHost } ?: emptyList()
            hostData = data.firstOrNull { it.isHost }

            adapter.submitList(userList)
        }

        // 업데이트된 userList 삽입
        videoChatViewModel.userLeftDataInfo.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data.roomParticipants.filter { !it.isHost })
        }

        friendViewModel.result.observe(viewLifecycleOwner){data ->
            if (data == null) return@observe
            Toast.makeText(requireContext(),data, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopup(text : List<String>, right : ()-> Unit?, left: () -> Unit?) {
        val dialog = TwoButtonPopup(text,object : PopupClick{
            override fun rightClickFunction() { right() }
            override fun leftClickFunction() {
                left()
            }
        }, false)
        dialog.show(activity?.supportFragmentManager!!, "ChatRoomParticipantsPopup")
    }

    // 방장 뷰에 대한 팝업 메뉴
    private fun showPopupMenu(anchorView: View, data: ParticipantData){
        val popupBinding = PopupParticipantOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // popupBinding root 크기 측정 후 정렬 위치 계산
        popupBinding.root.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        val popupWidth = popupBinding.root.measuredWidth

        // 오른쪽 정렬: anchor 오른쪽 끝 기준
        val offsetX = -popupWidth + anchorView.width
        val offsetY = 0

        // 클릭 리스너 연결
        popupBinding.tvReport.setOnClickListener {
            val text = data.nickname+"님을 신고하시겠습니까?"
            val textList = listOf(text,"예","아니오")
            showPopup(text =textList, left = {
                Toast.makeText(requireContext(),"신고 접수 되었습니다", Toast.LENGTH_SHORT).show()
            }, right = {} )
            popupWindow.dismiss()
        }
        popupBinding.tvRecommend.setOnClickListener {
            Toast.makeText(requireContext(),"${data.nickname}님을 추천했습니다.", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }
        popupBinding.tvAddFriend.setOnClickListener {
            friendViewModel.requestFriend(data.userId)
            popupWindow.dismiss()
        }
        popupBinding.tvBlock.setOnClickListener {
            val text = data.nickname+"님을 차단하시겠습니까?"
            val textList = listOf(text,"예","아니오")
            showPopup(text =textList, left = {
                Toast.makeText(requireContext(),"${data.nickname}님을 차단했습니다.", Toast.LENGTH_SHORT).show()
            }, right = {} )
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
    }
}
