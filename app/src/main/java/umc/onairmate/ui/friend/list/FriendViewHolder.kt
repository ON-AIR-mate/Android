package umc.onairmate.ui.friend.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.PopupFriendOptionsBinding
import umc.onairmate.databinding.RvItemFriendBinding
import umc.onairmate.ui.util.NetworkImageLoader

class FriendViewHolder(
    private val binding: RvItemFriendBinding,
    private val context: Context,
    private val itemClick : FriendListRVAdapter.ItemClickListener
) :  RecyclerView.ViewHolder(binding.root) {
    private val TAG = this.javaClass.simpleName
    fun bind(data : FriendData ){
        binding.tvUserNickname.text = data.nickname
        binding.ivOnline.visibility = if(data.isOnline) View.VISIBLE else View.GONE
        NetworkImageLoader.profileLoad(binding.ivProfile, data.profileImage)

        binding.ivMore.setOnClickListener {
            showPopupMenu(binding.ivMore, adapterPosition)
        }
        binding.ivMessage.setOnClickListener {
            itemClick.clickMessage()
        }
    }

    private fun showPopupMenu(anchorView: View, position: Int){
        val popupBinding = PopupFriendOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // popupBinding root 크기 측정 후 정렬 위치 계산
        popupBinding.root.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        val popupWidth = popupBinding.root.measuredWidth

        // 오른쪽 정렬: anchor 오른쪽 끝 기준
        val offsetX = -popupWidth + anchorView.width
        val offsetY = 0

        // 클릭 리스너 연결
        popupBinding.tvCollection.setOnClickListener {
            Log.d(TAG,"click tvCollection")
            //onMenuClick(MenuAction.COLLECTION, position)
            popupWindow.dismiss()
        }
        popupBinding.tvDeleteFriend.setOnClickListener {
            Log.d(TAG,"click tvDeleteFriend")
            //onMenuClick(MenuAction.DELETE, position)
            popupWindow.dismiss()
        }
        popupBinding.tvBlock.setOnClickListener {
            Log.d(TAG,"click tvBlock")
            //onMenuClick(MenuAction.BLOCK, position)
            popupWindow.dismiss()
        }
        popupBinding.tvReport.setOnClickListener {
            Log.d(TAG,"click tvReport")
            //onMenuClick(MenuAction.REPORT, position)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, offsetX, offsetY)
    }
}