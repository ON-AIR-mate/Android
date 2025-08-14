package umc.onairmate.ui.util

import android.widget.ImageView
import coil.load
import umc.onairmate.R

/**
 * 네트워크 이미지 로더 (썸네일 유틸)
 *
 * 사용법
 * - 썸네일 로더: NetworkImageLoader.thumbnailLoad(이미지뷰, url)
 * - 프로필 로더: NetworkImageLoader.profileLoad(이미지뷰, url)
 */
object NetworkImageLoader {
    // 사용법: NetworkImageLoader.thumbnailLoad(이미지뷰, url)
    fun thumbnailLoad(imageView: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.img)
        } else {
            imageView.load(url) {
                // 로딩중 이미지
                placeholder(R.drawable.img_loading)
                // 로딩 실패시 이미지
                error(R.drawable.img_fail)
            }
        }
    }

    // 사용법: NetworkImageLoader.profileLoad(이미지뷰, url)
    fun profileLoad(imageView: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.ic_empty_profile)
        } else {
            imageView.load(url) {
                // 로딩중 이미지
                placeholder(R.drawable.ic_empty_profile)
                // 로딩 실패시 이미지
                error(R.drawable.ic_empty_profile)
            }
        }
    }
}