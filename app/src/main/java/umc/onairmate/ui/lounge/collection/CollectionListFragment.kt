package umc.onairmate.ui.lounge.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.request.ShareCollectionRequest
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.lounge.collection.create.CollectionCreateDialog
import umc.onairmate.ui.lounge.collection.create.CreateCollectionCallback
import umc.onairmate.ui.lounge.collection.delete.CollectionDeleteDialog
import umc.onairmate.ui.lounge.collection.detail.CollectionDetailFragment
import umc.onairmate.ui.lounge.collection.share.CollectionShareDialog
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.SharedPrefUtil

@AndroidEntryPoint
class CollectionListFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter
    private val collectionViewModel: CollectionViewModel by viewModels()
    private val friendViewModel: FriendViewModel by viewModels()

    private val user: UserData = SharedPrefUtil.getData("user_info") ?: UserData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        initAdapter()
        setClickListener()
        initObserver()
        setupRefreshObserver()

        return binding.root
    }

    private fun setupRefreshObserver() {
        collectionViewModel.createdCollectionDataInfo.observe(viewLifecycleOwner) { data ->
            Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
            collectionViewModel.getCollections()
        }
        collectionViewModel.deleteCollectionMessage.observe(viewLifecycleOwner) { data ->
            Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
            collectionViewModel.getCollections()
        }
        collectionViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    fun initAdapter() {
        collectionViewModel.getCollections()

        adapter = CollectionRVAdapter(object : CollectionEventListener {
            override fun deleteCollection(collection: CollectionData) {
                showDeleteDialog(collection)
            }
            override fun shareCollection(collection: CollectionData) {
                showShareDialog(collection)
            }

            override fun clickCollectionItem(collection: CollectionData) {
                val bundle = Bundle()
                bundle.putInt("collectionId", collection.collectionId)
                val detail = CollectionDetailFragment()
                detail.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.lounge_content_container, detail)
                    .commit()
            }
        })
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.adapter = adapter
    }

    fun setClickListener() {
        binding.llCreateCollection.setOnClickListener {
            showCreateCollectionPopup()
        }
    }

    fun initObserver() {
        collectionViewModel.collectionList.observe(viewLifecycleOwner) { data ->
            val collections = data.collections ?: emptyList()

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

    private fun showCreateCollectionPopup() {
        val dialog = CollectionCreateDialog(object : CreateCollectionCallback {
            override fun onCreateCollection(requestData: CreateCollectionRequest) {
                collectionViewModel.createCollection(requestData)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateCollectionPopup")
        }
    }


    private fun showShareDialog(collectionData: CollectionData) {
        friendViewModel.getFriendList()

        friendViewModel.friendList.observe(viewLifecycleOwner) { list ->
            val friendList = list ?: emptyList()

            val dialog = CollectionShareDialog(friendList, { friend ->
                val request = ShareCollectionRequest(
                    listOf(friend.userId)
                )

                collectionViewModel.shareCollection(collectionData.collectionId ,request)
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "ShareCollectionPopup")
            }
        }
    }

    private fun showDeleteDialog(collectionData: CollectionData) {
        val dialog = CollectionDeleteDialog(collectionData, object : PopupClick {
            override fun leftClickFunction() {
                collectionViewModel.deleteCollection(collectionData.collectionId)
            }

            override fun rightClickFunction() { }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "DeleteCollectionPopup")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}