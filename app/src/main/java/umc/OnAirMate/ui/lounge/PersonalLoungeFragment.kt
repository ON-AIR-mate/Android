package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.databinding.FragmentPersonalLoungeBinding
import umc.onairmate.ui.lounge.adapter.PersonalLoungeAdapter
import umc.onairmate.ui.lounge.dialog.ImportCollectionDialogFragment
import umc.onairmate.ui.lounge.dialog.ShareBlockedDialogFragment
import umc.onairmate.ui.lounge.dialog.ShareToUserDialogFragment
import umc.onairmate.ui.lounge.model.Collection


class PersonalLoungeFragment : Fragment() {

    private var _binding: FragmentPersonalLoungeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalLoungeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        // 1. 샘플 데이터 생성
        val sampleData = listOf(
            Collection(
                title = "웃긴 장면",
                dateCreated = "2025.03.24",
                lastUpdated = "2025.06.23",
                privacy = "전체공개",
                thumbnailUrl = "https://example.com/image1.jpg",
                ownerName = "닉네임A",
                date = "2025.03.24",
                visibility = "전체공개",
                description = "웃긴 장면들을 모아놓은 컬렉션입니다."
            ),
            Collection(
                title = "감동 모음",
                dateCreated = "2025.04.01",
                lastUpdated = "2025.07.10",
                privacy = "친구공개",
                thumbnailUrl = "https://example.com/image2.jpg",
                ownerName = "닉네임B",
                date = "2025.04.01",
                visibility = "친구만 공개",
                description = "감동적인 순간들을 모은 영상 모음이에요."
            )
        )

        // 2. 어댑터 설정
        val adapter = PersonalLoungeAdapter(
            itemList = sampleData,
            onCopyClick = { collection ->
                showCopyConfirmDialog(collection)
            },
            onShareClick = { collection ->
                if (collection.visibility != "전체공개") {
                    // 전체공개가 아니면 공유 차단 다이얼로그
                    val dialog = ShareBlockedDialogFragment(
                        collectionTitle = collection.title,
                        visibility = collection.visibility
                    )
                    dialog.show(parentFragmentManager, "ShareBlockedDialog")
                } else {
                    // 전체공개일 경우 정상 공유 흐름
                    showShareToUserDialog(collection)
                }
            },
            onImportClicked = { nickname, title ->
                val dialog = ImportCollectionDialogFragment(
                    nickname = nickname,
                    collectionTitle = title,
                    onConfirm = {
                        Toast.makeText(requireContext(), "가져오기 완료", Toast.LENGTH_SHORT).show()
                    }
                )
                dialog.show(parentFragmentManager, "ImportDialog")
            }
        )

        // 3. RecyclerView 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun showCopyConfirmDialog(collection: Collection) {
        AlertDialog.Builder(requireContext())
            .setTitle("내 라운지에 가져오기")
            .setMessage("[${collection.title}] 컬렉션을 가져오시겠습니까?")
            .setPositiveButton("확인") { dialog, _ ->
                // TODO: 가져오기 처리
                dialog.dismiss()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showShareToUserDialog(collection: Collection) {
        val dialog = ShareToUserDialogFragment.newInstance(collection)
        dialog.show(parentFragmentManager, "ShareToUserDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
