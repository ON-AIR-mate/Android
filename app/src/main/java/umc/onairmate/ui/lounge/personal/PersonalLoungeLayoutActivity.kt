package umc.onairmate.ui.lounge.personal

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentPersonalLoungeBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel

/**
 * PersonalLoungeLayoutActivity: 개인 라운지 화면의 메인 액티비티.
 *
 * 이 액티비티는 더 이상 Fragment를 담는 컨테이너가 아니며,
 * lounge 레이아웃의 뷰들을 직접 관리합니다.
 */

@AndroidEntryPoint
class PersonalLoungeLayoutActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    lateinit var friendData: FriendData
    private lateinit var binding: FragmentPersonalLoungeBinding

    private val sharedCollectionsViewModel: SharedCollectionsViewModel by viewModels()
    private val collectionViewModel: PersonalCollectionViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentPersonalLoungeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friendId : Int = intent.getIntExtra("FRIEND_ID", 0)

        // RecyclerView 설정
        val adapter = PersonalCollectionAdapter(object : PersonalCollectionAdapter.OnCollectionActionListener {
            override fun onItemClick(item: CollectionData) {
                // 아이템 전체를 클릭했을 때의 동작
                Log.d("PersonalLoungeLayout", "아이템 클릭: ${item.collectionId}")
            }

            override fun onMoreClick(view: View, item: CollectionData) {
                // '더보기' 버튼을 클릭했을 때의 동작
                Log.d("PersonalLoungeLayout", "더보기 클릭: ${item.collectionId}")
                // TODO: 팝업 메뉴를 띄우거나 다른 동작을 수행하는 코드를 여기에 작성하세요.
            }
        })

        sharedCollectionsViewModel.getFriendPublicCollections(friendId)

        // RecyclerView에 어댑터와 레이아웃 매니저를 설정합니다.
        binding.rvCollections.layoutManager = LinearLayoutManager(this)
        binding.rvCollections.adapter = adapter

        // LiveData 관찰 및 어댑터에 데이터 제출
        // 1. 개인 컬렉션 LiveData 관찰
        collectionViewModel.collections.observe(this) { collections ->
            // collections는 List<CollectionData> 타입이어야 합니다.
            adapter.submitList(collections)
        }

        // 2. 공유 컬렉션 LiveData 관찰 - 오류 수정
        // sharedCollectionsViewModel.sharedCollections의 LiveData는 List<CollectionData>를 방출해야 합니다.
        // 따라서 Observer의 람다 파라미터도 List<CollectionData> 타입으로 받도록 수정하고,
        // 변수명을 'collections'처럼 의미있게 변경했습니다.
        sharedCollectionsViewModel.sharedCollections.observe(this) { sharedCollectionsList ->
            // sharedCollectionsList는 List<CollectionData> 타입입니다.
            // 이 리스트를 adapter.submitList()에 전달합니다.
            adapter.submitList(sharedCollectionsList as MutableList<CollectionData>)
        }
    }

    private fun bindObservers() {
        // 이미 onCreate에서 옵저버를 설정했으므로, 이 메서드는 필요하지 않을 수 있습니다.
        // TODO: 만약 bindObservers()가 호출되는 다른 곳이 있다면 그에 맞게 코드를 수정하세요.
    }
}