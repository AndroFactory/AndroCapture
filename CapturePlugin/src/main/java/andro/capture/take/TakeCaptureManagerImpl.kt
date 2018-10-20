package andro.capture.take

import andro.capture.core.TakeCaptureManager
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.support.v4.app.FragmentManager
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import java.io.File

internal class TakeCaptureManagerImpl internal constructor(private val supportFragmentManager: FragmentManager): TakeCaptureManager {

    private val takeCaptureCameraFragment = TakeCaptureFragment.newInstance()

    override fun startTakeCapture(frameLayoutId: Int) : Single<Boolean> {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, takeCaptureCameraFragment)
                .commitAllowingStateLoss()
        return startCapturePublisher
    }

    override fun saveTakeCapture(file: File) : Single<Boolean>{
        takeCaptureCameraFragment.lockFocus(file)
        return takeCapturePublisher
    }

    companion object {

        val takeCapturePublisher = SingleSubject.create<Boolean>()

        val startCapturePublisher = SingleSubject.create<Boolean>()

        fun onConfigured(cameraCaptureSession: CameraCaptureSession){ cameraCaptureSession.toString()
            startCapturePublisher.onSuccess(true)
        }

        fun cameraSessionOnConfigureFailed(session: CameraCaptureSession) {
            startCapturePublisher.onError(Throwable("cameraSessionOnConfigureFailed"))
        }

        fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            takeCapturePublisher.onSuccess(true)
        }

        fun startCaptureException(exception: Exception){
            startCapturePublisher.onError(exception)
        }

        fun takeCaptureException(exception: Exception){
            takeCapturePublisher.onError(exception)
        }

    }
}