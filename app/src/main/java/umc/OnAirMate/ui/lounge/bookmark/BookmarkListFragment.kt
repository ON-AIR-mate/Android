package umc.onairmate.ui.lounge.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.BookmarkData
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

        return binding.root
    }

    private fun initScreen() {
        bookmarkViewModel.getBookmarks(null, false)
    }

    private fun setAdapter() {
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner) { data ->
            // todo: data가 any와 categorized로 나뉘어져있으므로 그거 처리해야함!!
            val bookmarkList = data ?: emptyList<BookmarkData>()
        }
    }
}