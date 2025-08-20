package umc.onairmate.ui.chat_room.summary

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import umc.onairmate.R
import umc.onairmate.data.model.entity.Emotion
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.SummaryCreateResponse
import umc.onairmate.databinding.FbItemFeelingBinding
import umc.onairmate.databinding.FragmentSummaryBinding
import umc.onairmate.databinding.LlItemTimelineTextBinding
import umc.onairmate.ui.chat_room.ChatVideoViewModel
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel
import umc.onairmate.ui.util.SharedPrefUtil

class SummaryFragment : Fragment() {

    private lateinit var binding: FragmentSummaryBinding

    private val chatVideoViewModel: ChatVideoViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()

    private var roomData: RoomData = RoomData()
    private var isFeedbackClicked = false

    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(layoutInflater)
        roomData = arguments?.getParcelable("roomData", RoomData::class.java)!!

        initSummaryData()
        initScreen()

        return binding.root
    }

    private fun initSummaryData() {
        val body = SummaryCreateRequest(roomData.roomId)
        chatVideoViewModel.createChatSummary(body)
        bookmarkViewModel.getBookmarks(collectionId = null, true)
    }

    private fun initScreen() {
        chatVideoViewModel.createdSummaryDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe

            binding.tvRoomTitle.text = data.roomTitle
            binding.tvSummary.text = data.topicSummary
            
            // 감정 분석 flexbox에 data.emotionAnalysis 아이템 추가
            // 감정 리스트 처리
            binding.fbFeelings.removeAllViews()
            data.emotionAnalysis.forEachIndexed { index, emotion ->
                val itemBinding =
                    FbItemFeelingBinding.inflate(layoutInflater, binding.fbFeelings, false)

                // 텍스트 설정
                itemBinding.tvFeeling.text = "${emotion.emotion} ${emotion.percentage}%"

                // 배경 색상 설정
                val colorId = Emotion.apiNameToEmotion(emotion.emotion)?.emotionColor ?: return@forEachIndexed
                val colorInt = ContextCompat.getColor(itemBinding.root.context, colorId)
                itemBinding.tvFeeling.backgroundTintList = ColorStateList.valueOf(colorInt)

                binding.fbFeelings.addView(itemBinding.root)
            }

            // 북마크 linearlayout에 data....??? 아이템 추가
            // 북마크 타임라인 처리 - 북마크가 안와서 따로 북마크 받아옴

            binding.ivLike.setOnClickListener {
                if (isFeedbackClicked == false) {
                    // 아직 피드백 보내지 않음
                    isFeedbackClicked = true
                    val request = SummaryFeedbackRequest("LIKE", "")
                    chatVideoViewModel.sendFeedbackForSummary(data.summaryId, request)
                    Toast.makeText(context, "피드백을 보냈습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "이미 피드백을 보냈습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            binding.ivDislike.setOnClickListener {
                if (isFeedbackClicked == false) {
                    // 아직 피드백 보내지 않음
                    isFeedbackClicked = true
                    val request = SummaryFeedbackRequest("DISLIKE", "")
                    chatVideoViewModel.sendFeedbackForSummary(data.summaryId, request)
                    Toast.makeText(context, "피드백을 보냈습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "이미 피드백을 보냈습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            binding.tvDone.setOnClickListener {
                if (user.nickname == roomData.hostNickname) {
                    // host일 경우 api 통신으로 방 삭제
                    homeViewModel.leaveRoom(roomData.roomId)
                } else {
                    // 일반 참가자일 경우 이미 방이 사라졌으므로 화면만 전환
                    requireActivity().finish()
                    homeViewModel.clearRoomDetailInfo()
                }
            }
        }

        // 북마크 찾아 넣기
        bookmarkViewModel.bookmarkList.observe(viewLifecycleOwner) { data ->
            if ((data == null) or (data.uncategorized.find { it.roomData.roomId == roomData.roomId } == null)) {
                // 이번 방의 데이터 없음
                binding.tvBookmarkEmpty.visibility = View.VISIBLE
            } else {
                binding.tvBookmarkEmpty.visibility = View.GONE
                binding.fbFeelings.removeAllViews()
                val bookmarks = data.uncategorized.find { it.roomData.roomId == roomData.roomId }!!.bookmarks
                bookmarks.forEachIndexed { index, bookmark ->
                    val itemBinding =
                        LlItemTimelineTextBinding.inflate(layoutInflater, binding.llBookmarkText, false)
                    itemBinding.tvBookmarkTime.text = bookmark!!.message
                    binding.llBookmarkText.addView(itemBinding.root)
                }
            }
        }
    }
}