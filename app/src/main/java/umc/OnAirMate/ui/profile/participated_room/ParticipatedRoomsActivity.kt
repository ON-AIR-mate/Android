package umc.onairmate.ui.profile.participated_room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.R
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.ActivityParticipatedRoomsBinding
import umc.onairmate.ui.profile.UserViewModel


@AndroidEntryPoint
class ParticipatedRoomsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityParticipatedRoomsBinding
    private lateinit var adapter: ParticipatedRoomsAdapter

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticipatedRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScreen()
        setupAdapter()
        setupObserver()
    }

    private fun initScreen() {
        //userViewModel.loadParticipatedRooms()
    }


    private fun setupAdapter() {
        adapter = ParticipatedRoomsAdapter(object : ParticipatedRoomEventListener {
            override fun onDeleteClick(roomId: Int) {
                //userViewModel.deleteParticipatedRoom(roomId)
            }
        })

        binding.rvParticipatedRooms.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvParticipatedRooms.adapter = adapter
    }

    private fun setupObserver() {
        // 화면 진입 / 방 리스트 변경시 로드
        userViewModel.rooms.observe(this) { rooms ->
            // 참여한 방이 없습니다 화면 띄울지?
            val participatedRooms = rooms ?: emptyList()

            if (participatedRooms.isEmpty()) {
                //
            } else {
                adapter.submitList(rooms)
            }
        }

        // 방 삭제
        userViewModel.deleteState.observe(this) { deleteInfo ->
            val data = deleteInfo ?: null
            if (data == null) return@observe

            // 삭제 후 메시지
            Toast.makeText(this, "${userViewModel.deleteState.value}", Toast.LENGTH_SHORT).show()
            // 방 리스트 다시 로드
            userViewModel.loadParticipatedRooms()
        }
    }
}
