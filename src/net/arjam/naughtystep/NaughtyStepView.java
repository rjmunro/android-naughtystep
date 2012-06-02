package net.arjam.naughtystep;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;

class NaughtyStepView extends NaughtyStepCvViewBase {
    private Mat mRgba;
    private Mat mOrigMat;

    public NaughtyStepView(Context context) {
        super(context);
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            // initialize Mats before usage
            mRgba = new Mat();
            mOrigMat = new Mat();
        }

        super.surfaceCreated(holder);
    }

    @Override
    protected Bitmap processFrame(VideoCapture capture) {
        capture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
        /*
        if (mOrigMat == null) {
        	mOrigMat = mRgba.clone();
        } else {
            Point result = Imgproc.phaseCorrelate(mRgba, mOrigMat);
        }
        */
        Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);

        try {
        	Utils.matToBitmap(mRgba, bmp);
            return bmp;
        } catch(Exception e) {
        	Log.e("net.arjam.naughtystep", "Utils.matToBitmap() throws an exception: " + e.getMessage());
            bmp.recycle();
            return null;
        }
    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (mRgba != null)
                mRgba.release();
            if (mOrigMat != null)
                mOrigMat.release();

            mRgba = null;
            mOrigMat = null;
        }
    }
}
