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
import umc.onairmate.databinding.PopupTooltipBinding
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
            showTooltip(it, "추천 및 제재에 따라 인기도가 조정됩니다.")
        }

        binding.layoutMyRooms.setOnClickListener {  }

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
        val tooltipBinding = PopupTooltipBinding.inflate(LayoutInflater.from(anchorView.context))

        // 텍스트 설정
        tooltipBinding.tooltipText.text = message

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            tooltipBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        tooltipBinding.root.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        val popupWidth = tooltipBinding.root.measuredWidth
        val offsetX = -popupWidth + anchorView.width
        val offsetY = 0
        

        popupWindow.showAsDropDown(anchorView, offsetX, offsetY)

    }

    private fun initUserData(){
        Log.d(TAG,"initUserData")
        binding.tvNickname.text = user.nickname
        NetworkImageLoader.profileLoad(binding.ivProfile, user.profileImage)
    }
}