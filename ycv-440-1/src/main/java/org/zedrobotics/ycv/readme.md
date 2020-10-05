## YCV Module

There are two essential files that are the glue logic between the camera and openCV:
    OpenCVAgent.java     - from zedrobotics
    CustomCamerView.java - piece of code magic from Enderbots
    
OpenCVAgent.java is an abstract class that can be extended and used as the basis for
OpenCV processing. Override the OpenCVAgent method with your OpenCV code.
    public abstract Mat processCameraFrame(Mat rgba, Mat gray);
    
These files are example extensions of OpenCVAgent:
    BasicAgent.java   - minimalist example of extending OpenCVAgent
    GoldDetector.java - realistic processor for gold/yellow objects
    
This file is an example of an FTC OpMode that instantiates an OpenCV processing object
    GoldDetectorOp.java - instantiates GoldDetector.java
    
This file can be displayed on a laptop for GoldDetectorOp.java to detect:
    gold_cube.jpg
    
    
Note: User-written OpModes like GoldDetectorOp.java and OpenCV processors like 
GoldDetector.java should reside in the TeamCode module, not in the YCV module.