package umc.onairmate.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.databinding.FragmentHomeBinding
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick

class HomeFragment : Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val list : ArrayList<String> = arrayListOf()
        for(i in 1..5) list.add("room${i}")

        val adapter = RoomRVAdapter(list) {info ->
            showJoinRoomPopup(info)
        }
        binding.rvContinuingRooms.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvContinuingRooms.adapter = adapter

        binding.rvOnairRooms.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvOnairRooms.adapter = adapter

        setSearchSpinner()


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSearchSpinner(){
        val searchTypeList = listOf("영상 제목", "방 제목", "방장 닉네임")
        val searchAdapter = SearchTypeSpinnerAdapter(requireContext(),searchTypeList)
        binding.spinnerSearchType.adapter = searchAdapter
        binding.spinnerSearchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                Log.d(TAG,"selected Item ${selectedItem}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showJoinRoomPopup(info : String){
        val dialog = JoinRoomPopup(info, object : PopupClick {
            override fun rightClickFunction() {

            }
        })
        dialog.show(activity?.supportFragmentManager!!, "JoinRoomPopup")
    }
}