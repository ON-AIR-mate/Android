package umc.onairmate.ui.home.room

import umc.onairmate.data.model.entity.RoomData

// 홈화면 리사이클러뷰에서 필요한 이벤트 리스너
interface HomeEventListener {
    fun joinRoom(data : RoomData){
        return
    }

    fun selectSortType(type: String )

}