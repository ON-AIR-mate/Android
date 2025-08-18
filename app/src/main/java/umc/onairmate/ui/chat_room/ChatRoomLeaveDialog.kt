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

/**
 * ChatRoomLeaveDialog: 채팅방에서 뒤로가기, 방 나가기 버튼을 눌렀을 때의 팝업
 * - 리소스: dialog_leave_room
 * - 매개변수
 *   1. roomData: 현재 가입된 방의 정보
 *   2. clickFunc: 팝업에서 특정 버튼을 눌렀을 때의 상호작용 지정
 */
class ChatRoomLeaveDialog(
    private val roomData: RoomData,
    private val clickFunc: PopupClick
) : DialogFragment() {
    lateinit var binding: DialogLeaveRoomBinding

    // 현재 유저의 정보
    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogLeaveRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        // 팝업 내 정보 초기화
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

    // 팝업 내 정보 초기화
    private fun initDialog() {
        // 팝업 내 정보 초기화 및 네트워크 이미지 로딩
        NetworkImageLoader.profileLoad(binding.ivHostProfile, roomData.hostProfileImage)
        NetworkImageLoader.thumbnailLoad(binding.ivThumbnail, roomData.videoThumbnail)

        binding.tvHostNickname.text = roomData.hostNickname
        binding.tvRoomUserNum.text = "${roomData.currentParticipants} / ${roomData.maxParticipants}"
        binding.tvPlayTime.text = roomData.duration
        binding.tvRoomTitle.text = roomData.roomTitle
        binding.tvVideoTitle.text = roomData.videoTitle

        // 현재 유저가 host일 경우 경고 메시지: host가 방에서 퇴장하면 방이 사라짐.
        if (roomData.hostNickname == user.nickname) {
            binding.tvHostLeaveMessage.visibility = View.VISIBLE
        } else {
            binding.tvHostLeaveMessage.visibility = View.GONE
        }
    }

    // 팝업 버튼 리스너 초기화
    private fun setClickListener() {
        // 확인 버튼
        binding.btnLeft.setOnClickListener {
            clickFunc.leftClickFunction()
            dismiss()
        }
        // 취소 버튼
        binding.btnRight.setOnClickListener {
            dismiss()
        }
    }
}