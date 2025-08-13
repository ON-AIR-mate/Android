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
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.lounge.adapter.MoveCollectionAdapter
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkRVAdapter


class CollectionDetailFragment : Fragment() {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkAdapter : BookmarkRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkAdapter = BookmarkRVAdapter(object : BookmarkEventListener {
            override fun createRoomWithBookmark(bookmark: BookmarkData) {
                super.createRoomWithBookmark(bookmark)
            }

            override fun deleteBookmark(bookmark: BookmarkData) {
                super.deleteBookmark(bookmark)
            }

            override fun moveCollection(bookmark: BookmarkData) {
                super.moveCollection(bookmark)
            }
        })
        bookmarkAdapter.submitList(emptyList())

        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookmarkAdapter
        }
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
        val rv = dialogView.findViewById<RecyclerView>(R.id.rv_collection_list)
        val moveBtn = dialogView.findViewById<Button>(R.id.tv_move_collection)



        var targetCollection: CollectionData? = null

        rv.layoutManager = LinearLayoutManager(requireContext())
        val moveAdapter = MoveCollectionAdapter { selected: CollectionData ->
            targetCollection = selected
        }
        rv.adapter = moveAdapter
        //moveAdapter.submitList(collections)

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
