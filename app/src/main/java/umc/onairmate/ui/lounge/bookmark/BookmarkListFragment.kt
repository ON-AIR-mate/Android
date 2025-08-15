package umc.onairmate.ui.lounge.bookmark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.RoomArchiveData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.request.MoveCollectionRequest
import umc.onairmate.data.model.request.RoomStartOption
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.databinding.FragmentBookmarkListBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.lounge.bookmark.move.CollectionMoveDialog
import umc.onairmate.ui.lounge.collection.CollectionViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup

@AndroidEntryPoint
class BookmarkListFragment : Fragment() {

    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val collectionViewModel: CollectionViewModel by viewModels()
    private val searchRoomViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: BookmarkRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkListBinding.inflate(layoutInflater)

        initScreen()
        setAdapter()
        setUpObserver()

        return binding.root
    }

    private fun initScreen() {
        bookmarkViewModel.getBookmarks(null, false)
    }

    private fun setAdapter() {
        adapter = BookmarkRVAdapter(object : BookmarkEventListener {
            override fun createRoomWithBookmark(roomArchiveData: RoomArchiveData) {
                // todo: 방 생성 팝업 띄워서 방 만들기
                roomArchiveData.bookmarks[0]?.let { showCreateRoomPopup(it, roomArchiveData) }
            }

            override fun deleteBookmark(roomArchiveData: RoomArchiveData) {
                //bookmarkViewModel.deleteBookmark(bookmark.bookmarkId)
                Toast.makeText(context, "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun moveCollection(roomArchiveData: RoomArchiveData) {
                // todo: 팝업 띄워서 어떤 컬렉션으로 보낼지 선택해야함
                roomArchiveData.bookmarks[0]?.let { showMoveCollectionPopup(it) }
            }
        })

        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.rvBookmarks.adapter = adapter
    }

    private fun setUpObserver() {
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner) { data ->
            val emptyList = BookmarkListResponse(emptyList<RoomArchiveData>(), emptyList<RoomArchiveData>())

            var bookmarkList = data ?: emptyList
            Log.d("북마크 리스트 확인", "북마크 리스트 ${data.uncategorized} / ${data.all}")

            // 북마크 리스트가 비어있는 경우 비어있는 화면 보여주기
            if (bookmarkList.uncategorized.isEmpty() and bookmarkList.all.isEmpty()) {
                binding.rvBookmarks.visibility = View.GONE
                binding.emptyBookmarkLayout.visibility = View.VISIBLE
            } else {
                binding.rvBookmarks.visibility = View.VISIBLE
                binding.emptyBookmarkLayout.visibility = View.GONE

                adapter.initData(bookmarkList.uncategorized, bookmarkList.all)
            }
        }
        bookmarkViewModel.createdRoomDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            else {
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
    }

    // 어디서 시작할지 팝업 띄우기
    // 근데 한 북마크에 메시지 여러개하면 북마크부터 시작이 어디서부터 시작인데
    private fun showSelectBookmarkPopup() {

    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data: BookmarkData, roomArchiveData: RoomArchiveData){
        val videoData = VideoData(
            channelName = "",
            thumbnail = roomArchiveData.roomData.videoThumbnail,
            title = roomArchiveData.roomData.videoTitle,
            uploadTime = "",
            viewCount = 0L
        )

        val dialog = CreateRoomPopup(videoData, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                val requestBody = CreateRoomWithBookmarkRequest(
                    roomTitle = body.roomName,
                    maxParticipants = body.maxParticipants,
                    isPrivate = body.isPrivate,
                    startFrom = RoomStartOption.BEGINNING.apiName
                )

                // 북마크로 방 생성 api 호출
                //bookmarkViewModel.createRoomWithBookmark(data.bookmarkId, requestBody)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateRoomPopup")
        }
    }

    private fun showMoveCollectionPopup(bookmark: BookmarkData) {
        collectionViewModel.getCollections()

        collectionViewModel.collectionList.observe(viewLifecycleOwner) { data ->
            val collectionList = data.collections ?: emptyList()

            val dialog = CollectionMoveDialog(collectionList, { collection ->
                //bookmarkViewModel.moveCollectionOfBookmark(bookmark.bookmarkId, MoveCollectionRequest(collection.collectionId))
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "CollectionMoveDialog")
            }
        }
    }
}