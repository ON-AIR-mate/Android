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
import umc.onairmate.databinding.FragmentFriendListTabBinding
import umc.onairmate.ui.friend.FriendViewModel
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

            override fun acceptRequest() {
                // TODO("Not yet implemented")
            }

        })
        binding.rvFriendList.adapter = adapter
        binding.rvFriendList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}