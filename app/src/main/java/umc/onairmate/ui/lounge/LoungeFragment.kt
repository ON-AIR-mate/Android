package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.BookmarkSection
import umc.onairmate.data.model.entity.VideoItem
import umc.onairmate.ui.lounge.adapter.OuterAdapter
import umc.onairmate.databinding.FragmentLoungeBinding
import umc.onairmate.R
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import android.widget.Button
import umc.onairmate.ui.lounge.model.Collection as LoungeCollection


@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private var _binding: FragmentLoungeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoungeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 샘플 데이터
        val sampleSections = listOf(
            BookmarkSection(
                sectionTitle = "정리되지 않은 북마크",
                videos = listOf(
                    VideoItem(
                        thumbnailUrl = "https://via.placeholder.com/100",
                        title = "방 제목 1",
                        host = "영상 제목 1",
                        time = "16:23"
                    ),
                    VideoItem(
                        thumbnailUrl = "https://via.placeholder.com/100",
                        title = "방 제목 2",
                        host = "영상 제목 2",
                        time = "16:24"
                    )
                )
            )
        )

        // 데이터가 없을 경우 emptyView 보여주기
        if (sampleSections.isEmpty() || sampleSections.all { it.videos.isEmpty() }) {
            binding.bookmarkRecyclerView.visibility = View.GONE
            binding.emptyBookmarkView.visibility = View.VISIBLE
        } else {
            binding.bookmarkRecyclerView.visibility = View.VISIBLE
            binding.emptyBookmarkView.visibility = View.GONE

            // 리사이클러뷰 설정
            binding.bookmarkRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = OuterAdapter(sampleSections)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteConfirmationDialog(
        collection: LoungeCollection,
        onDeleteClick: (LoungeCollection) -> Unit
    ) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_collection, null)


        builder.setView(dialogView)
        val dialog = builder.create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvCollectionTitle)
        val tvCreated = dialogView.findViewById<TextView>(R.id.tvCreatedDate)
        val tvUpdated = dialogView.findViewById<TextView>(R.id.tvUpdatedDate)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        tvTitle.text = collection.title
        tvCreated.text = "생성일 : ${collection.dateCreated}"
        tvUpdated.text = "마지막 수정일 : ${collection.lastUpdated}"

        btnDelete.setOnClickListener {
            // 실제 삭제 로직 호출
            onDeleteClick(collection)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

