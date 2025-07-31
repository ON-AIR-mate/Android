package umc.onairmate.ui.util

import android.widget.ImageView
import coil.load
import umc.onairmate.R

/**
 * 네트워크 이미지 로더 (썸네일 유틸)
 *
 * 사용법: NetworkImageLoader.load(이미지뷰, url)
 */
object NetworkImageLoader {
    fun load(imageView: ImageView, url: String) {
        imageView.load(url) {
            // 로딩중 이미지
            placeholder(R.drawable.img_loading)
            // 로딩 실패시 이미지
            error(R.drawable.img_fail)
        }
    }
}