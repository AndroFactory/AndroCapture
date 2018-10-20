package andro.capture.crop

import andro.capture.core.CropCaptureManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import java.io.File

internal class CropCaptureManagerImpl internal constructor() : CropCaptureManager, UCropFragmentCallback {

    lateinit var cropCaptureFragment : UCropFragment


    override fun startCropCapture(supportFragmentManager: Any, frameLayoutId: Int, sourceImage: File, destinationImage: File): Single<Boolean> {
        return doStartCropCapture(supportFragmentManager, frameLayoutId, sourceImage, destinationImage )
    }

    override fun startCropCapture(supportFragmentManager: Any, frameLayoutId: Int, sourceImage: String, destinationImage: String): Single<Boolean> {
        return doStartCropCapture(supportFragmentManager, frameLayoutId, sourceImage, destinationImage )
    }

    private fun doStartCropCapture(supportFragmentManager: Any, frameLayoutId: Int, sourceImage: Any, destinationImage: Any): Single<Boolean> {
        if(supportFragmentManager is FragmentManager){

            val option = UCrop.Options()
            option.setFreeStyleCropEnabled(true)
            option.setHideBottomControls(true)
            option.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)

            val mCropOptionsBundle = Bundle()
            mCropOptionsBundle.putParcelable(UCrop.EXTRA_INPUT_URI, convertToUri(sourceImage))
            mCropOptionsBundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, convertToUri(destinationImage))
            mCropOptionsBundle.putAll(option.optionBundle)

            cropCaptureFragment = CropCaptureFragment.newInstance(mCropOptionsBundle, this)

            supportFragmentManager.beginTransaction()
                    .add(frameLayoutId, cropCaptureFragment, UCropFragment.TAG)
                    .commitAllowingStateLoss()

        }else {
            startCropCapturePublisher.onError(Throwable("erreur de container"))
        }
        return startCropCapturePublisher
    }

    private fun convertToUri(image: Any) : Uri {
        var retour : Uri? = null
        if(image is String){
            retour = Uri.parse(image)
        }else if(image is File){
            retour = Uri.fromFile(image)
        }
        return retour!!
    }

    override fun saveCropCapture(): Single<Boolean> {
        this.cropCaptureFragment.cropAndSaveImage()
        return saveCropCapturePublisher
    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {

    }

    override fun loadingProgress(showLoader: Boolean) {

    }

    companion object {

        val startCropCapturePublisher = SingleSubject.create<Boolean>()

        val saveCropCapturePublisher = SingleSubject.create<Boolean>()

    }


}




