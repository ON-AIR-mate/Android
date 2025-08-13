package umc.onairmate.ui.lounge.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.request.ShareCollectionRequest
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.lounge.collection.create.CollectionCreateDialog
import umc.onairmate.ui.lounge.collection.create.CreateCollectionCallback
import umc.onairmate.ui.lounge.collection.send.CollectionShareDialog
import umc.onairmate.ui.util.SharedPrefUtil

@AndroidEntryPoint
class CollectionListFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter
    private val collectionViewModel: CollectionViewModel by viewModels()
    private val friendViewModel: FriendViewModel by viewModels()

    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        initAdapter()
        setClickListener()
        initObserver()

        return binding.root
    }

    fun initAdapter() {
        collectionViewModel.getCollections()

        adapter = CollectionRVAdapter(object : CollectionEventListener {
            override fun deleteCollection(collection: CollectionData) {
                // todo: 컬렉션 삭제 팝업 띄워서 삭제하기
            }
            override fun shareCollection(collection: CollectionData) {
                showShareDialog(collection)
            }

            override fun clickCollectionItem(collection: CollectionData) {
                // todo: parentFragment를 detail 뷰로 교체하기
            }
        })
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.adapter = adapter

        binding.emptyCollectionLayout.visibility = View.GONE
        binding.rvCollections.visibility = View.VISIBLE
        adapter.submitList(collections) // 임시 데이터
    }

    fun setClickListener() {
        binding.llCreateCollection.setOnClickListener {
            showCreateCollectionPopup()
        }
    }

    fun initObserver() {
        collectionViewModel.collectionList.observe(viewLifecycleOwner) { data ->
            val collections = data.collections ?: emptyList()

            if (collections.isEmpty()) {
                binding.emptyCollectionLayout.visibility = View.VISIBLE
                binding.rvCollections.visibility = View.GONE
            } else {
                binding.emptyCollectionLayout.visibility = View.GONE
                binding.rvCollections.visibility = View.VISIBLE

                adapter.submitList(collections)
            }
        }
    }

    private fun showCreateCollectionPopup() {
        val dialog = CollectionCreateDialog(object : CreateCollectionCallback {
            override fun onCreateCollection(requestData: CreateCollectionRequest) {
                collectionViewModel.createCollection(requestData)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateCollectionPopup")
        }
    }


    private fun showShareDialog(collectionData: CollectionData) {
        friendViewModel.getFriendList()

        friendViewModel.friendList.observe(viewLifecycleOwner) { list ->
            val friendList = list ?: emptyList()

            val dialog = CollectionShareDialog(friendList, { friend ->
                val request = ShareCollectionRequest(
                    listOf(user.userId, friend.userId)
                )

                collectionViewModel.shareCollection(collectionData.collectionId ,request)
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "ShareCollectionPopup")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

val collections = listOf(
    CollectionData(
        bookmarkCount = 14,
        collectionId = 1,
        coverImage = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        createdAt = "2025.08.12",
        description = "그럼 싫으세요?",
        title = "고양이가 좋은 이유",
        updatedAt = "2030.80.30",
        visibility = "PRIVATE"
    ),
    CollectionData(
        bookmarkCount = 41,
        collectionId = 1,
        coverImage = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        createdAt = "2025-01-01T12:00:00Z",
        description = "그럼 싫으세요?",
        title = "고양이가 좋은 이유",
        updatedAt = "3000.80.30",
        visibility = "FRIENDS_ONLY"
    ),
    CollectionData(
        bookmarkCount = 20,
        collectionId = 1,
        coverImage = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        createdAt = "2025.08.42",
        description = "그럼 싫으세요?",
        title = "고양이가 좋은 이유",
        updatedAt = "3000.80.30",
        visibility = "PUBLIC"
    ),
)