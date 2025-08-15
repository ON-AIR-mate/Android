package umc.onairmate.ui.chat_room

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.DialogLeaveRoomBinding
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.SharedPrefUtil

class ChatRoomLeaveDialog(
    private val roomData: RoomData,
    private val clickFunc: PopupClick
) : DialogFragment() {
    lateinit var binding: DialogLeaveRoomBinding

    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogLeaveRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        initDialog()
        setClickListener()

        // 취소 불가능
        setCancelable(isCancelable)
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

    private fun initDialog() {
        NetworkImageLoader.profileLoad(binding.ivHostProfile, roomData.hostProfileImage)
        binding.tvHostNickname.text = roomData.hostNickname
        binding.tvRoomUserNum.text = "${roomData.currentParticipants} / ${roomData.maxParticipants}"
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, roomData.videoThumbnail)
        binding.tvPlayTime.text = roomData.duration
        binding.tvRoomTitle.text = roomData.roomTitle
        binding.tvVideoTitle.text = roomData.videoTitle

        if (roomData.hostNickname == user.nickname) {
            binding.tvHostLeaveMessage.visibility = View.VISIBLE
        } else {
            binding.tvHostLeaveMessage.visibility = View.GONE
        }
    }

    private fun setClickListener() {
        binding.btnLeft.setOnClickListener {
            clickFunc.leftClickFunction()
            dismiss()
        }
        binding.btnRight.setOnClickListener {
            dismiss()
        }
    }
}