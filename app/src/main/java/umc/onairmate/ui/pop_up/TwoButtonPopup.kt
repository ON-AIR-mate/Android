package umc.onairmate.ui.pop_up

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.PopupTemplateTwoButtonBinding

class TwoButtonPopup (
    message: List<String>,
    private val clickFunc : PopupClick,
    private val isCancelable : Boolean = true
) : DialogFragment(){
    lateinit var binding: PopupTemplateTwoButtonBinding
    val text = message[0]
    val btnLeft = message[1]
    val btnRight = message[2]
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = PopupTemplateTwoButtonBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        // 텍스트 바꾸기
        updateInfo()
        setOnClickListener()

        // 취소 불가능
        setCancelable(isCancelable)

//        // 배경 투명 + 밝기 조절 (0.7)
//        dialog.window?.let { window ->
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            val layoutParams = window.attributes
//            layoutParams.dimAmount = 0.7f
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//            window.attributes = layoutParams
//        }

        return dialog
    }

    private fun setOnClickListener() {
        binding.btnLeft.setOnClickListener {
            // 함수 실행
            clickFunc.leftClickFunction()
            // 모든 함수 수행 후 팝업 닫기
            dismiss()
        }
        binding.btnRight.setOnClickListener {
            // 함수 실행
            clickFunc.rightClickFunction()
            // 모든 함수 수행 후 팝업 닫기
            dismiss()
        }
    }

    // 팝업창 실행할 곳에 넣을 함수 예시 (팝업 띄울 위치에 복붙해서 사용하면 됩니다!
    private fun popupTest() {
        val text = listOf("main text ","Left","Right")
        val dialog = TwoButtonPopup(text,object : PopupClick{
            override fun rightClickFunction() {
                // 실행하고자 하는 함수 있으면 overriding

            }
        }, false) // 뒤로 가기 막고 싶으면 false 넣어주세요, 아니면 생략가능합니다.
        dialog.show(activity?.supportFragmentManager!!, "testPopup")
    }


    private fun updateInfo() {
        binding.tvPopupText.text= text
        binding.btnLeft.text = btnLeft
        binding.btnRight.text = btnRight
    }

}