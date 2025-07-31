package umc.onairmate.ui.friend.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.FragmentSearchFriendTabBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.pop_up.TwoButtonPopup
import kotlin.getValue

@AndroidEntryPoint
class SearchFriendTabFragment: Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentSearchFriendTabBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendViewModel by viewModels()
    lateinit private var adapter : SearchUserRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFriendTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = SearchUserRVAdapter() { data ->
            val text = data.nickname+"님에게 친구요청을 보내시겠습니까?"
            val textList = listOf(text,"예","아니오")
            val dialog = TwoButtonPopup(textList,object : PopupClick{
                override fun rightClickFunction() {
                    // 실행하고자 하는 함수 있으면 overriding

                }

                override fun leftClickFunction() {
                    viewModel.requestFriend(data.userId)
                }
            }, false) // 뒤로 가기 막고 싶으면 false 넣어주세요, 아니면 생략가능합니다.
            dialog.show(activity?.supportFragmentManager!!, "requestFriendPopup")
        }
        binding.rvUserList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.rvUserList.adapter = adapter

        setTextListener()
        setObservers()

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchUser("")

    }


    private fun setObservers() {
        viewModel.searchedUserList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            adapter.initData(list)
            binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        })

    }

    private fun setTextListener(){
        binding.etInputNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val input = binding.etInputNickname.text.toString()
                viewModel.searchUser(input)
                Log.d(TAG,"input : ${input}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}