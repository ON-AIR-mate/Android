package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.data.model.entity.RoomArchiveData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.request.RoomWithBookmarkCreateRequest
import umc.onairmate.data.model.request.CollectionModifyRequest
import umc.onairmate.data.model.request.CollectionMoveRequest
import umc.onairmate.data.model.request.RoomStartOption
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel
import umc.onairmate.ui.lounge.bookmark.move.CollectionMoveDialog
import umc.onairmate.ui.lounge.collection.CollectionViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup
import umc.onairmate.ui.lounge.collection.detail.CollectionDetailEventListener


// 컬렉션 세부 화면 프래그먼트
@AndroidEntryPoint
class PersonalCollectionDetailFragment : Fragment() {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    private var collectionId: Int = 0
    private lateinit var adapter: PersonalCollectionDetailRVAdapter

    private val collectionViewModel: CollectionViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailBinding.inflate(inflater, container, false)
        collectionId = arguments?.getInt("collectionId")!!

        setAdapter()
        setupObserver()
        setupRefreshObserver()

        return binding.root
    }

    private fun setAdapter() {
        collectionViewModel.getCollectionDetailInfo(collectionId)
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setupRefreshObserver() {
    }

    private fun setupObserver() {
        collectionViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // 컬렉션 정보 + 북마크 리사이클러뷰 어댑터 연결
        collectionViewModel.collectionDetailDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                Toast.makeText(context, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@observe
            }

            adapter = PersonalCollectionDetailRVAdapter(false,
                object : CollectionDetailEventListener {
                    override fun onVisibilitySelected(selectedVisibility: String) {
                        collectionViewModel.modifyCollection(
                            collectionId,
                            request = CollectionModifyRequest(
                                title = data.title,
                                description = data.description ?: "",
                                visibility = CollectionVisibility.fromDisplayName(selectedVisibility)!!.apiName
                            )
                        )
                    }
                    override fun onDescriptionModified(input: String) {
                        collectionViewModel.modifyCollection(
                            collectionId,
                            request = CollectionModifyRequest(
                                title = data.title,
                                description = input ?: "",
                                visibility = data.visibility
                            )
                        )
                    }
                },
                object : BookmarkEventListener {
                    override fun createRoomWithBookmark(roomArchiveData: RoomArchiveData) {
                        //showCreateRoomPopup(bookmark)
                    }

                    override fun deleteBookmark(roomArchiveData: RoomArchiveData) {
                        //bookmarkViewModel.deleteBookmark(bookmark.bookmarkId)
                        Toast.makeText(context, "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun moveCollection(roomArchiveData: RoomArchiveData) {
                        //showMoveCollectionPopup(bookmark)
                    }
                }
            )

            adapter.initData(data)
            binding.rvBookmarks.adapter = adapter
        }

    }


}
