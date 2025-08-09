package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.lounge.adapter.CollectionListAdapter
import umc.onairmate.ui.lounge.model.Collection

class CollectionListFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionListAdapter
    private val sampleData = listOf(
        Collection(
            title = "웃긴 장면",
            dateCreated = "2025.03.24",
            lastUpdated = "2025.03.24", // 실제 업데이트 날짜로
            privacy = "전체 공개",
            thumbnailUrl = "https://example.com/image1.jpg"
        ),
        Collection(
            title = "감동 모음",
            dateCreated = "2025.02.01",
            lastUpdated = "2025.02.01",
            privacy = "친구만 공개",
            thumbnailUrl = "https://example.com/image2.jpg"
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CollectionListAdapter(sampleData) { collection ->
            val action = CollectionListFragmentDirections
                .actionCollectionListFragmentToCollectionDetailFragment(collection)
            findNavController().navigate(action)
        }

        binding.recyclerViewCollections.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCollections.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}