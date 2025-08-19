package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentPersonalLoungeBinding
import umc.onairmate.ui.lounge.personal.PersonalCollectionAdapter

@AndroidEntryPoint
class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentPersonalLoungeBinding? = null
    private val b get() = _binding!!

    private val vm: PersonalLoungeViewModel by viewModels()

    private lateinit var adapter: PersonalCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalLoungeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        bindObservers()

        // API 연결 전: 더미 로드 / 이후엔 vm.fetchCollections()로 교체
        vm.loadDummyForPreview()
    }

    private fun setupRecyclerView() {
        adapter = PersonalCollectionAdapter(object : PersonalCollectionAdapter.OnCollectionActionListener {
            override fun onItemClick(item: PersonalCollectionAdapter) {
                // 상세로 이동 등
                // findNavController().navigate(...)
            }

            override fun onMoreClick(anchor: View, item: PersonalCollectionAdapter) {

                PopupMenu(requireContext(), anchor).apply {
                    inflate(R.menu.popup_personal_collection_item) // "내 라운지에 가져오기", "공유하기" 등
                    setOnMenuItemClickListener { mi ->
                        when (mi.itemId) {
                            R.id.action_copy_to_my_lounge -> {
                                // vm.copyToMyLounge(item.id)
                                true
                            }
                            R.id.action_share -> {
                                // vm.shareCollection(item.id)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        })

        b.rvCollections.layoutManager = LinearLayoutManager(requireContext())
        b.rvCollections.adapter = adapter
    }

    private fun bindObservers() {
        vm.collections.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            // 비어있을 때 empty view 토글
            b.emptyGroup.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
