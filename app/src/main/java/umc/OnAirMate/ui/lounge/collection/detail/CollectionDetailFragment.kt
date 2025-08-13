package umc.onairmate.ui.lounge.collection.detail

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
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.lounge.adapter.MoveCollectionAdapter
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkRVAdapter

@AndroidEntryPoint
class CollectionDetailFragment : Fragment() {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailBinding.inflate(inflater, container, false)

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        // todo: CollectionDetailRvAdapter 연결
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
