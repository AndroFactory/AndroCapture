package andro.capture.crop

import android.content.Context
import android.os.Bundle
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback

class CropCaptureFragment : UCropFragment() {

    override fun onAttach(context: Context?) {
        try {
            super.onAttach(context)
        } catch (e: ClassCastException) {
            //" must implement UCropFragmentCallback";
        }

    }

    companion object {

        fun newInstance(uCrop: Bundle, callback : UCropFragmentCallback): CropCaptureFragment {
            val fragment = CropCaptureFragment()
            fragment.arguments = uCrop
            fragment.setCallback(callback)
            return fragment
        }
    }
}