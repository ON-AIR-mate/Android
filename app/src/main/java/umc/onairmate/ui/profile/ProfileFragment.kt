package umc.onairmate.ui.profile


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.FragmentProfileBinding
import umc.onairmate.ui.ImageViewModel
import umc.onairmate.ui.util.ImagePickerDelegate

import kotlin.getValue
import androidx.core.content.edit
import umc.onairmate.ui.login.LoginActivity
import umc.onairmate.ui.util.NetworkImageLoader
import umc.onairmate.ui.util.SharedPrefUtil



@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var imageUrl =""

    private var user : UserData = UserData()
    
    private val imageViewModel: ImageViewModel by viewModels()

    private lateinit var picker: ImagePickerDelegate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setImagePicker()

        user = SharedPrefUtil.getData("user_info")?: UserData()
        initUserData()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        initEventListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setObservers(){
        imageViewModel.imageUrl.observe(viewLifecycleOwner){ url ->
            if (url == null) return@observe
            imageUrl= url
            imageViewModel.editProfile(user.nickname,imageUrl)
        }
        imageViewModel.isSuccess.observe(viewLifecycleOwner){ isSuccess ->
            if (isSuccess == null) return@observe
            NetworkImageLoader.profileLoad(binding.ivProfile, imageUrl)
        }
        imageViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun initEventListeners() {
        binding.btnChangeProfile.setOnClickListener {
            picker.launch()
        }
        binding.ivPopularityHelp.setOnClickListener {
            //showTooltip(it, "추천 및 제재에 따라 인기도가 조정됩니다.")
        }

        //레이아웃클릭하면 엑티비티 오픈
        binding.layoutMyRooms.setOnClickListener {
            openParticipatedRooms()
        }
        binding.layoutBlock.setOnClickListener {
            // openBlockedUsers()
        }

        //로그아웃
        binding.tvLogout.setOnClickListener {
            val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            spf.edit { clear() }
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    private fun setImagePicker(){
        picker = ImagePickerDelegate(this) { uri ->
            if (uri != null) {
                imageViewModel.uploadUri(uri)
            }
        }
        picker.register()
    }


    //도움말 클릭시 글귀 표시
    private fun showTooltip(anchorView: View, message: String) {
        val inflater = LayoutInflater.from(anchorView.context)
        val popupView = inflater.inflate(R.layout.popup_tooltip, null)

        // 텍스트 설정
        val tooltipText = popupView.findViewById<TextView>(R.id.tooltip_text)
        tooltipText.text = message

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // 위치 계산
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        val anchorX = location[0]
        val anchorY = location[1]

        // anchorView 위에 말풍선 위치 조정
        popupWindow.showAtLocation(
            anchorView, Gravity.NO_GRAVITY,
            anchorX - popupView.measuredWidth / 2 + anchorView.width / 2,
            anchorY - anchorView.height - 20  // 말풍선 높이 조절
        )


    }

    private fun initUserData(){
        Log.d(TAG,"initUserData")
        binding.tvNickname.text = user.nickname
        NetworkImageLoader.profileLoad(binding.ivProfile, user.profileImage)
    }

    private fun openParticipatedRooms() {
        // TODO: 실제 대상 액티비티 이름으로 바꿔줘 (예: ParticipatedRoomsActivity)
        val intent = Intent(requireContext(), ParticipatedRoomsActivity::class.java)
        startActivity(intent)
    }

//    private fun openBlockedUsers() {
//        // TODO: 차단 목록 액티비티/프래그먼트가 다르면 맞게 수정
//        val intent = Intent(requireContext(), BlockListActivity::class.java)
//        startActivity(intent)
//    }



    private object ExtraKeys {
        const val ROOM_ID = "roomId"
    }

}