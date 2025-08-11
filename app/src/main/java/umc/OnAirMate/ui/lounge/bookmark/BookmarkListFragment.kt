package umc.onairmate.ui.lounge.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.databinding.FragmentBookmarkListBinding

@AndroidEntryPoint
class BookmarkListFragment : Fragment() {

    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel: BookmarkViewModel by viewModels()
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
                //bookmarkViewModel.createRoomWithBookmark(bookmark)
            }

            override fun deleteBookmark(bookmark: BookmarkData) {
                bookmarkViewModel.deleteBookmark(bookmark.bookmarkId)
            }

            override fun moveCollection(bookmark: BookmarkData) {
                // todo: 팝업 띄워서 어떤 컬렉션으로 보낼지 선택해야함
                //ookmarkViewModel.moveCollectionOfBookmark(bookmark.bookmarkId)
            }
        })

        val dummy = BookmarkListResponse(uncategorizedBookmarkList, allBookmarkList)
        val bookmarkList = dummy
        binding.rvBookmarks.visibility = View.VISIBLE
        binding.emptyBookmarkLayout.visibility = View.GONE
        adapter.initData(bookmarkList.uncategorized, bookmarkList.all)
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
}

val uncategorizedBookmarkList = listOf(
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = null,
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = null,
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = null,
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    )
)
val allBookmarkList = listOf(
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    ),
    BookmarkData(
        bookmarkId = 1,
        collectionTitle = "고양이좋아",
        createdAt = "",
        message = "16:32 너무너무 귀여워서 우뜩해",
        timeline = 1632,
        videoThumbnail = "https://marketplace.canva.com/8-1Kc/MAGoQJ8-1Kc/1/tl/canva-ginger-cat-with-paws-raised-in-air-MAGoQJ8-1Kc.jpg",
        videoTitle = "고양이만세하다"
    )
)