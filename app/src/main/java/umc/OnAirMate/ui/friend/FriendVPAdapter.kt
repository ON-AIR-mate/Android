package umc.OnAirMate.ui.friend

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendVPAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position){
            2 -> SearchFriendTabFragment()
            else -> FriendListTabFragment()
        }
    }
    override fun getItemCount(): Int = 3

}