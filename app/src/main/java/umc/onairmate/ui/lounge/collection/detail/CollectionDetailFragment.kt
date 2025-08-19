package umc.onairmate.ui.lounge.collection.detail

import android.content.Intent
import android.os.Bundle
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
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.request.RoomWithBookmarkCreateRequest
import umc.onairmate.data.model.request.CollectionModifyRequest
import umc.onairmate.data.model.request.CollectionMoveRequest
import umc.onairmate.data.model.request.RoomStartOption
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel
import umc.onairmate.ui.lounge.bookmark.move.CollectionMoveDialog
import umc.onairmate.ui.lounge.bookmark.select.BookmarkSelectDialog
import umc.onairmate.ui.lounge.collection.CollectionViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup

// 컬렉션 세부 화면 프래그먼트
@AndroidEntryPoint
class CollectionDetailFragment : Fragment() {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    private var collectionId: Int = 0
    private lateinit var adapter: CollectionDetailRVAdapter

    private val collectionViewModel: CollectionViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val searchRoomViewModel: HomeViewModel by viewModels()

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

        bookmarkViewModel.createdRoomDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            else {
                (activity?.supportFragmentManager?.findFragmentByTag("SelectBookmarkPopup")
                        as? androidx.fragment.app.DialogFragment
                        )?.dismissAllowingStateLoss()
                searchRoomViewModel.getRoomDetailInfo(data.roomId)
            }
        }
        searchRoomViewModel.roomDetailInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            else {
                (activity?.supportFragmentManager?.findFragmentByTag("CreateRoomPopup")
                        as? androidx.fragment.app.DialogFragment
                        )?.dismissAllowingStateLoss()

                // 방 액티비티로 전환
                val intent = Intent(requireActivity(), ChatRoomLayoutActivity::class.java).apply {
                    putExtra("room_data", data)
                }
                startActivity(intent)
            }
            searchRoomViewModel.clearJoinRoom()
            searchRoomViewModel.clearRoomDetailInfo()
        }

        // 컬렉션 정보 + 북마크 리사이클러뷰 어댑터 연결
        collectionViewModel.collectionDetailDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                Toast.makeText(context, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@observe
            }

            adapter = CollectionDetailRVAdapter(
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
                    override fun onTitleModifyClicked() {
                        showCollectionEditTitlePopup(data)
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
                        showSelectBookmarkPopup(roomArchiveData, { selectedBookmark ->
                            showCreateRoomPopup(selectedBookmark, roomArchiveData)
                        })
                    }

                    override fun deleteBookmark(roomArchiveData: RoomArchiveData) {
                        showSelectBookmarkPopup(roomArchiveData, { selectedBookmark ->
                            bookmarkViewModel.deleteBookmark(selectedBookmark.bookmarkId)
                            Toast.makeText(context, "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        })
                    }

                    override fun moveCollection(roomArchiveData: RoomArchiveData) {
                        showSelectBookmarkPopup(roomArchiveData, { selectedBookmark ->
                            showMoveCollectionPopup(selectedBookmark)
                        })
                    }
                }
            )

            adapter.initData(data)
            binding.rvBookmarks.adapter = adapter
        }

    }

    // 컬렉션 제목 변경
    private fun showCollectionEditTitlePopup(data: CollectionDetailData){

        val dialog = CollectionEditTitleDialog(
            data.title,
            { modifiedTitle ->
                collectionViewModel.modifyCollection(
                    collectionId,
                    request = CollectionModifyRequest(
                        title = modifiedTitle ?: "",
                        description = data.description,
                        visibility = data.visibility
                    )
                )
            }
        )
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CollectionEditTitlePopup")
        }
    }

    // 어디서 시작할지 팝업 띄우기
    private fun showSelectBookmarkPopup(roomArchiveData: RoomArchiveData, afterAction: (BookmarkData) -> Unit) {
        val dialog = BookmarkSelectDialog(roomArchiveData.bookmarks, { selectedItem ->
            afterAction(selectedItem)
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "SelectBookmarkPopup")
        }
    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data: BookmarkData, roomArchiveData: RoomArchiveData){
        val videoData = VideoData(
            channelName = "",
            thumbnail = roomArchiveData.roomData.videoThumbnail ?: "",
            title = roomArchiveData.roomData.videoTitle,
            uploadTime = "",
            viewCount = 0L
        )

        val dialog = CreateRoomPopup(videoData, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                val requestBody = RoomWithBookmarkCreateRequest(
                    roomTitle = body.roomName,
                    maxParticipants = body.maxParticipants,
                    isPrivate = body.isPrivate,
                    startFrom = RoomStartOption.BEGINNING.apiName
                )

                // 북마크로 방 생성 api 호출
                bookmarkViewModel.createRoomWithBookmark(data.bookmarkId, requestBody)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "SelectBookmarkPopup")
        }
    }

    // 컬렉션 이동 팝업 띄우기
    private fun showMoveCollectionPopup(bookmarkData: BookmarkData) {
        collectionViewModel.getCollections()

        (activity?.supportFragmentManager?.findFragmentByTag("SelectBookmarkPopup")
                as? androidx.fragment.app.DialogFragment
                )?.dismissAllowingStateLoss()

        collectionViewModel.collectionList.observe(viewLifecycleOwner) { data ->
            val collectionList = data.collections ?: emptyList()

            val dialog = CollectionMoveDialog(collectionList, { collection ->
                bookmarkViewModel.moveCollectionOfBookmark(bookmarkData.bookmarkId, CollectionMoveRequest(collection.collectionId))
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "CollectionMoveDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
