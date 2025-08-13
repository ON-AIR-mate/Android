package umc.onairmate.ui.util

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ImagePickerDelegateForActivity(
    private val activity: AppCompatActivity,
    private val onPicked: (Uri?) -> Unit
) {
    private lateinit var permission: ActivityResultLauncher<String>
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var getContent: ActivityResultLauncher<String>

    fun register() {
        pickMedia = activity.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri -> onPicked(uri) }

        getContent = activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri -> onPicked(uri) }

        permission = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) getContent.launch("image/*") else onPicked(null)
        }
    }

    fun launch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}
