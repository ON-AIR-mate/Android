package umc.onairmate.ui.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object UriToMultipartUtil {

    fun uriToMultipart(context: Context, uri: Uri, fieldName: String = "profileImage"): MultipartBody.Part {
        // 1. 파일명 먼저 가져오기
        val fileName = queryDisplayName(context, uri)
            ?: "image_${System.currentTimeMillis()}.jpg"

        Log.d("UriToMultipart", "fileName=$fileName")

        // 2. MIME 타입 결정
        val mimeType = context.contentResolver.getType(uri) ?: guessMimeType(fileName)
        Log.d("UriToMultipart", "mimeType=$mimeType")

        // 3. URI → 캐시 파일 복사
        val file = copyUriToCache(context, uri, fileName)
        Log.d("UriToMultipart", "cacheFilePath=${file.absolutePath} size=${file.length()} bytes")

        // 4. MultipartBody.Part 변환
        val part = MultipartBody.Part.createFormData(
            fieldName,
            file.name,
            file.asRequestBody(mimeType.toMediaTypeOrNull())
        )

        // 5. 변환된 part 로그
        Log.d(
            "UriToMultipart",
            "MultipartBody.Part created → ${part}}"
        )

        return part
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) cursor.getString(idx) else null
            } else null
        } finally {
            cursor?.close()
        }
    }

    private fun guessMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".png", ignoreCase = true) -> "image/png"
            fileName.endsWith(".webp", ignoreCase = true) -> "image/webp"
            fileName.endsWith(".heic", ignoreCase = true) -> "image/heic"
            else -> "image/jpeg"
        }
    }

    private fun copyUriToCache(context: Context, uri: Uri, fileName: String): File {
        val outFile = File(context.cacheDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        }
        return outFile
    }
}
