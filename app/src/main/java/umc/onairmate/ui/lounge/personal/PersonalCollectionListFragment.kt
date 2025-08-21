// PersonalCollectionListFragment.kt
package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentPersonalCollectionListBinding
import umc.onairmate.ui.lounge.personal.dialog.PersonalLoungeImportDialog
import umc.onairmate.ui.lounge.personal.dialog.PersonalLoungePrivacyInfoDialog
import umc.onairmate.ui.lounge.personal.dialog.PersonalShareListDialog
import android.widget.TextView

class PersonalCollectionListFragment : Fragment() {

    private var _binding: FragmentPersonalCollectionListBinding? = null
    private val binding get() = _binding!!

    private val vm: PersonalCollectionViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: PersonalCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalCollectionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // API 연결
        vm.fetchCollections()

        // 어댑터 및 리사이클러뷰 설정
        setupRecyclerView()

        // 데이터 관찰 및 바인딩
        bindObservers()

    }

    private fun setupRecyclerView() {
        // PersonalLoungeFragment에서 가져온 리스너를 그대로 사용
        adapter = PersonalCollectionAdapter(object : PersonalCollectionAdapter.OnCollectionActionListener {
            override fun onItemClick(item: CollectionData) {
                // 상세로 이동 등
            }

            override fun onMoreClick(anchor: View, item: CollectionData) {
                val inflater = LayoutInflater.from(requireContext())
                val popupView = inflater.inflate(umc.onairmate.R.layout.popup_personal_collection_item, null)

                // ID 오류 수정
//                popupView.findViewById<TextView>(umc.onairmate.R.id.tv_import_collection).setOnClickListener {
//                    val dialog = PersonalLoungeImportDialog(item.collectionId) { collectionId ->
//                        vm.importToMyCollection(collectionId)
//                    }
//                    dialog.show(childFragmentManager, "ImportDialog")
//                }
                popupView.findViewById<TextView>(umc.onairmate.R.id.tv_move_collection).setOnClickListener {
                    when (item.visibility) {
                        "전체 공개" -> showShareDialog(item)
                        "친구만 공개", "나만 보기" -> {
                            val privacyDialog = PersonalLoungePrivacyInfoDialog {
                                showShareDialog(item)
                            }
                            privacyDialog.show(childFragmentManager, "PrivacyDialog")
                        }
                    }
                }
            }
        })

        // XML의 RecyclerView id에 맞게 binding.rvPersonalCollections로 접근
        binding.rvCollections.adapter = adapter
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showShareDialog(item: CollectionData) {
        // PersonalLoungeFragment에 정의된 friendList를 가져와야 함
        // vm을 통해 부모 프래그먼트의 LiveData에 접근
        val friendList = vm.friends.value ?: emptyList()

        if (friendList.isNotEmpty()) {
            val shareDialog = PersonalShareListDialog(item.collectionId, friendList) { collectionId, friendIds ->
                vm.shareToMyCollection(collectionId, friendIds)
            }
            shareDialog.show(childFragmentManager, "ShareDialog")
        } else {
            // TODO: 친구 목록이 없을 때 사용자에게 알리는 로직 추가
        }
    }

    private fun bindObservers() {
        // ViewModel을 통해 컬렉션 목록을 관찰
        vm.collections.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}