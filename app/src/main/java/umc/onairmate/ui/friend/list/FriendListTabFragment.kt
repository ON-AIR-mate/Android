package umc.onairmate.ui.friend.list

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.FragmentFriendListTabBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.friend.chat.FriendChatActivity
import umc.onairmate.ui.friend.chat.FriendChatViewModel
import umc.onairmate.ui.lounge.personal.PersonalLoungeLayoutActivity
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.pop_up.TwoButtonPopup
import umc.onairmate.ui.util.SharedPrefUtil
import kotlin.getValue

@AndroidEntryPoint
class FriendListTabFragment() : Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentFriendListTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : FriendListRVAdapter
    private val viewModel: FriendViewModel by viewModels()
    private val friendChatViewModel: FriendChatViewModel by viewModels()

    private var user : UserData = UserData()
    private var friendId : Int = 0
    private var type : Int = 0

    companion object {
        private const val ARG_POSITION = "arg_position"

        fun newInstance(position: Int): FriendListTabFragment {
            return FriendListTabFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }

        const val LIST_TYPE = 0 // 친구 리스트 조회
        const val REQUEST_TYPE = 1 // 받은 요청 리스트 조회

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListTabBinding.inflate(inflater, container, false)
        val root: View = binding.root
        type = arguments?.getInt(ARG_POSITION) ?: 0

        setAdapter()
        setObservers()

        return root
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 선택된 탭에 따라 다른 api 호출
    private fun initData(){
        if (type == LIST_TYPE) viewModel.getFriendList()
        if (type == REQUEST_TYPE) viewModel.getRequestedFriendList()
        user = SharedPrefUtil.getData("user_info")?: UserData()
    }

    private fun setObservers() {
        viewModel.friendList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            binding.tvEmptyMessage.text = "아직 친구가 없습니다.\n함께할 친구를 초대해보세요!"
            binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            adapter.initFriendList(list)
        })

        viewModel.requestedFriendList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            binding.tvEmptyMessage.text = "받은 친구 요청이 아직 없어요."
            binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            adapter.initRequestList(list)
        })

        viewModel.result.observe(viewLifecycleOwner, Observer { message ->
            if (message == null) return@Observer
            if (message == "친구가 삭제되었습니다.") friendChatViewModel.deleteFriend(friendId,user.userId)
            initData()
            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            viewModel.clearResult()
        })

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setAdapter(){
        adapter = FriendListRVAdapter(requireContext())
        adapter.setItemClickListener(object: FriendItemClickListener{

            // 1대1 채팅 화면 이동
            override fun clickMessage(data: FriendData) {
                val bundle = Bundle().apply {
                    putParcelable("friendData", data)
                }
                parentFragmentManager.setFragmentResult("open_friend_chat_activity", bundle)
            }

            // 받은 요청 처리
            override fun acceptRequest(data: RequestedFriendData) {
                val text = data.nickname+"님의 친구요청을 수락하시겠습니까?"
                val textList = listOf(text,"수락","거절")
                showPopup(
                    text =textList,
                    left = { viewModel.acceptFriend(data.requestId, "ACCEPT")},
                    right = {  viewModel.acceptFriend(data.requestId, "REJECT") })
            }

            // 친구 콜렉션 방문
            override fun clickCollection(data: FriendData) {
                // 인탠트 필요

                // 1. 새 Activity로 이동하기 위한 Intent 생성
                // Intent의 첫 번째 인자는 현재 컨텍스트(context), 두 번째 인자는 이동할 Activity의 클래스입니다.
                val intent = Intent(context, PersonalLoungeLayoutActivity::class.java).apply {
                    // 2. 새 Activity에 데이터 전달
                    // putExtra()를 사용하여 데이터를 key-value 쌍으로 전달합니다.
                    // 여기서는 친구의 ID(userId)를 "FRIEND_ID"라는 키로 전달합니다.
                    putExtra("FRIEND_ID", data.userId)
                }

                // 3. Intent를 사용하여 새 Activity 시작
                startActivity(intent)
            }

            // 삭제하기
            override fun clickDelete(data: FriendData) {
                val text = data.nickname+"님을 친구 목록에서 삭제하시겠습니까?"
                val textList = listOf(text,"예","아니오")
                friendId = data.userId
                showPopup(text =textList, left = {
                    viewModel.deleteFriend(data.userId) }, right = {} )
            }

            // 차단하기
            override fun clickBlock(data: FriendData) {
                val text = data.nickname+"님을 차단하시겠습니까?"
                val textList = listOf(text,"예","아니오")
                showPopup(text =textList, left = {
                    Toast.makeText(requireContext(),"${data.nickname}님을 차단했습니다.", Toast.LENGTH_SHORT).show() }, right = {} )
            }

            // 신고하기
            override fun clickReport(data: FriendData) {
                val text = data.nickname+"님을 신고하시겠습니까?"
                val textList = listOf(text,"예","아니오")
                showPopup(text =textList, left = {
                    Toast.makeText(requireContext(),"신고 접수 되었습니다", Toast.LENGTH_SHORT).show()
                }, right = {} )
            }

        })
        binding.rvFriendList.adapter = adapter
        binding.rvFriendList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    // 2버튼 팝업 출력 
    private fun showPopup(text : List<String>, right : ()-> Unit?, left: () -> Unit?) {
        val dialog = TwoButtonPopup(text,object : PopupClick{
            override fun rightClickFunction() { right() }
            override fun leftClickFunction() {
                left()
            }
        }, false) // 뒤로 가기 막고 싶으면 false 넣어주세요, 아니면 생략가능합니다.
        dialog.show(activity?.supportFragmentManager!!, "FriendPopup")
    }

}