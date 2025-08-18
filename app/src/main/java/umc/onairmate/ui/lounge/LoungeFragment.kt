package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentLoungeBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkListFragment
import umc.onairmate.ui.lounge.collection.CollectionListFragment

/**
 * LoungeFragment: '라운지' 화면의 진입점
 * - childFragment: BookmarkListFragment(전체 목록), CollectionListFragment(컬렉션 목록)
 */
@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private var _binding: FragmentLoungeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoungeBinding.inflate(inflater, container, false)

        initScreen()
        initListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initScreen() {
        // 접근시 전체 목록 화면 지정
        selectButton(binding.tvAllList)
        childFragmentManager.beginTransaction()
            .replace(R.id.lounge_content_container, BookmarkListFragment())
            .commit()
    }

    fun initListener() {
        // 전체 목록 버튼 선택시
        binding.tvAllList.setOnClickListener {
            selectButton(it)
            childFragmentManager.beginTransaction()
                .replace(R.id.lounge_content_container, BookmarkListFragment())
                .commit()
        }
        // 컬렉션별 목록 버튼 선택시
        binding.tvCollectionList.setOnClickListener {
            selectButton(it)
            childFragmentManager.beginTransaction()
                .replace(R.id.lounge_content_container, CollectionListFragment())
                .commit()
        }
    }

    // 버튼 선택 상태를 업데이트하는 함수
    private fun selectButton(selectedView: View) {
        // 모든 버튼을 일단 false로 초기화
        binding.tvAllList.isSelected = false
        binding.tvCollectionList.isSelected = false

        // 선택된 뷰의 isSelected만 true로 변경
        selectedView.isSelected = true
    }
}

