package umc.onairmate.ui.friend

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import umc.onairmate.ui.friend.list.FriendListTabFragment
import umc.onairmate.ui.friend.search.SearchFriendTabFragment

class FriendVPAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position){
            2 -> SearchFriendTabFragment()
            else -> FriendListTabFragment.newInstance(position)
        }
    }
    override fun getItemCount(): Int = 3

}