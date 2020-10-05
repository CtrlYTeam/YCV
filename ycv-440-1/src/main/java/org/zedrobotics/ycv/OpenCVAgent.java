/*
 * Copyright (c) 2019 Zed Robotics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * By downloading, copying, installing or using the software you agree to this license.
 * If you do not agree to this license, do not download, install,
 * copy or use the software.
*/

package org.zedrobotics.ycv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;

/**
 *  Abstract class to provide methods to enable, process with opencv, and display
 *  images from camera to user's Views.
 */
public abstract class OpenCVAgent implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OpenCVAgent";   // Logging Tag
    private static boolean loaded = false;             // Flag to indicate if opencv library module is loaded

    //
    //  Static block of code to execute only once, prior to constructor methods,
    //  to load opencv libraries. Failure to load is a critical non-recoverable
    //  error.
    //
    static {
        Log.d(TAG, "static method");
        try {
            // need to load libopencv_java3.so into project
            Log.d(TAG, "loading libopencv_java4.so");
            System.loadLibrary("opencv_java4");
            loaded = true;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "yo, buddy, looks like you didn't read the readme.md");
            Log.e(TAG, "you gotta get the libopencv_java3.so into the project!");
            loaded = false;
        }
    }

    private Context mContext = null;            // <<hardwareMap.appContext magic>>
    public JavaCameraView mCameraView = null;   // Means for OpenCV to access camera
    private boolean initialized = false;        // Flag to indicate if init() succeeded
    private int mResourceID = 0;                // Android Resource ID
    private ViewGroup mViewGroup = null;        // Object to execute addView() and removeView()

    private Mat rgba = new Mat();
    private Mat gray = new Mat();
    private Mat res = new Mat();

    private Mat mRgba;
    private Mat mRgbaF;
    private Mat mRgbaT;

    
    /**
     *  No frills constructor for OpenCVAgent.
     *  Intentionally doesn't do anything.
     *  No params or returns.
     */
    public OpenCVAgent() {
        Log.d(TAG, "constructor() in OpenCVAgent");
    }
    
    /**
     * Initializes OpenCVAgent.
     * Inner classes may access 'final' fields, so the init arguments use 'final' modifiers to declare them as such.
     * @param context     the application context. with FTC SDK, hardwareMap.appContext will be passed in
     * @param cameraIndex camera identifier: 0 = back camera, 1 = front camera.
     */
    
    public void init(final Context context, final int cameraIndex) {
        Log.d(TAG, "init() in OpenCVAgent");
        // 'this' refers to the object instantiated by the non-abstract class
        // that extends this class, abstract OpenCVAgent.
        // In order for the reference variable mListener, whose type is an interface,
        // OpenCVAgent, or its subclass referred as 'this', must implement the interface.
        // The reference variable uses the 'final' modifier to be accessible by the
        // anonymous inner class declared in method activity.runOnUiThread().
        //
        // We are instantiating an anonymous object that has the method onCameraFrame
        // from the CvCameraViewListener2 interface that will be used as a callback.
        // Then we are pointing that object to the object of the subclass that
        // extends this class.
        final CameraBridgeViewBase.CvCameraViewListener2 mListener = this;

        // Save the context for later usage in terminate()
        mContext = context;

        // We need to establish an Activity object so that we can leverage
        // off of to execute methods on the UI thread.
        final Activity activity = (Activity) mContext;

        // In FtcRobotController/res/layout/activity_ftc_controller.xml there is RelativeLayout
        // with the statement: android:id="@+id/RelativeLayout". We can get the ID for this resource.
        // This RelativeLayout is the View we want the put the camera image on and take off from.
        // The context is hardwareMap.appContext.
        mResourceID = mContext.getResources().getIdentifier("RelativeLayout", "id", mContext.getPackageName());
        // Android needs a ViewGroup object to connect the camera image to something
        // that will display the image on the phone.
        // We declare a ViewGroup and assign our RelativeLayout resource to it. This
        // gives some placement and dimension parameters to hold our image.
        mViewGroup = (ViewGroup) activity.findViewById(mResourceID);


        if (!loaded){
            Log.d(TAG, "Whoa, cowboy, can't init without loading libopencv_java3.so!");
        }
        else {

            // From Android Developers reference on View:
            // "You must always be on the UI thread when calling any method on any view."
            // So we instantiate a new anonymous Thread to do so.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "activity.run() in OpenCVAgent");

                    // Create JavaCameraView as a SurfaceView
                    //
                    // We instantiate JavaCameraView, which extends abstract CameraBridgeViewBase and View.
                    // This object will give us access to the camera image and methods to handle it.
//  FIX                  mCameraView = new JavaCameraView(context, cameraIndex);
                    mCameraView = new CustomCameraView(context, cameraIndex);
                    // Set which camera this is: 0 = back camera, 1 = front camera
                    mCameraView.setCameraIndex(cameraIndex);

                    // ?Set permission?
                    mCameraView.setCameraPermissionGranted();

                    // When a camera frame is retrieved, JavaCameraView.CameraWorker will be notified. (how?)
                    // The Thread run by CameraWorker will call method CameraBridgeViewBase.deliverAndDrawFrame().
                    // (This method happens to be overridden by CustomCameraView.) This method
                    // will call the method onCameraFrame() referred to by field mListener of CameraBridgeViewBase.
                    // We set the field mListener of CameraBridgeViewBase with this call. Our mListener argument
                    // was set above as the non-abstract instantiated subclass of this OpenCVAgent class.
                    mCameraView.setCvCameraViewListener(mListener);

                    // We use the ViewGroup's methods to connect the camera image to it.
                    mViewGroup.addView(mCameraView);

                    // Enable the camera
                    //
                    // Must call this method for CameraBridgeViewBase to provide images
                    //
                    // This method invokes a series of calls, resulting in launching of
                    // CameraWorker Thread:
                    //   CameraBridgeViewBase.checkCurrentState()
                    //     CameraBridgeViewBase.processEnterState()
                    //       CameraBridgeViewBase.onEnterStartedState()
                    //         JavaCameraView.connectCamera()
                    //            Thread(JavaCameraView.CameraWorker)
                    // CameraWorker waits for notification and setting of variables indicating
                    // a camera frame is ready. CameraWorker calls drawAndDeliverFrame()
                    // Since CustonCameraView extends JavaCameraView extends CameraBridgeViewBase
                    // all these methods are accessible by object mCameraView.
                    mCameraView.enableView();

                    Log.d(TAG, "JavaCameraView enabled");
                    initialized = true;

                }
            });
        }
    }

    public void terminate() {

        final Activity activity = (Activity) mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // We need to construct a ViewGroup so that we can disconnect the
                //final int resourceID = mResourceID;
                //ViewGroup mViewGroup = (ViewGroup) activity.findViewById(mResourceID);
                if (mCameraView != null) {
                    // Some utility for JavaCameraView
                    mCameraView.disableView();
                    // Call method to stop thread that updates images
                    mCameraView.disconnectCamera();
                    // Detach camera image from Layout -- will no longer see images on phone
                    mViewGroup.removeView(mCameraView);
                }
                mCameraView = null;
            }
        });

    }

    public boolean isInitialized() { return initialized; };

    /**
     * Method declared in CvCameraViewListener2
     * @param width -  the width the camera frame
     * @param height - the height the camera frame
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "called onCameraViewStarted");
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
        // does nothing in this abstract class
    }

    /**
     *  Method declared in CvCameraViewListener2
     */
    @Override
    public void onCameraViewStopped() {
        Log.d(TAG, "called onCameraViewStopped");
        mRgba.release();
        // does nothing in this abstract class
    }

    /**
     *  Method declared in CvCameraViewListener2 as a callback for this class's superclass.
     *  This method calls processCameraFrame, which needs to be overridden by this class's subclass.
     *  @param inputFrame interface declared in CameraViewBase with methods:
     *                      rgba() - returns RGBA Mat with frame from camera
     *                      gray() - returns single channel gray scale Mat with frame from camera
     *  @return the Mat returned from calling method processCameraFrame(Mat, Mat)
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.d(TAG, "called onCameraFrame");

        // This makes the screen go black for some reason.
        // But it works with CustomCameraView from endercv

        rgba.release();
        gray.release();
        res.release();
        rgba = new Mat();
        gray = new Mat();

        rgba = inputFrame.rgba();
        gray = inputFrame.gray();

        return res = processCameraFrame(rgba, gray);
    }
    
    /**
     * Override this method! This method is called from onCameraFrame().
     * @param rgba {@link Mat} in RGBA format
     * @param gray {@link Mat} in grayscale
     * @return     {@link Mat} displayable to the screen
     */     
    public abstract Mat processCameraFrame(Mat rgba, Mat gray);
    
     
 }