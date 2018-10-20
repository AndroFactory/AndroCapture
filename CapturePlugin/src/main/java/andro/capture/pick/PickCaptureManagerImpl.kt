package andro.capture.pick

import andro.capture.core.PickCaptureManager
import andro.capture.pick.GalleryHelper.getIntentForImageGallery
import android.content.Context
import android.content.Intent

internal class PickCaptureManagerImpl internal constructor(
        private val context: Context,
        private val startActivityForResult: (requestCode: Int, Intent,
                                             publisher: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)) -> Unit)
    : PickCaptureManager {

    override fun pickCaptureFromGallery(result : (path : String) -> Unit) {
        getIntentForImageGallery(context).let {
            startActivityForResult(123, it) { _, _, data ->
                result(GalleryHelper.getPathFormIntent(context, data))
            }
        }
    }


}