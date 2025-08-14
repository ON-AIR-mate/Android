package umc.onairmate.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R

@AndroidEntryPoint
class JoinDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_join_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 닫기 버튼 클릭 시 JoinFragment로 이동
        view.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            findNavController().navigate(R.id.action_joinDetailFragment_to_joinFragment)
        }

        // 완료 버튼 클릭 시 JoinFragment로 이동
        view.findViewById<Button>(R.id.join_btn).setOnClickListener {
            findNavController().navigate(R.id.action_joinDetailFragment_to_joinFragment)
        }
    }
}