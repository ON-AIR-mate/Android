package umc.onairmate.ui.chat_room.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import umc.onairmate.R
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.SummaryCreateResponse
import umc.onairmate.databinding.FbItemFeelingBinding
import umc.onairmate.databinding.FragmentSummaryBinding
import umc.onairmate.databinding.LlItemTimelineTextBinding
import umc.onairmate.ui.chat_room.ChatVideoViewModel

class SummaryFragment : Fragment() {

    private lateinit var binding: FragmentSummaryBinding

    private val chatVideoViewModel: ChatVideoViewModel by activityViewModels()

    private var roomId: Int = 0
    private var isFeedbackClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(layoutInflater)
        roomId = arguments?.getInt("roomId", 0)!!

        initSummaryData()

        return binding.root
    }

    private fun initSummaryData() {
        val body = SummaryCreateRequest(roomId)
        chatVideoViewModel.createChatSummary(body)


    }

    private fun initScreen() {
        chatVideoViewModel.createdSummaryDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe

            binding.tvRoomTitle.text = data.roomTitle
            binding.tvSummary.text = data.topicSummary
            
            // 감정 분석 flexbox에 data.emotionAnalysis 아이템 추가
            // 북마크 타임라인 처리
            binding.fbFeelings.removeAllViews()
            data.emotionAnalysis.forEachIndexed { index, emotion ->
                val itemBinding =
                    FbItemFeelingBinding.inflate(layoutInflater, binding.fbFeelings, false)
                itemBinding.tvFeeling.text = "${emotion.emotion} ${emotion.percentage}%"

                binding.fbFeelings.addView(itemBinding.root)
            }

            // 북마크 linearlayout에 data....??? 아이템 추가
            // 북마크 타임라인 처리 - 북마크가 안와.....
            binding.llBookmarkText.removeAllViews()
            val itemBinding = LlItemTimelineTextBinding.inflate(layoutInflater, binding.llBookmarkText, false)
            itemBinding.tvBookmarkTime.text = data.timestamp
            binding.llBookmarkText.addView(itemBinding.root)

            /*// 북마크 하이라이트 여러개 받는데....
            data.timestamp.forEachIndexed { index, bookmark ->
                val itemBinding =
                    LlItemTimelineTextBinding.inflate(layoutInflater, binding.llBookmarkText, false)
                itemBinding.tvBookmarkTime.text = bookmark

                if (index >= 2) itemBinding.root.visibility = View.GONE

                binding.llBookmarkText.addView(itemBinding.root)
            }*/

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
        }
    }
}