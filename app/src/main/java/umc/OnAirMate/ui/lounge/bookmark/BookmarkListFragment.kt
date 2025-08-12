package umc.onairmate.ui.lounge.bookmark

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
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.request.MoveCollectionRequest
import umc.onairmate.data.model.request.RoomStartOption
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.databinding.FragmentBookmarkListBinding
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
        // setUpObserver()

        return binding.root
    }

    private fun initScreen() {
        bookmarkViewModel.getBookmarks(null, false)
    }

    private fun setAdapter() {
        adapter = BookmarkRVAdapter(object : BookmarkEventListener {
            override fun createRoomWithBookmark(bookmark: BookmarkData) {
                // todo: 방 생성 팝업 띄워서 방 만들기
                showCreateRoomPopup(bookmark)
            }

            override fun deleteBookmark(bookmark: BookmarkData) {
                bookmarkViewModel.deleteBookmark(bookmark.bookmarkId)
                Toast.makeText(context, "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun moveCollection(bookmark: BookmarkData) {
                // todo: 팝업 띄워서 어떤 컬렉션으로 보낼지 선택해야함
                showMoveCollectionPopup(bookmark)
            }
        })

        val dummy = BookmarkListResponse(uncategorizedBookmarkList, allBookmarkList)
        val bookmarkList = dummy
        // 북마크 리스트가 비어있는 경우 비어있는 화면 보여주기
        if (bookmarkList.uncategorized.isEmpty() and bookmarkList.all.isEmpty()) {
            binding.rvBookmarks.visibility = View.GONE
            binding.emptyBookmarkLayout.visibility = View.VISIBLE
        } else {
            binding.rvBookmarks.visibility = View.VISIBLE
            binding.emptyBookmarkLayout.visibility = View.GONE

            adapter.initData(bookmarkList.uncategorized, bookmarkList.all)
        }

        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.rvBookmarks.adapter = adapter
    }

    private fun setUpObserver() {
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner) { data ->
            val emptyList = BookmarkListResponse(emptyList<BookmarkData>(), emptyList<BookmarkData>())

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
    }

    // 어디서 시작할지 팝업 띄우기
    // 근데 한 북마크에 메시지 여러개하면 북마크부터 시작이 어디서부터 시작인데
    private fun showSetStartingPoint() {

    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data: BookmarkData){

        val dialog = CreateRoomPopup(null, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                val requestBody = CreateRoomWithBookmarkRequest(
                    roomName = body.roomName,
                    maxParticipants = body.maxParticipants,
                    isPrivate = body.isPrivate,
                    startFrom = RoomStartOption.BEGINNING.apiName
                )

                // 북마크로 방 생성 api 호출
                bookmarkViewModel.createRoomWithBookmark(data.bookmarkId, requestBody)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateRoomPopup")
        }
    }

    private fun showMoveCollectionPopup(data: BookmarkData) {
        collectionViewModel.getCollections()

        collectionViewModel.collectionList.observe(viewLifecycleOwner) { list ->
            val collectionList = list ?: emptyList()

            val dialog = CollectionMoveDialog(collectionList, {
                bookmarkViewModel.moveCollectionOfBookmark(data.bookmarkId, MoveCollectionRequest(it))
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "CollectionMoveDialog")
            }
        }
    }
}

val uncategorizedBookmarkList = listOf(
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = null,
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 2,
        collectionTitle = null,
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 3,
        collectionTitle = null,
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해", "16:32 코멘트가 기이이이이일어지면 어떻게 되나요 두줄이 되나요?"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    )
)
val allBookmarkList = listOf(
    BookmarkData(
        bookmarkId = 4,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 5,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 6,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 7,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 8,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = listOf("16:32 너무너무 귀여워서 우뜩해", "16:32 너무너무 귀여워서 우뜩해"),
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    )
)