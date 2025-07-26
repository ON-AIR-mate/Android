package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.model.BookmarkSection
import umc.onairmate.model.VideoItem
import umc.OnAirMate.adapter.OuterAdapter
import umc.onairmate.databinding.FragmentLoungeBinding


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

        // 리사이클러뷰 설정
        binding.bookmarkRecyclerView.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(requireContext())
            adapter = OuterAdapter(sampleSections)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

