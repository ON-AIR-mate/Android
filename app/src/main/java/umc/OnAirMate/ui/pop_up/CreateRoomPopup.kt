package umc.onairmate.ui.pop_up

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
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.databinding.PopupCreateRoomBinding
import umc.onairmate.ui.chat_room.drawer.RoomSettingSpAdapter
import umc.onairmate.ui.chat_room.drawer.maxParticipants

class CreateRoomPopup (
private val data : VideoData,
private val clickFunc : PopupClick
): DialogFragment() {
    lateinit var binding: PopupCreateRoomBinding

    var isPrivate : Boolean = false
    lateinit var roomData : RoomData

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PopupCreateRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        setOnClickListener()
        initData()
        onPrivateRoomButtonClick()

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
            clickFunc.rightClickFunction()

            // 모든 함수 수행 후 팝업 닫기
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    // 방 정보 주입
    private fun initData(){
        binding.tvVideoTitle.text = data.title
        roomData = RoomData()

        val maxParticipantsAdapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spMaximumParticipant.adapter = maxParticipantsAdapter
    }

    fun onPrivateRoomButtonClick() {
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

    // 팝업 띄우는 화면용 함수 -> 복붙하여 사용
    private fun test(data : RoomData){
        val dialog = JoinRoomPopup(data, object : PopupClick {
            override fun rightClickFunction() {

            }
        })
        dialog.show(activity?.supportFragmentManager!!, "JoinRoomPopup")
    }

}