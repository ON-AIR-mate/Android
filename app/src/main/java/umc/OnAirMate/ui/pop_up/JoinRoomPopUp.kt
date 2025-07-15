package umc.onairmate.ui.pop_up

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.PopupJoinRoomBinding


class JoinRoomPopup (
    private val roomInfo : String ,
    private val clickFunc : PopupClick
): DialogFragment(){
    lateinit var binding : PopupJoinRoomBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PopupJoinRoomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()
        setOnClickListener()
        initData()

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
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    // 방 정보 주입
    private fun initData(){
        binding.tvRoomName.text = roomInfo
    }


    // 팝업 띄우는 화면용 함수 -> 복붙하여 사용
    private fun test(info : String){
        val dialog = JoinRoomPopup(info, object : PopupClick {
            override fun rightClickFunction() {

            }
        })
        dialog.show(activity?.supportFragmentManager!!, "JoinRoomPopup")
    }
}