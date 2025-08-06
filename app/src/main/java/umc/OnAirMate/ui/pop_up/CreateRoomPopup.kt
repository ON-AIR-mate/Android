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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PopupCreateRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        setOnClickListener()
        initData()
        onPrivateRoomButtonClick()
        setTextListener()

        // 뒤로가기 가능
        setCancelable(true)

        // 배경 투명 + 밝기 조절 (0.7)
        dialog.window?.let { window ->
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.7f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = layoutParams
        }

        return dialog
    }

    // 팝업 버튼 클릭리스너
    private fun setOnClickListener() {
        binding.btnOk.setOnClickListener {
            if (textLength in 3..20) {
                val maxParticipantPosition = binding.spMaximumParticipant.selectedItemPosition

                val roomData = CreateRoomRequest(
                    roomName = roomTitle,
                    maxParticipants = maxParticipants[maxParticipantPosition].toInt(),
                    isPrivate = isPrivate,
                    videoId = data.videoId,
                )

                createRoomCallback.onCreateRoom(roomData)

                // 모든 함수 수행 후 팝업 닫기
                dismiss()
            } else {
                binding.tvTitleLengthErrorMsg.visibility = View.VISIBLE
            }

        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

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
            override fun afterTextChanged(s: Editable?) {
                editRunnable?.let { editHandler.removeCallbacks(it) }
                editRunnable = Runnable {
                    val input = binding.etInputRoomTitle.text.toString()
                    roomTitle = input
                }
            }
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