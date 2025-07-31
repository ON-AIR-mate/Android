package umc.onairmate.ui.friend.list

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.FriendData
import umc.onairmate.data.RequestedFriendData
import umc.onairmate.databinding.FragmentFriendListTabBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.pop_up.TwoButtonPopup
import kotlin.getValue

@AndroidEntryPoint
class FriendListTabFragment() : Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentFriendListTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : FriendListRVAdapter
    private val viewModel: FriendViewModel by viewModels()

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

        const val LIST_TYPE = 0
        const val REQUEST_TYPE = 1
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

    private fun initData(){
        if (type == LIST_TYPE) viewModel.getFriendList()
        if (type == REQUEST_TYPE) viewModel.getRequestedFriendList()
    }

    private fun setObservers() {
        viewModel.friendList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            adapter.initFriendList(list)
        })

        viewModel.requestedFriendList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            adapter.initRequestList(list)
        })
    }

    private fun setAdapter(){
        adapter = FriendListRVAdapter(requireContext())
        adapter.setItemClickListener(object: FriendListRVAdapter.ItemClickListener{
            override fun clickMessage() {
                // TODO("Not yet implemented")
            }

            override fun clickMore() {
                // TODO("Not yet implemented")
            }

            override fun acceptRequest(data: RequestedFriendData) {
                val text = data.nickname+"님의 친구요청을 수락하시겠습니까?"
                val textList = listOf(text,"수락","거절")
                val dialog = TwoButtonPopup(textList,object : PopupClick{
                    override fun rightClickFunction() {
                        // 실행하고자 하는 함수 있으면 overriding

                    }

                    override fun leftClickFunction() {

                    }
                }, false) // 뒤로 가기 막고 싶으면 false 넣어주세요, 아니면 생략가능합니다.
                dialog.show(activity?.supportFragmentManager!!, "FriendAcceptPopup")
            }

        })
        binding.rvFriendList.adapter = adapter
        binding.rvFriendList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    // 팝업창 실행할 곳에 넣을 함수 예시 (팝업 띄울 위치에 복붙해서 사용하면 됩니다!
    private fun popup() {

    }

}