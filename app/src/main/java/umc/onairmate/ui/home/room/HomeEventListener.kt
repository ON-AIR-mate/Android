package umc.onairmate.ui.home.room

import android.content.Intent
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity

// 홈화면 리사이클러뷰에서 필요한 이벤트 리스너
interface HomeEventListener {
    fun joinRoom(data : RoomData){
        return
    }

    fun selectSortType(type: String )

}