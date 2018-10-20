package andro.capture

import andro.capture.core.CropCaptureManager
import andro.capture.core.PickCaptureManager
import andro.capture.core.TakeCaptureManager
import andro.capture.crop.CropCaptureManagerImpl
import andro.capture.pick.PickCaptureManagerImpl
import andro.capture.take.TakeCaptureManagerImpl
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.FragmentManager

class CaptureFactory {

    companion object {

        fun createTakeCaptureManager(supportFragmentManager: FragmentManager): TakeCaptureManager {
            return TakeCaptureManagerImpl(supportFragmentManager)
        }

        fun createCropCaptureManager(supportFragmentManager: FragmentManager): CropCaptureManager {
            return CropCaptureManagerImpl(supportFragmentManager)
        }

        fun createPickCaptureManager(context: Context,
                                     startActivityForResult: (requestCode: Int, Intent,
                                                              publisher: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)) -> Unit): PickCaptureManager {
            return PickCaptureManagerImpl(context  , startActivityForResult)
        }
    }
}