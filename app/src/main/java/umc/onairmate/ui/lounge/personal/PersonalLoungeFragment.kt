package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import android.widget.PopupWindow
import android.widget.TextView
import umc.onairmate.databinding.FragmentPersonalLoungeBinding
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.CollectionData // CollectionData import
import umc.onairmate.data.model.response.DefaultResponse // DefaultResponse import
import umc.onairmate.ui.lounge.personal.dialog.PersonalLoungeImportDialog // 새로 만든 다이얼로그 import
import umc.onairmate.ui.lounge.personal.dialog.PersonalShareListDialog // 새로 만든 다이얼로그 import
import umc.onairmate.ui.lounge.personal.dialog.PersonalLoungePrivacyInfoDialog

@AndroidEntryPoint
class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentPersonalLoungeBinding? = null
    // 1. 변수명을 binding으로 변경하여 가독성 향상
    private val binding get() = _binding!!

    // viewModel 초기화
    private val vm: CollectionViewModel by viewModels()

    private lateinit var adapter: PersonalCollectionAdapter

    // 친구 목록 데이터를 저장할 변수
    private var friendList: List<FriendData> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalLoungeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        bindObservers()

        // API 연결
        vm.fetchCollections() // 컬렉션 목록을 가져오는 함수를 호출
        vm.fetchFriends() // ⭐️ 친구 목록도 함께 가져오도록 API 호출
    }

    private fun setupRecyclerView() {
        adapter = PersonalCollectionAdapter(object : PersonalCollectionAdapter.OnCollectionActionListener {
            override fun onItemClick(item: CollectionData) { // 2. CollectionData 타입으로 수정
                // 상세로 이동 등
                // findNavController().navigate(...)
            }

            override fun onMoreClick(anchor: View, item: CollectionData) { // 2. CollectionData 타입으로 수정

                // 1. popup_personal_collection_item.xml 레이아웃을 인플레이트
                val inflater = LayoutInflater.from(requireContext())
                val popupView = inflater.inflate(R.layout.popup_personal_collection_item, null)

                // 2. PopupWindow 생성
                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true // 외부 클릭 시 팝업 닫기 가능하도록 설정
                )

                // 3. 팝업 내 버튼에 클릭 리스너 연결
                // ID는 레이아웃 파일에 맞게 수정해주세요.
                popupView.findViewById<TextView>(R.id.tv_import_colletion).setOnClickListener {
                    // "내 라운지에 가져오기" 클릭 시 다이얼로그 띄우기
                    val dialog = PersonalLoungeImportDialog(item.id) { collectionId ->
                        vm.importToMyCollection(collectionId)
                    }
                    dialog.show(childFragmentManager, "ImportDialog")
                    popupWindow.dismiss() // 팝업 닫기
                }

                popupView.findViewById<TextView>(R.id.tv_move_collection).setOnClickListener {
                    // "공유하기" 클릭 시 다이얼로그 띄우기
                    when (item.visibility) {
                        "전체 공개" -> showShareDialog(item)
                        "친구만 공개", "나만 보기" -> {
                            val privacyDialog = PersonalLoungePrivacyInfoDialog {
                                showShareDialog(item)
                            }
                            privacyDialog.show(childFragmentManager, "PrivacyDialog")
                        }
                    }
                    popupWindow.dismiss() // 팝업 닫기
                }

                // 4. anchor(더보기) 버튼 아래에 팝업 윈도우 표시
                popupWindow.showAsDropDown(anchor)
            }
        })

        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCollections.adapter = adapter
    }

    private fun showShareDialog(item: CollectionData) {
        if (friendList.isNotEmpty()) {
            val shareDialog = PersonalShareListDialog(item.id, friendList) { collectionId, friendIds ->
                vm.shareToMyCollection(collectionId, friendIds)
            }
            shareDialog.show(childFragmentManager, "ShareDialog")
        } else {
            // TODO: 친구 목록이 없을 때 사용자에게 알리는 로직 추가
        }
    }

    private fun bindObservers() {
        // 컬렉션 목록을 LiveData로 관찰
        vm.collections.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            // 비어있을 때 empty view 토글
            binding.emptyGroup.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        // ⭐️ 친구 목록을 LiveData로 관찰하고 변수에 저장
        vm.friends.observe(viewLifecycleOwner) { friends ->
            friendList = friends
        }

        // API 호출 결과(공유)를 LiveData로 관찰
        vm.shareResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DefaultResponse.Success -> Log.d("Fragment", "컬렉션 공유 성공")
                is DefaultResponse.Error -> Log.e("Fragment", "컬렉션 공유 실패: ${result.message}")
            }
        }

        // API 호출 결과(가져오기)를 LiveData로 관찰
        vm.importResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DefaultResponse.Success -> Log.d("Fragment", "컬렉션 가져오기 성공")
                is DefaultResponse.Error -> Log.e("Fragment", "컬렉션 가져오기 실패: ${result.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}