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

import android.util.Log;

import org.opencv.core.Mat;

/**
 *  Extends abstract class OpenCVAgent with minimalistic additional functionality
 *  as a instantiable entity.
 */
public class BasicAgent extends OpenCVAgent{

    private String TAG;         // Logging ID tag; set in constructor

    /**
     *  Constructor to instantiate bare-bones OpenCVAgent.
     *  No params or returns.
     */
    public BasicAgent() {
        super();
        // Universal way to set TAG to Class name.
        TAG = this.getClass().getSimpleName();
        Log.d(TAG, "in no-arg constructor");
    }

    /**
     *  Override of Method declared in OpenCVAgent.
     *  This method intentionally does nothing to process camera frames.
     *  @param  rgba 3-Channel Mat of RGB content
     *  @param  gray 1-Channel Mat of gray-scale content
     *  @return rgba without modifications.
     */
    @Override
    public Mat processCameraFrame(Mat rgba, Mat gray) {
        
        Log.d(TAG, "processing camera frame");

        gray.release();
        return rgba;
    }

 }