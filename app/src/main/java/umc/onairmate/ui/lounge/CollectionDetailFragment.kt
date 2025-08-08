package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.R
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.lounge.adapter.BookmarkAdapter
import umc.onairmate.ui.lounge.adapter.MoveCollectionAdapter
import umc.onairmate.ui.lounge.adapter.OnBookmarkActionListener
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.CollectionData

class CollectionDetailFragment : Fragment(), OnBookmarkActionListener {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    private val bookmarkAdapter by lazy { BookmarkAdapter(this) }
    private val dummyBookmarks = mutableListOf(
        Bookmark(R.drawable.sample_image, "방 제목", "16:23 ~~~~", "5분전"),
        Bookmark(R.drawable.sample_image, "또다른 제목", "17:00 ~~~~", "5분전")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookmarkAdapter
        }

        bookmarkAdapter.submitList(dummyBookmarks.toList())
    }

    override fun onDeleteBookmark(bookmark: Bookmark) {
        showDeleteDialog(bookmark)
    }

    override fun onMoveBookmark(bookmark: Bookmark) {
        showMoveDialog(bookmark)
    }

    private fun showDeleteDialog(bookmark: Bookmark) {
        val dialogView = layoutInflater.inflate(
            R.layout.dialog_confirm_delete_bookmark,
            null
        )
        val titleTv = dialogView.findViewById<TextView>(R.id.titleText)
        val timeTv = dialogView.findViewById<TextView>(R.id.timeText)
        val deleteBtn = dialogView.findViewById<Button>(R.id.btnDelete)
        val cancelBtn = dialogView.findViewById<Button>(R.id.btnCancel)

        titleTv.text = "[${bookmark.title}]"
        timeTv.text = bookmark.time

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.show()

        deleteBtn.setOnClickListener {
            // 삭제 로직
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showMoveDialog(bookmark: Bookmark) {
        val dialogView = layoutInflater.inflate(
            R.layout.dialog_move_collection,
            null
        )
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvCollections)
        val moveBtn = dialogView.findViewById<Button>(R.id.btnMove)

        // 전체 Collection 객체를 생성 (필수 필드 모두 채움)

        val collections = listOf(
            CollectionData(
                title = "웃긴 장면",
                dateCreated = "2025.03.24",
                lastUpdated = "2025.03.24",
                privacy = "전체 공개",
                thumbnailUrl = "https://example.com/thumb1.jpg",
            ),
            CollectionData(
                title = "힐링 모먼트",
                dateCreated = "2025.02.10",
                lastUpdated = "2025.02.10",
                privacy = "친구만 공개",
                thumbnailUrl = "https://example.com/thumb2.jpg"
            ),
            CollectionData(
                title = "눈물 주의",
                dateCreated = "2025.01.05",
                lastUpdated = "2025.01.05",
                privacy = "전체 공개",
                thumbnailUrl = "https://example.com/thumb3.jpg"
            )
        )


        var targetCollection: CollectionData? = null
4

        rv.layoutManager = LinearLayoutManager(requireContext())
        val moveAdapter = MoveCollectionAdapter { selected: CollectionData ->
            targetCollection = selected
        }
        rv.adapter = moveAdapter
        moveAdapter.submitList(collections)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.show()

        moveBtn.setOnClickListener {
            targetCollection?.let {
                // 이동 처리
                dialog.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
