package umc.onairmate.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.lifecycle.Observer
import umc.onairmate.R
import androidx.core.os.bundleOf
import umc.onairmate.data.model.entity.BookmarkData as ParticipatedRoomData
import umc.onairmate.databinding.FragmentRecentRoomsBinding
import androidx.navigation.fragment.findNavController


@AndroidEntryPoint
class ParticipatedRoomsFragment :Fragment(R.layout.fragment_recent_rooms), OnRoomActionListener{

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentRecentRoomsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ParticipatedRoomsAdapter
    private var lastDeletedRoomId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // adapter = ParticipatedRoomsAdapter(this) ...
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecentRoomsBinding.bind(view)

        adapter = ParticipatedRoomsAdapter(this)
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter

        userViewModel.rooms.observe(viewLifecycleOwner) { list ->
//            val uiList = list.map { d ->
//                ParticipatedRoomItem(
//                    roomId = d.roomId.toLong(),          // 타입 맞춰주기 (Long/Int 확인)
//                    roomtitle = d.roomTitle,
//                    videotitle = d.videoTitle,
//                    bookmarktime = d.bookmarks.firstOrNull()?.timeline ?: 0 ,
//                    lastEnteredAt = d.participatedAt
//                )
//            }
            adapter.submitList(list) // 이미 ViewModel에서 “최근순” 정렬 완료
        }

        // 로딩 표시
        userViewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean> {isLoading ->
            if (isLoading == true) showLoading() else hideLoading()
        })

        // 메시지 표시(성공, 실패)
        userViewModel.deleteMessage.observe(viewLifecycleOwner, Observer { msg ->
            if (msg == null) return@Observer
            hideLoading()
            toast(msg)
            refreshListAfterDelete()


            lastDeletedRoomId?.let { removeItemFromList(it) }
            lastDeletedRoomId = null
        })

        }


//      화면 명세서에 있지 않은 기능임
//    override fun onRoomClick(roomId: Long) {
//        // 방 상세로 이동
//        findNavController().navigate(
//            R.id.action_recentRooms_to_roomDetail,
//            bundleOf("roomId" to roomId)
//        )
//    }
        // 처음 리스트 채우기(이미 있다면 생략)
        // adapter.submitList(initialList)

    //어뎁터에서 온 삭제 클릭 처리
    override fun onDeleteClick(roomId: Long) {
        AlertDialog.Builder(requireContext())
            .setMessage("참여한 방 기록을 삭제할까요?")
            .setPositiveButton("삭제") { _, _ ->
                lastDeletedRoomId = roomId
                userViewModel.deleteParticipated(roomId)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun refreshListAfterDelete() {
        // 서버/로컬 리스트 갱신 로직 (재호출 or 로컬 제거) 프로젝트 방식대로
    }


    private fun removeItemFromList(roomId: Long) {
        val cur = adapter.currentList.toMutableList()
        val idx = cur.indexOfFirst { it.roomId == roomId }
        if (idx >= 0) {
            cur.removeAt(idx)
            adapter.submitList(cur)
        }
    }

    private fun showLoading() { /* Progress */ }
    private fun hideLoading() { /* Progress */ }
    private fun toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}