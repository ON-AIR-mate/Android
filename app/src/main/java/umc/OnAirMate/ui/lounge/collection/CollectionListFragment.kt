package umc.onairmate.ui.lounge.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.pop_up.CollectionCreateDialogFragment

@AndroidEntryPoint
class CollectionListFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter
    private val collectionViewModel: CollectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        initAdapter()
        setClickListener()
        initObserver()

        return binding.root
    }

    fun initAdapter() {
        collectionViewModel.getCollections()
        adapter = CollectionRVAdapter(object : CollectionEventListener {
            override fun deleteCollection(collection: CollectionData) {
                // todo: 컬렉션 삭제 팝업 띄워서 삭제하기
            }
            override fun shareCollection(collection: CollectionData) {
                // todo: 컬렉션 공유 팝업 띄우기
            }

            override fun clickCollectionItem(collection: CollectionData) {
                // todo: parentFragment를 detail 뷰로 교체하기
            }
        })
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.adapter = adapter
    }

    fun setClickListener() {
        binding.llCreateCollection.setOnClickListener {
            // todo: 컬렉션 생성 팝업 띄우고 컬렉션 생성
        }
    }

    fun initObserver() {
        collectionViewModel.collectionList.observe(viewLifecycleOwner) { list ->
            val collections = list ?: emptyList()

            if (collections.isEmpty()) {
                binding.emptyCollectionLayout.visibility = View.VISIBLE
                binding.rvCollections.visibility = View.GONE
            } else {
                binding.emptyCollectionLayout.visibility = View.GONE
                binding.rvCollections.visibility = View.VISIBLE

                adapter.submitList(collections)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}