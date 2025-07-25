package umc.onairmate.ui.util

import android.widget.ImageView
import coil.load
import umc.onairmate.R

/**
 * 네트워크 이미지 로더 (썸네일 유틸)
 *
 * 사용법: ThumbnailLoader.load(이미지뷰, url)
 */
object ThumbnailLoader {
    fun load(imageView: ImageView, url: String) {
        imageView.load(url) {
            // todo: 로딩중 이미지 넣기
            placeholder(R.drawable.ic_lounge)
            // todo: 로딩 실패 이미지 넣기
            error(R.drawable.ic_lounge)
        }
    }
}