package com.example.android.camera2basic

import andro.capture.core.CropCaptureManager
import andro.capture.core.TakeCaptureManager
import andro.capture.crop.CropCaptureFactory
import andro.capture.take.TakeCaptureFactory
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import java.io.File

class CameraActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var takeCaptureManager : TakeCaptureManager

    lateinit var cropCaptureManager: CropCaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        takeCaptureManager = TakeCaptureFactory.createTakeCaptureManager(this.supportFragmentManager)

        cropCaptureManager = CropCaptureFactory.createCropCaptureManager()

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ), REQUEST_PERMISSION)

        findViewById<View>(R.id.picture).setOnClickListener(this)
        findViewById<View>(R.id.info).setOnClickListener(this)
        findViewById<View>(R.id.save).setOnClickListener(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.size == grantResults.size) {
                takeCaptureManager.startTakeCapture( R.id.container)
            } else {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(supportFragmentManager, "dialog")
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.picture -> {
                val file = File(getExternalFilesDir(null), PIC_FILE_NAME)

                takeCaptureManager.saveTakeCapture(file).subscribe(
                        { response -> onTakeCaptureSuccess(response, file)},
                        { error -> onError(error)}
                )
            }
            R.id.info -> { pickFromGallery() }
            R.id.save -> { cropCaptureManager.saveCropCapture().subscribe(
                    { response -> onSaveCaptureSuccess(response)},
                    { error -> onError(error)} )
            }
        }
    }

    private fun pickFromGallery() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        startActivityForResult(Intent.createChooser(intent, ".label_select_picture"), 1)
    }

    private fun onError(error: Throwable?) {
        showToast(error!!.message!!)
    }

    private fun onTakeCaptureSuccess(success: Boolean, file : File) {
        if(success){
            val file2 = File(getExternalFilesDir(null), PIC_FILE_NAME2)
            cropCaptureManager.startCropCapture(supportFragmentManager, R.id.container, file, file2).subscribe(
                    { response -> onCropCaptureSuccess(response, file)},
                    { error -> onError(error)}
            )
        }else {
            showToast("Erreur ")
        }
    }

    private fun onSaveCaptureSuccess(response: Boolean) {
        if("SUCCESS".equals(response)){
            "SUCCESS".toString()
        }else {
            showToast("Erreur ")
        }
    }

    private fun onCropCaptureSuccess(response: Boolean, file : File) {
        if("SUCCESS".equals(response)){
            val file2 = File(getExternalFilesDir(null), PIC_FILE_NAME2)
            cropCaptureManager.startCropCapture(supportFragmentManager, R.id.container, file, file2).subscribe(
                    { response -> onTakeCaptureSuccess(response, file)},
                    { error -> onError(error)}
            )
        }else {
            showToast("Erreur ")
        }
    }

/*
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data)
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val selectedUri = data.data
                if (selectedUri != null) {
                    startCrop(selectedUri)
                } else {
                    Toast.makeText(this, "toast_cannot_retrieve_selected_image", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                //    handleCropResult(data)
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            //    handleCropError(data)
        }
    }
*/


    private fun startCrop(uri: Uri) {
        var destinationFileName = "SAMPLE_CROPPED_IMAGE_NAME.png" // jpg
        var sourceFile  = File(cacheDir, "SAMPLE_CROPPED_IMAGE_NAME.png")
        var destinFile  = File(cacheDir, "SAMPLE_NAME.png")
        cropCaptureManager.startCropCapture(supportFragmentManager, R.id.container,  uri.toString(), Uri.fromFile(destinFile).toString())

    }

    companion object {
        @JvmField val REQUEST_PERMISSION = 2
        @JvmField val PIC_FILE_NAME = "pic.jpg"
        @JvmField val PIC_FILE_NAME2 = "pic2.jpg"
    }

    fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
    }



}
