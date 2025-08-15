package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.SocketMessage
import umc.onairmate.data.model.entity.VideoPauseData
import umc.onairmate.data.model.entity.VideoPlayData
import umc.onairmate.data.model.entity.VideoSyncData

interface VideoEventListener {
    fun onError(errorMessage: SocketMessage){}
    fun onVideoPlay(playData: VideoPlayData) {}
    fun onVideoPause(pauseData: VideoPauseData) {}
    fun syncVideo(syncData: VideoSyncData) {}
}