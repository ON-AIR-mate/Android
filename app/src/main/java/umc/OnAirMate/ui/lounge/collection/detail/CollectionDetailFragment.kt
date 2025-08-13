package umc.onairmate.ui.lounge.collection.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.Bookmark
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.request.ModifyCollectionRequest
import umc.onairmate.data.model.request.MoveCollectionRequest
import umc.onairmate.data.model.request.RoomStartOption
import umc.onairmate.databinding.FragmentCollectionDetailBinding
import umc.onairmate.ui.lounge.bookmark.BookmarkEventListener
import umc.onairmate.ui.lounge.bookmark.BookmarkRVAdapter
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel
import umc.onairmate.ui.lounge.bookmark.move.CollectionMoveDialog
import umc.onairmate.ui.lounge.collection.CollectionRVAdapter
import umc.onairmate.ui.lounge.collection.CollectionViewModel
import umc.onairmate.ui.pop_up.CreateRoomCallback
import umc.onairmate.ui.pop_up.CreateRoomPopup

@AndroidEntryPoint
class CollectionDetailFragment : Fragment() {

    private var _binding: FragmentCollectionDetailBinding? = null
    private val binding get() = _binding!!

    private var collectionId: Int = 0
    private lateinit var adapter: CollectionDetailRVAdapter

    private val collectionViewModel: CollectionViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDetailBinding.inflate(inflater, container, false)
        collectionId = arguments?.getInt("collectionId")!!

        setAdapter()
        setupObserver()
        setupRefreshObserver()

        return binding.root
    }

    private fun setAdapter() {
        collectionViewModel.getCollectionDetailInfo(collectionId)
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setupRefreshObserver() {

    }

    private fun setupObserver() {
        collectionViewModel.collectionDetailDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                Toast.makeText(context, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@observe
            }

            adapter = CollectionDetailRVAdapter(
                object : CollectionDetailEventListener {
                    override fun onVisibilitySelected(selectedVisibility: String) {
                        Log.d("detail", "여기다 visibility")
                        collectionViewModel.modifyCollection(
                            collectionId,
                            request = ModifyCollectionRequest(
                                title = data.title,
                                description = data.description ?: "",
                                visibility = CollectionVisibility.fromDisplayName(selectedVisibility)!!.apiName
                            )
                        )
                    }
                    override fun onTitleModifyClicked() {
                        // todo: 팝업 띄워서 제목 변경 유도
                    }
                    override fun onDescriptionModified(input: String) {
                        Log.d("detail", "여기다 description")
                        collectionViewModel.modifyCollection(
                            collectionId,
                            request = ModifyCollectionRequest(
                                title = data.title,
                                description = input ?: "",
                                visibility = data.visibility
                            )
                        )
                    }
                },
                object : BookmarkEventListener {
                    override fun createRoomWithBookmark(bookmark: BookmarkData) {
                        // todo: 방 생성 팝업 띄워서 방 만들기
                        showCreateRoomPopup(bookmark)
                    }

                    override fun deleteBookmark(bookmark: BookmarkData) {
                        bookmarkViewModel.deleteBookmark(bookmark.bookmarkId)
                        Toast.makeText(context, "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun moveCollection(bookmark: BookmarkData) {
                        // todo: 팝업 띄워서 어떤 컬렉션으로 보낼지 선택해야함
                        showMoveCollectionPopup(bookmark)
                    }
                }
            )

            adapter.initData(data)
            binding.rvBookmarks.adapter = adapter
        }

    }

    // 방 생성 팝업 띄우기
    private fun showCreateRoomPopup(data: BookmarkData){

        val dialog = CreateRoomPopup(null, object : CreateRoomCallback {
            override fun onCreateRoom(body: CreateRoomRequest) {
                val requestBody = CreateRoomWithBookmarkRequest(
                    roomName = body.roomName,
                    maxParticipants = body.maxParticipants,
                    isPrivate = body.isPrivate,
                    startFrom = RoomStartOption.BEGINNING.apiName
                )

                // 북마크로 방 생성 api 호출
                bookmarkViewModel.createRoomWithBookmark(data.bookmarkId, requestBody)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "CreateRoomPopup")
        }
    }

    private fun showMoveCollectionPopup(bookmark: BookmarkData) {
        collectionViewModel.getCollections()

        collectionViewModel.collectionList.observe(viewLifecycleOwner) { data ->
            val collectionList = data.collections ?: emptyList()

            val dialog = CollectionMoveDialog(collectionList, {
                bookmarkViewModel.moveCollectionOfBookmark(bookmark.bookmarkId, MoveCollectionRequest(it))
            })
            activity?.supportFragmentManager?.let { fm ->
                dialog.show(fm, "CollectionMoveDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
