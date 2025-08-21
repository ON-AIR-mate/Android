package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentPersonalLoungeBinding // Fragment용 바인딩 클래스 사용
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.ui.lounge.personal.PersonalCollectionAdapter
import umc.onairmate.ui.lounge.personal.PersonalCollectionViewModel
import umc.onairmate.ui.lounge.personal.PersonalLoungeLayoutActivity
@AndroidEntryPoint
class PersonalLoungeActivity : AppCompatActivity() {

    // Fragment용 바인딩 클래스를 그대로 사용
    private lateinit var binding: FragmentPersonalLoungeBinding
    private val vm: PersonalCollectionViewModel by viewModels()
    private lateinit var adapter: PersonalCollectionAdapter
    private var friendList: List<FriendData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fragment의 레이아웃 파일을 액티비티에 직접 설정
        binding = FragmentPersonalLoungeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API 호출
        vm.fetchCollections()
        vm.fetchFriends()

        // RecyclerView 초기화
        val adapter = PersonalCollectionAdapter(object : PersonalCollectionAdapter.OnCollectionActionListener {
            override fun onItemClick(item: CollectionData) {
                // 아이템 클릭 시 실행될 코드
                Log.d("PersonalLoungeFragment", "아이템 클릭: ${item.collectionId}")
                // TODO: 예를 들어, 상세 화면으로 이동하는 코드를 작성하세요.
            }

            override fun onMoreClick(view: View, item: CollectionData) {
                // '더보기' 버튼 클릭 시 실행될 코드
                Log.d("PersonalLoungeFragment", "더보기 클릭: ${item.collectionId}")
                // TODO: 팝업 메뉴를 띄우는 코드를 작성하세요.
            }
        })
        binding.rvCollections.layoutManager = LinearLayoutManager(this)
        binding.rvCollections.adapter = adapter

        // LiveData 관찰 로직
        bindObservers()
    }

    private fun bindObservers() {
        vm.collections.observe(this) { list ->
            adapter.submitList(list)
        }

        vm.friends.observe(this) { friends ->
            friendList = friends
        }

        vm.shareResponse.observe(this) { result ->
            when (result) {
                is DefaultResponse.Success -> Log.d("Activity", "컬렉션 공유 성공")
                is DefaultResponse.Error -> Log.e("Activity", "컬렉션 공유 실패: ${result.message}")
            }
        }

        vm.importResponse.observe(this) { result ->
            when (result) {
                is DefaultResponse.Success -> Log.d("Activity", "컬렉션 가져오기 성공")
                is DefaultResponse.Error -> Log.e("Activity", "컬렉션 가져오기 실패: ${result.message}")
            }
        }
    }
}