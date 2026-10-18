import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PeopleCounter {
    static {
        // Load your local OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Or if you know the exact path, use:
        // System.load("C:/path/to/opencv_java455.dll"); // Windows
        // System.load("/path/to/libopencv_java455.so"); // Linux
        // System.load("/path/to/libopencv_java455.dylib"); // Mac
    }

    private CascadeClassifier faceDetector;
    private CascadeClassifier fullBodyDetector;
    private VideoCapture capture;
    private JFrame frame;
    private JLabel cameraLabel;
    private JLabel countLabel;
    private int peopleCount = 0;

    public PeopleCounter() {
        // Initialize classifiers
        initializeDetectors();
        initializeGUI();
        initializeCamera();
    }

    private void initializeDetectors() {
        try {
            String opencvPath = "C:/Users/elkay/Desktop/opencv/opencv/"; // Change this to your OpenCV path
            String faceCascadePath = opencvPath + "sources/data/haarcascades/haarcascade_frontalface_default.xml";
            String bodyCascadePath = opencvPath + "sources/data/haarcascades/haarcascade_fullbody.xml";


            faceDetector = new CascadeClassifier(faceCascadePath);
            fullBodyDetector = new CascadeClassifier(bodyCascadePath);

            if (faceDetector.empty()) {
                System.err.println("Error: Could not load face detector from: " + faceCascadePath);
                // Try alternative path
                faceDetector = new CascadeClassifier();
                if (!faceDetector.load("haarcascade_frontalface_default.xml")) {
                    System.err.println("Failed to load face detector from alternative path");
                }
            }

            if (fullBodyDetector.empty()) {
                System.err.println("Error: Could not load body detector from: " + bodyCascadePath);
                // Try alternative path
                fullBodyDetector = new CascadeClassifier();
                if (!fullBodyDetector.load("haarcascade_fullbody.xml")) {
                    System.err.println("Failed to load body detector from alternative path");
                }
            }

            System.out.println("Detectors initialized successfully");

        } catch (Exception e) {
            System.err.println("Error initializing detectors: " + e.getMessage());
        }
    }

    private void initializeGUI() {
        frame = new JFrame("People Counter - OpenCV");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        cameraLabel = new JLabel();
        cameraLabel.setHorizontalAlignment(JLabel.CENTER);

        countLabel = new JLabel("People Count: 0", JLabel.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 20));
        countLabel.setForeground(Color.BLUE);
        countLabel.setOpaque(true);
        countLabel.setBackground(Color.WHITE);

        frame.add(cameraLabel, BorderLayout.CENTER);
        frame.add(countLabel, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void initializeCamera() {
        capture = new VideoCapture(0); // 0 for default camera

        if (!capture.isOpened()) {
            System.err.println("Error: Cannot open webcam");
            // Try different camera indices
            for (int i = 1; i < 3; i++) {
                capture = new VideoCapture(i);
                if (capture.isOpened()) {
                    System.out.println("Opened camera at index: " + i);
                    break;
                }
            }

            if (!capture.isOpened()) {
                JOptionPane.showMessageDialog(frame,
                        "Cannot open webcam!\nPlease check if camera is connected and not used by another application.",
                        "Camera Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Set camera resolution
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);

        System.out.println("Webcam opened successfully");
        System.out.println("OpenCV version: " + Core.VERSION);
    }

    public void startDetection() {
        Mat frameMat = new Mat();

        while (true) {
            if (capture != null && capture.isOpened() && capture.read(frameMat)) {
                if (!frameMat.empty()) {
                    // Detect people and update display
                    detectAndDisplay(frameMat);

                    // Small delay to control frame rate
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            // Check if window is closed
            if (!frame.isVisible()) {
                break;
            }
        }

        // Cleanup
        if (capture != null) {
            capture.release();
        }
        System.out.println("Camera released");
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faceDetections = new MatOfRect();
        MatOfRect bodyDetections = new MatOfRect();

        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // Detect faces
        if (faceDetector != null && !faceDetector.empty()) {
            faceDetector.detectMultiScale(
                    grayFrame,
                    faceDetections,
                    1.1,  // scale factor
                    3,    // min neighbors
                    0,    // flags
                    new Size(30, 30), // min size
                    new Size(300, 300) // max size
            );
        }

        // Detect full bodies
        if (fullBodyDetector != null && !fullBodyDetector.empty()) {
            fullBodyDetector.detectMultiScale(
                    grayFrame,
                    bodyDetections,
                    1.05,  // smaller scale factor for bodies
                    3,
                    0,
                    new Size(30, 100),
                    new Size(300, 400)
            );
        }

        // Count unique detections (use the maximum of face or body detections)
        int faceCount = faceDetections.toArray().length;
        int bodyCount = bodyDetections.toArray().length;
        peopleCount = Math.max(faceCount, bodyCount);

        // Draw rectangles around detected faces
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(
                    frame,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), // Green for faces
                    2
            );
            // Add label
            Imgproc.putText(
                    frame,
                    "Face",
                    new Point(rect.x, rect.y - 5),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    0.5,
                    new Scalar(0, 255, 0),
                    1
            );
        }

        // Draw rectangles around detected bodies
        for (Rect rect : bodyDetections.toArray()) {
            Imgproc.rectangle(
                    frame,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 0, 0), // Blue for bodies
                    2
            );
            // Add label
            Imgproc.putText(
                    frame,
                    "Body",
                    new Point(rect.x, rect.y - 5),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    0.5,
                    new Scalar(255, 0, 0),
                    1
            );
        }

        // Add people count to frame
        Imgproc.putText(
                frame,
                "People Count: " + peopleCount,
                new Point(10, 30),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                1,
                new Scalar(0, 0, 255), // Red color
                2
        );

        // Add detection info
        Imgproc.putText(
                frame,
                "Faces: " + faceCount + " Bodies: " + bodyCount,
                new Point(10, 60),
                Imgproc.FONT_HERSHEY_SIMPLEX,
                0.6,
                new Scalar(255, 255, 255),
                1
        );

        // Update GUI
        updateDisplay(frame);
        countLabel.setText("People Count: " + peopleCount);

        // Print to console
        System.out.println("Faces: " + faceCount + ", Bodies: " + bodyCount + ", Total: " + peopleCount);
    }

    private void updateDisplay(Mat mat) {
        Image image = matToBufferedImage(mat);
        ImageIcon icon = new ImageIcon(image);
        cameraLabel.setIcon(icon);
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        // Convert BGR to RGB for proper display in Java
        Mat rgbMat = new Mat();
        Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_BGR2RGB);

        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (rgbMat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }

        int bufferSize = rgbMat.channels() * rgbMat.cols() * rgbMat.rows();
        byte[] buffer = new byte[bufferSize];
        rgbMat.get(0, 0, buffer);

        BufferedImage image = new BufferedImage(rgbMat.cols(), rgbMat.rows(), type);
        final byte[] targetPixels = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);

        return image;
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            PeopleCounter counter = new PeopleCounter();
            // Start detection in a separate thread to keep GUI responsive
            new Thread(counter::startDetection).start();
        });
    }
}