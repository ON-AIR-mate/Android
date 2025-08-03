package umc.onairmate.ui.home.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.onairmate.R
import umc.onairmate.databinding.FragmentVideoSearchBinding

class VideoSearchFragment : Fragment() {

    private lateinit var binding: FragmentVideoSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoSearchBinding.inflate(layoutInflater)

        return binding.root
    }

}