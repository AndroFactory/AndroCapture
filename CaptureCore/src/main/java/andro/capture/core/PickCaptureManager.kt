package andro.capture.core


interface PickCaptureManager {

    fun pickCaptureFromGallery(result : (path : String) -> Unit)

}
