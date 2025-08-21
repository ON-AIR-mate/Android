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
import androidx.fragment.app.commit
import umc.onairmate.data.model.entity.RoomData

@AndroidEntryPoint
class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentPersonalLoungeBinding? = null
    // 1. 변수명을 binding으로 변경하여 가독성 향상
    private val binding get() = _binding!!

    // viewModel 초기화
    private val vm: PersonalCollectionViewModel by viewModels()
    lateinit var friendData: FriendData
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

        // API 연결
        vm.fetchCollections() // 컬렉션 목록을 가져오는 함수를 호출
        vm.fetchFriends() // ⭐️ 친구 목록도 함께 가져오도록 API 호출


        // LiveData 관찰 로직
        bindObservers()

    }

    private fun bindObservers() {
        // 컬렉션 목록을 LiveData로 관찰
        vm.collections.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
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