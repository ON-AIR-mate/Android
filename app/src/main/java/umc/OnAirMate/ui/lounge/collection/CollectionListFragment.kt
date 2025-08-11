package umc.onairmate.ui.lounge.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.databinding.FragmentCollectionListBinding
import umc.onairmate.ui.pop_up.CollectionCreateDialogFragment

class CollectionListFragment : Fragment() {

    private var _binding: FragmentCollectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionListBinding.inflate(inflater, container, false)

        initAdapter()
        setClickListener()

        return binding.root
    }

    fun initAdapter() {
        adapter = CollectionRVAdapter()
    }

    fun setClickListener() {
        binding.llCreateCollection.setOnClickListener {
            // 컬렉션 생성 팝업 띄우고 컬렉션 생성
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 예시 데이터
val collections = listOf(
    CollectionData("웃긴 장면", "2025.03.24", "2025.06.23", "비공개", ""),
    CollectionData("감동 모음", "2025.01.10", "2025.04.01", "공유하기", " ")
)