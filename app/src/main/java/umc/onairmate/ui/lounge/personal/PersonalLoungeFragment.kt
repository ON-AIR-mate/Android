package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.response.DefaultResponse // DefaultResponse import
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.lounge.collection.CollectionEventListener
import umc.onairmate.ui.lounge.collection.CollectionRVAdapter
import umc.onairmate.ui.lounge.collection.detail.CollectionDetailFragment

@AndroidEntryPoint
class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter

    private var friendId : Int = 0
    private val sharedCollectionsViewModel: SharedCollectionsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        //friendId = arguments?.getInt("friendId", 0)!!

        binding.llCreateCollection.visibility = View.GONE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setObservers()

    }

    private fun setObservers() {
        sharedCollectionsViewModel.friendPublicCollections.observe(viewLifecycleOwner){list->
            if (list == null )return@observe
            if (list.isEmpty()) {
                binding.emptyCollectionLayout.visibility = View.VISIBLE
                binding.rvCollections.visibility = View.GONE

            }
            else{
                binding.emptyCollectionLayout.visibility = View.GONE
                binding.rvCollections.visibility = View.VISIBLE
                adapter.submitList(list)
            }

        }

    }

    fun initAdapter() {

        adapter = CollectionRVAdapter(object : CollectionEventListener {
            override fun deleteCollection(collection: CollectionData) {
                //showDeleteDialog(collection)
            }
            override fun shareCollection(collection: CollectionData) {
                //showShareDialog(collection)
            }

            override fun clickCollectionItem(collection: CollectionData) {
                val bundle = Bundle()
                bundle.putInt("collectionId", collection.collectionId)
                val detail = CollectionDetailFragment()
                detail.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.personal_lounge_content_container, detail)
                    .commit()
            }
        })
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}