package umc.onairmate.ui.friend.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
    private lateinit var adapter : SearchUserRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFriendTabBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initAdapter()
        setTextListener()
        setObservers()

        return root
    }

    override fun onResume() {
        super.onResume()
        binding.etInputNickname.setText("")   // 입력창 초기화

    }

    // 리사이클러뷰 설정
    private fun initAdapter(){
        adapter = SearchUserRVAdapter { data  ->
            val text = data.nickname + "님에게 친구요청을 보내시겠습니까?"
            val textList = listOf(text, "예", "아니오")
            val dialog = TwoButtonPopup(textList, object : PopupClick {
                override fun leftClickFunction() {
                    Log.d(TAG,"requestFriend ${data}")
                    viewModel.requestFriend(data.userId)
                }
            }, false) // 뒤로가기 막을거면 false
            dialog.show(activity?.supportFragmentManager!!, "requestFriendPopup")
        }
        binding.rvUserList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.rvUserList.adapter = adapter
    }


    private fun setObservers() {
        viewModel.searchedUserList.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            adapter.initData(list)
            binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        })
        viewModel.result.observe(viewLifecycleOwner, Observer { message ->
            if (message == null) return@Observer
            viewModel.searchUser(binding.etInputNickname.text.toString())
            Log.d(TAG,"test ${message}")
            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            viewModel.clearResult()
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

    }

    private fun setTextListener(){
        // 엔터누르면 입력 왼료되도록
        binding.etInputNickname.setOnEditorActionListener{v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                // 검색
                val query = binding.etInputNickname.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchUser(query)
                }

                v.clearFocus() // 포커스 제거
                return@setOnEditorActionListener true
            }
            else return@setOnEditorActionListener  false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}