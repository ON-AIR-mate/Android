package umc.onairmate.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.FragmentRecentRoomsBinding

class RecentRoomsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: BookmarkAdapter
    private lateinit var binding: FragmentRecentRoomsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentRoomsBinding.inflate(inflater, container, false)
        return binding.root



//        // 어댑터에 리스너(this) 주입
//        adapter = BookmarkAdapter(actionListener = this)
//        recyclerView.adapter = adapter
//
//        // 더미 데이터 (어댑터가 Bookmark를 받도록 가정)
//        val dummyList: List<BookmarkData> = listOf(
//            BookmarkData(
//                bookmarkId = 1,
//                collectionTitle = null,
//                createdAt = "2025-08-12T00:00:00",
//                message = "영상 제목 1",
//                timeline = 12*60 + 34,    // 12:34 -> 754(초) 같은 규칙으로
//                videoThumbnail = "" ,// 썸네일 URL 없으면 빈문자
//                videoTitle = ""
//            ),
//            BookmarkData(
//                bookmarkId = 2,
//                collectionTitle = null,
//                createdAt = "2025-08-12T00:00:00",
//                message = "영상 제목 2",
//                timeline = 5*60 + 21,
//                videoThumbnail = "",
//                videoTitle = ""
//            ),
//            BookmarkData(
//                bookmarkId = 3,
//                collectionTitle = null,
//                createdAt = "2025-08-12T00:00:00",
//                message = "영상 제목 3",
//                timeline = 23*60 + 59,
//                videoThumbnail = "",
//                videoTitle = " "
//            )
//        )
//
//        adapter.submitList(dummyList)
//
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 다른 초기화 코드...
    }

//
//    // ===== OnBookmarkActionListener 구현 =====
//    override fun onDeleteBookmark(bookmark: BookmarkData) {
//        Toast.makeText(requireContext(), "삭제: ${bookmark.message}", Toast.LENGTH_SHORT).show()
//        // TODO: 실제 삭제 로직 + submitList 갱신
//    }
//
//    override fun onMoveBookmark(bookmark: BookmarkData) {
//        Toast.makeText(requireContext(), "이동: ${bookmark.message}", Toast.LENGTH_SHORT).show()
//        // TODO: 이동 다이얼로그/네비게이션 처리
//    }
}