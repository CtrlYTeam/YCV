/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *----------------------------------------------------------------------------------
 *
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
/*
package org.firstinspires.ftc.teamcode;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.zedrobotics.ycv.GoldDetector;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.FileWriter;
import java.io.IOException;


// Extension of Iterative OpMode in FTC SDK.
// This OpMode uses OpenCV to detect a gold mineral. Telemetry back to Driver
// Station reports detection and position within camera frame of mineral. 
 

@TeleOp(name="Gold Mineral Detection Op", group="YCV Opmode")
//@Disabled
public class GoldDetectorOp extends LinearOpMode {

    static final String TAG = "GoldDetectorOp";   // Logging Tag

    public void runOpMode() {

        // Init Code
        boolean hasFrozenImage = false;            // flag to indicate if image is frozen or not
        ElapsedTime runtime = new ElapsedTime();   // Timer

        GoldDetector mdetector = new GoldDetector();
        //'hardwareMap.appContext' is magic. Dunno all its ramificationa yet...
        // This init method is exectured by OpenCVAgent to do all the camera/opencv magic.
        // arg: (context, cameraIndes)
        //       cameraIndex: 0 - back camera, 1 - front camera
        mdetector.init(hardwareMap.appContext,0);
        //OpenCVAgent init thread doesn't finish before reaching this statement...
        //Log.d(TAG, String.format("OpenCVAgent initialized = %b",  mdetector.isInitialized()));

        // Code after driver hits PLAY
        waitForStart();

        runtime.reset();

        // run repeatedly
        while (opModeIsActive()) {

            // If there is a gamepad attached to the Driver Station then use button Y
            // to freeze the image.
            if (gamepad1.y) {
                hasFrozenImage = true;
                mdetector.mCameraView.disableView();
            }

            // If there is a gamepad attached to the Driver Station then use button A
            // to unfreeze the image.
            if (gamepad1.a) {
                hasFrozenImage = false;
                mdetector.mCameraView.enableView();
            }

            //If there is a gamepad attached to the Driver Station then use button B
            //to save the image to file in the robot controller phone.
            if (gamepad1.b) {
                if (hasFrozenImage) {

                    if (Imgcodecs.imwrite("/sdcard/image.png", mdetector.fileMat)) {
                        telemetry.addData("Image", "written");
                    }
                    Imgcodecs.imwrite("/sdcard/image.jpg", mdetector.fileMat);
                    try {
                        FileWriter writer = new FileWriter("/sdcard/icontours.txt");
                        for (MatOfPoint c: mdetector.contourList) {
                            double area = Imgproc.contourArea(c);
                            writer.write(area + "  " +c.toString() + "\n");
                            for (Point p: c.toList()){
                                writer.write("x:"+p.x + " y:"+p.y+"\n");
                            }
                        }
                        writer.close();
                    } catch (IOException e){}

                }
                else {
                    telemetry.addData("Oops", "Press Y to freeze an image before saving it to file");
                }
            }

            telemetry.addData("hasFrozenImage" , hasFrozenImage);

        }

        // Code after driver hits STOP

        // Garbage collect CV objects
        if (mdetector != null)
            mdetector.terminate();

    }
}
*/