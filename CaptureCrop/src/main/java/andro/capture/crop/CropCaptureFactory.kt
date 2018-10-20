package andro.capture.crop

import andro.capture.core.CropCaptureManager
import android.content.Context

class CropCaptureFactory {

    companion object {

        fun createCropCaptureManager() : CropCaptureManager {
            return CropCaptureManagerImpl()
        }
    }
}