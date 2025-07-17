package umc.onairmate.ui.home.room

import umc.onairmate.data.RoomData

interface HomeEventListener {
    fun joinRoom(data : RoomData){
        return
    }
    fun selectSortType(type: String )

}