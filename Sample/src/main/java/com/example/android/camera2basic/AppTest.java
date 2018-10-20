package com.example.android.camera2basic;

import android.app.Application;

import andro.capture.core.CropCaptureManager;
import andro.capture.crop.CropCaptureFactory;
import andro.capture.take.TakeCaptureFactory;
import andro.capture.core.TakeCaptureManager;

public class AppTest extends Application {

    public TakeCaptureManager takeCaptureManager;

    public CropCaptureManager cropCaptureManager ;

    @Override
    public void onCreate() {
        super.onCreate();

        cropCaptureManager = CropCaptureFactory.Companion.createCropCaptureManager();
    }
}
