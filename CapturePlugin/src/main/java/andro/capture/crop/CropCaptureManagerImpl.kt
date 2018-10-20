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

internal class CropCaptureManagerImpl internal constructor(private val supportFragmentManager: FragmentManager) : CropCaptureManager, UCropFragmentCallback {

    private lateinit var cropCaptureFragment : UCropFragment

    override fun startCropCapture(frameLayoutId: Int, sourceImage: File, destinationImage: File): Single<Boolean> {
            val option = UCrop.Options()
            option.setFreeStyleCropEnabled(true)
            option.setHideBottomControls(true)
            option.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)

            val mCropOptionsBundle = Bundle()
            mCropOptionsBundle.putParcelable(UCrop.EXTRA_INPUT_URI, Uri.fromFile(sourceImage))
            mCropOptionsBundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, Uri.fromFile(destinationImage))
            mCropOptionsBundle.putAll(option.optionBundle)

            cropCaptureFragment = CropCaptureFragment.newInstance(mCropOptionsBundle, this)

            supportFragmentManager.beginTransaction()
                    .add(frameLayoutId, cropCaptureFragment, UCropFragment.TAG)
                    .commitAllowingStateLoss()

        return startCropCapturePublisher
    }

    override fun saveCropCapture(): Single<Boolean> {
        this.cropCaptureFragment.cropAndSaveImage()
        return saveCropCapturePublisher
    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {
        toString()
    }

    override fun loadingProgress(showLoader: Boolean) {
        toString()
    }

    companion object {

        val startCropCapturePublisher = SingleSubject.create<Boolean>()

        val saveCropCapturePublisher = SingleSubject.create<Boolean>()

    }


}




