package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.request.CollectionShareRequest
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.lounge.collection.CollectionEventListener
import umc.onairmate.ui.lounge.collection.CollectionRVAdapter
import umc.onairmate.ui.lounge.collection.CollectionViewModel
import umc.onairmate.ui.lounge.collection.detail.CollectionDetailFragment
import umc.onairmate.ui.lounge.collection.share.CollectionShareDialog
import umc.onairmate.ui.lounge.personal.dialog.PersonalLoungeImportDialog

@AndroidEntryPoint
class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter

    private var friendNickname : String = ""
    private val sharedCollectionsViewModel: SharedCollectionsViewModel by activityViewModels()
    private val collectionViewModel: CollectionViewModel by  activityViewModels()
    private val friendViewModel: FriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        friendNickname = arguments?.getString("friend_nickname", "").toString()

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

        collectionViewModel.importResponse.observe(viewLifecycleOwner){data ->
            if(data == null )return@observe
            Toast.makeText(requireContext(),data.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun initAdapter() {
        adapter = CollectionRVAdapter(object : CollectionEventListener {
            override fun deleteCollection(collection: CollectionData) {
                val title = "[${friendNickname}]의 " + collection.title
                val dialog = PersonalLoungeImportDialog(collectionId = collection.collectionId, { collectionId ->
                    collectionViewModel.importToMyCollection(collectionId)
                }, title )
                dialog.show(childFragmentManager, "ImportDialog")
            }
            override fun shareCollection(collection: CollectionData) {
                // 친구공개일경우
                if(collection.visibility == "FRIENDS_ONLY"){
                    val privacyDialog = PersonalLoungePrivacyInfoDialog(
                        onConfirmCallback = {

                        },
                        collection = collection,
                        friendNickname =friendNickname
                    )
                    privacyDialog.show(childFragmentManager, "PrivacyDialog")
                }
                else {
                    showShareDialog(collection)
                }
            }
            override fun clickCollectionItem(collection: CollectionData) {
                val bundle = Bundle()
                bundle.putInt("collectionId", collection.collectionId)
                bundle.putBoolean("isOwner",false)
                val detail = CollectionDetailFragment()
                detail.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.personal_lounge_content_container, detail)
                    .commit()
            }
        }, isOthers = true)
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCollections.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showShareDialog(collectionData: CollectionData) {
        friendViewModel.getFriendList()

        friendViewModel.friendList.observe(viewLifecycleOwner) { list ->
            val friendList = list ?: emptyList()

            val dialog = CollectionShareDialog(friendList, { friend ->
                val request = CollectionShareRequest(
                    listOf(friend.userId)
                )

                collectionViewModel.shareCollection(collectionData.collectionId ,request)
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "ShareCollectionPopup")
            }
        }
    }
}