package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.ui.lounge.adapter.BookmarkAdapter
import umc.onairmate.ui.lounge.adapter.OnBookmarkActionListener

class RecentRoomsFragment : Fragment(), OnBookmarkActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_recent_rooms, container, false)

        recyclerView = view.findViewById(R.id.rv_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 어댑터에 리스너(this) 주입
        adapter = BookmarkAdapter(actionListener = this)
        recyclerView.adapter = adapter

        // 더미 데이터 (어댑터가 Bookmark를 받도록 가정)
        val dummyList: List<BookmarkData> = listOf(
            BookmarkData(
                bookmarkId = 1,
                collectionTitle = null,
                createdAt = "2025-08-12T00:00:00",
                message = "영상 제목 1",
                timeline = 12*60 + 34,    // 12:34 -> 754(초) 같은 규칙으로
                videoThumbnail = "" ,// 썸네일 URL 없으면 빈문자
                videoTitle = ""
            ),
            BookmarkData(
                bookmarkId = 2,
                collectionTitle = null,
                createdAt = "2025-08-12T00:00:00",
                message = "영상 제목 2",
                timeline = 5*60 + 21,
                videoThumbnail = "",
                videoTitle = ""
            ),
            BookmarkData(
                bookmarkId = 3,
                collectionTitle = null,
                createdAt = "2025-08-12T00:00:00",
                message = "영상 제목 3",
                timeline = 23*60 + 59,
                videoThumbnail = "",
                videoTitle = " "
            )
        )

        adapter.submitList(dummyList)

        return view
    }

    // ===== OnBookmarkActionListener 구현 =====
    override fun onDeleteBookmark(bookmark: BookmarkData) {
        Toast.makeText(requireContext(), "삭제: ${bookmark.message}", Toast.LENGTH_SHORT).show()
        // TODO: 실제 삭제 로직 + submitList 갱신
    }

    override fun onMoveBookmark(bookmark: BookmarkData) {
        Toast.makeText(requireContext(), "이동: ${bookmark.message}", Toast.LENGTH_SHORT).show()
        // TODO: 이동 다이얼로그/네비게이션 처리
    }
}
