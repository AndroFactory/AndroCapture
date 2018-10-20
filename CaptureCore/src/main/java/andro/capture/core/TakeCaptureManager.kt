package andro.capture.core

import io.reactivex.Single
import java.io.File

interface TakeCaptureManager {

    fun startTakeCapture(frameLayoutId : Int) : Single<Boolean>

    fun saveTakeCapture(file : File) : Single<Boolean>

}
