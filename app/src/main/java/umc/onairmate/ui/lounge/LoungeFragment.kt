package umc.onairmate.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentLoungeBinding
import umc.onairmate.data.model.entity.CollectionData as LoungeCollection

/**
 * LoungeFragment: '라운지' 화면의 진입점
 * - childFragment: BookmarkListFragment(전체 목록), CollectionListFragment(컬렉션 목록)
 */
@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private var _binding: FragmentLoungeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoungeBinding.inflate(inflater, container, false)

        initListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initListener() {
        // 전체 목록 버튼 선택시
        binding.tvAllList.setOnClickListener {
            selectButton(it)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CollectionListFragment())
                .commit()
        }
        // 컬렉션별 목록 버튼 선택시
        binding.tvCollectionList.setOnClickListener {
            selectButton(it)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CollectionListFragment())
                .commit()
        }
    }

    // 버튼 선택 상태를 업데이트하는 함수
    private fun selectButton(selectedView: View) {
        // 모든 버튼을 일단 false로 초기화
        binding.tvAllList.isSelected = false
        binding.tvCollectionList.isSelected = false

        // 선택된 뷰의 isSelected만 true로 변경
        selectedView.isSelected = true
    }

    private fun showDeleteConfirmationDialog(
        collection: LoungeCollection,
        onDeleteClick: (LoungeCollection) -> Unit
    ) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_collection, null)


        builder.setView(dialogView)
        val dialog = builder.create()

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvCollectionTitle)
        val tvCreated = dialogView.findViewById<TextView>(R.id.tvCreatedDate)
        val tvUpdated = dialogView.findViewById<TextView>(R.id.tvUpdatedDate)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        tvTitle.text = collection.title
        tvCreated.text = "생성일 : ${collection.dateCreated}"
        tvUpdated.text = "마지막 수정일 : ${collection.lastUpdated}"

        btnDelete.setOnClickListener {
            // 실제 삭제 로직 호출
            onDeleteClick(collection)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

