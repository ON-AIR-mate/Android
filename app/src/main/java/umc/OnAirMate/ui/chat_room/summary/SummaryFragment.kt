package umc.onairmate.ui.chat_room.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import umc.onairmate.R
import umc.onairmate.databinding.FragmentSummaryBinding
import umc.onairmate.ui.chat_room.ChatVideoViewModel

class SummaryFragment : Fragment() {

    private lateinit var binding: FragmentSummaryBinding

    private val chatVideoViewModel: ChatVideoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(layoutInflater)

        return binding.root
    }
}