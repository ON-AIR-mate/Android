package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.R
import umc.onairmate.databinding.FragmentLoungeCollectionBinding
import umc.onairmate.ui.lounge.model.Collection
import umc.onairmate.ui.lounge.adapter.CollectionAdapter
import umc.onairmate.ui.lounge.pop_up.CollectionCreateDialogFragment

enum class LoungeTab { ALL, COLLECTION }
private var currentTab = LoungeTab.COLLECTION

class LoungeCollectionFragment : Fragment() {

    private var _binding: FragmentLoungeCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoungeCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 예시 데이터
        val collections = listOf(
            Collection("웃긴 장면", "2025.03.24", "2025.06.23", "비공개", ""),
            Collection("감동 모음", "2025.01.10", "2025.04.01", "공유하기", " ")
        )

        collectionAdapter = CollectionAdapter(
            onDeleteClick = { selectedItem -> },
            onShareClick = { selectedItem -> },
            collectionList = collections,
            onMoreClick = { selectedItem -> }
        )

        binding.rvCollection.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = collectionAdapter
        }

        // 탭 버튼 클릭 이벤트
        binding.btnAllList.setOnClickListener {
            currentTab = LoungeTab.ALL
            updateUI()
        }

        binding.btnCollectionList.setOnClickListener {
            currentTab = LoungeTab.COLLECTION
            updateUI()
        }

        // 컬렉션 생성 다이얼로그
        binding.iconPlus.setOnClickListener {
            val dialog = CollectionCreateDialogFragment()
            dialog.show(parentFragmentManager, "CollectionCreateDialog")
        }

        // 초기 화면 설정
        updateUI()
    }

    private fun updateUI() {
        when (currentTab) {
            LoungeTab.ALL -> {
                binding.rvBookmark.visibility = View.GONE
                binding.emptyBookmarkLayout.visibility = View.VISIBLE
                binding.rvCollection.visibility = View.GONE
                binding.emptyCollectionLayout.visibility = View.GONE
            }

            LoungeTab.COLLECTION -> {
                val isEmpty = collectionAdapter.itemCount == 0
                binding.rvCollection.visibility = if (isEmpty) View.GONE else View.VISIBLE
                binding.emptyCollectionLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.rvBookmark.visibility = View.GONE
                binding.emptyBookmarkLayout.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
