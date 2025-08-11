package umc.onairmate.ui.pop_up

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.databinding.PopupCreateRoomBinding
import umc.onairmate.ui.chat_room.drawer.RoomSettingSpAdapter
import umc.onairmate.ui.chat_room.drawer.maxParticipants

class CreateRoomPopup (
private val data : VideoData,
private val createRoomCallback : CreateRoomCallback
): DialogFragment() {
    lateinit var binding: PopupCreateRoomBinding

    var isPrivate : Boolean = false
    var roomTitle: String = ""
    var textLength: Int = 0

    private var editRunnable: Runnable? = null
    private val editHandler = Handler(Looper.getMainLooper())


    // 방 정보 주입
    private fun initData(){
        binding.tvVideoTitle.text = data.title

        val maxParticipantsAdapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spMaximumParticipant.adapter = maxParticipantsAdapter
    }

    private fun onPrivateRoomButtonClick() {
        binding.ivPrivateRoomOn.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.GONE
            binding.ivPrivateRoomOff.visibility = View.VISIBLE
            isPrivate = false
        }
        binding.ivPrivateRoomOff.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.VISIBLE
            binding.ivPrivateRoomOff.visibility = View.GONE
            isPrivate = true
        }
    }

    private fun setTextListener(){
        binding.etInputRoomTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textLength = s?.length ?: 0
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        editRunnable?.let { editHandler.removeCallbacks(it) }
    }

}

interface CreateRoomCallback {
    fun onCreateRoom(body: CreateRoomRequest)
}