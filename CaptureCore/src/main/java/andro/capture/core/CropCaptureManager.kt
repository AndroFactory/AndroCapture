package andro.capture.core

import io.reactivex.Single
import java.io.File

interface CropCaptureManager {

    fun startCropCapture(frameLayoutId : Int, sourceImage : File, destinationImage : File) : Single<Boolean>

    fun saveCropCapture() : Single<Boolean>
}