package facepal;

import Detection.HaarFaceDetector;
import com.github.sarxos.webcam.Webcam;
//import facedetection.*;
import static facepal.Main.mainContainer;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * FXML Controller class
 *
 * @author bishw
 */
public class HomeController implements Initializable, ControlledScreen {

    private long timeCount = 0; //this is for checking a face repeatedly after a period
    // FaceDetection faceDetection; // this is for detecting faces
    int cont = 0;
    ScreenController myController;

    @FXML
    ComboBox<WebCamInfo> cbCameraOptions;
    @FXML
    ImageView imgWebCamCapturedImage;
    @FXML
    BorderPane bpWebCamPaneHolder;
    @FXML
    private Button startMonitoringButton;
    @FXML
    private Button stopMonitoringButton;
    @FXML
    ComboBox<WebCamInfo> secondaryCameraComboBox;
    @FXML
    Label welcomeUsername;

    public static Button start;
    public static Button stop;
    private static boolean stopCamera = false;
    private int primaryCameraId = -1;
    private int secondaryCameraId = -1;
    private boolean doorOpen = true;
    private ArrayList<Image> listOfImage = new ArrayList<Image>();
    ImageTobyteConvert imgToByte;
    public boolean flag = false;
    private Object[] received;
    private BufferedImage grabbedImage;
//	private WebcamPanel selWebCamPanel = null;
    private static Webcam selWebCam = null;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

    private String cameraListPromptText = "Choose Camera";
    private Button doorButton = new Button();
   
    private Object[] sendingData;
    public static boolean concurrent = false;
    public static Timer timer;

    String primaryCameraChosen = null, secondaryCameraChosen = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setScreenParent(ScreenController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        welcomeUsername.setText(WelcomeController.adminName);
        start = startMonitoringButton;
        stop = stopMonitoringButton;

        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        int webCamCounter = 0;
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
            // System.out.println(webCamCounter);
        }
        cbCameraOptions.setItems(options);
        secondaryCameraComboBox.setItems(options);
        cbCameraOptions.setPromptText(cameraListPromptText);

        secondaryCameraComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                    secondaryCameraId = arg2.getWebCamIndex();
//                    System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                    //initializeWebCam(arg2.getWebCamIndex());
//                     if(doorOpen==true){
//                   // closeCamera();
//                     initializeWebCam(secondaryCameraId);
//                     secondCameraAction(true);

//      }
                }
            }
        });

        cbCameraOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {

//                    System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                    primaryCameraId = arg2.getWebCamIndex();
                    initializeWebCam(arg2.getWebCamIndex());
                }
            }
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                setImageViewSize();
                backgroundTask();

            }
        });

//     //jokhon dorja khula er jonno request jabe tokhon secondary camera show korbe abar bondho hoye gele primary te fire jabe 
//    if(doorOpen==true){
//        closeCamera();
//        initializeWebCam(secondaryCameraId);
//      }
//    else if(doorOpen==false){
//        //primary camera abar start hobe 
//    }
    }
    
    void backgroundTask(){
         received = null;

                timer = new Timer();

                timer.schedule(
                        new TimerTask() {

                            @Override
                            public void run() {
//                                System.out.println("ping");
                                try {
                                    processApplication();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 0, 5000);
    }

    public void secondCameraAction(boolean signal) {

        if (signal == true && secondaryCameraId != -1) {

            //closeCamera();
            initializeWebCam(secondaryCameraId);
            grabbedImage = null;
            processOfSecondCamera();
//            timer = new Timer();
//
//                timer.schedule(
//                        new TimerTask() {
//
//                            @Override
//                            public void run() {
//                                System.out.println("ping");
//                                try {
//                                   processOfSecondCamera();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, 0, 5000);

        }

    }

    void processOfSecondCamera() {

        if (!stopCamera && grabbedImage != null) {

//            System.out.println("2nd thread running " + timeCount);
            if (listOfImage.size() > 0) {
                listOfImage.clear();
            }

            HaarFaceDetector hf = new HaarFaceDetector();
            listOfImage = hf.HaarFaceDetectorPart(grabbedImage, 1);
            cont = 1;
            if (listOfImage.size() > 0) {
                //server part 

                CommunicateServer.sendObject = new Object[listOfImage.size() + 2];

                CommunicateServer.sendObject[0] = 9;
                CommunicateServer.sendObject[1] = WelcomeController.adminId;

                int counter = 2;
                System.out.println("I got faces " + listOfImage.size());

                for (Image faces : listOfImage) {

                    BufferedImage bImage = SwingFXUtils.fromFXImage(faces, null);

                    BufferedImage gray = Detection.Conversion.createResizedCopy(bImage, 125, 150, true);
                    CommunicateServer.sendObject[counter++] = imgToByte.convertString(gray);

//                                try {
//                                    ImageIO.write(gray, "png", new File("E:\\output" + cont + ".png"));
//                                } catch (IOException ex) {
//                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                }
                sendingData = CommunicateServer.sendObject;
                concurrent = true;

                //myService.restart();
                CommunicateServer.callSendObject(sendingData, true);
                System.out.println("in service class: communicate callsendobject completed");
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                received = CommunicateServer.getObject();

                if (received != null) {
                    stopCamera = true;

                    grabbedImage = null;
                    initializeWebCam(primaryCameraId);
                    timer.cancel();
                    timer.purge();
                }

            }

        }
    }

    protected void setImageViewSize() {

        // double height = bpWebCamPaneHolder.getHeight();
        //double width = bpWebCamPaneHolder.getWidth();
        double height = 270.0;
        double width = 460.0;
        imgWebCamCapturedImage.setFitHeight(height);
        imgWebCamCapturedImage.setFitWidth(width);
        imgWebCamCapturedImage.prefHeight(height);
        imgWebCamCapturedImage.prefWidth(width);
        imgWebCamCapturedImage.setPreserveRatio(true);

    }

    protected void initializeWebCam(final int webCamIndex) {

        Dimension d = new Dimension(800, 600);

        Task<Void> webCamIntilizer = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                if (selWebCam == null) {
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setCustomViewSizes(new Dimension[]{d});
                    selWebCam.setViewSize(d);

                    selWebCam.open();
                } else {
                    closeCamera();
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setCustomViewSizes(new Dimension[]{d});
                    selWebCam.setViewSize(d);
                    selWebCam.open();

                }
                // processingCamera();
                startWebCamStream();

                return null;
            }

        };

        new Thread(webCamIntilizer).start();
        stopMonitoringButton.setDisable(false);
        startMonitoringButton.setDisable(true);

    }

    protected void startWebCamStream() {

        stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                while (!stopCamera) {
                    try {
                        if ((grabbedImage = selWebCam.getImage()) != null) {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    final Image mainiamge = SwingFXUtils
                                            .toFXImage(grabbedImage, null);
                                    imageProperty.set(mainiamge);
                                }
                            });

                            grabbedImage.flush();

                        }
                    } catch (Exception e) {
                    } finally {

                    }

                }

                return null;

            }

        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    void processApplication() throws InterruptedException {

        if (!stopCamera && grabbedImage != null && concurrent == false) {

            System.out.println("2nd thread running " + timeCount);
            if (listOfImage.size() > 0) {
                listOfImage.clear();
            }

            HaarFaceDetector hf = new HaarFaceDetector();
            listOfImage = hf.HaarFaceDetectorPart(grabbedImage, 1);
            cont = 1;
            if (listOfImage.size() > 0) {
                //server part 

                CommunicateServer.sendObject = new Object[listOfImage.size() + 2];

                CommunicateServer.sendObject[0] = 6;
                CommunicateServer.sendObject[1] = WelcomeController.adminId;

                int counter = 2;
                System.out.println("I got faces " + listOfImage.size());

                for (Image faces : listOfImage) {

                    BufferedImage bImage = SwingFXUtils.fromFXImage(faces, null);

                    BufferedImage gray = Detection.Conversion.createResizedCopy(bImage, 125, 150, true);
                    CommunicateServer.sendObject[counter++] = imgToByte.convertString(gray);

                    sendingData = CommunicateServer.sendObject;
                    concurrent = true;

                 
                    CommunicateServer.callSendObject(sendingData, true);
                    System.out.println("in service class: communicate callsendobject completed");
                    Thread.sleep(150);
                    received = CommunicateServer.getObject();

                    if (received != null) {
                        
                        for(int y=2;y< received.length && received[y] !=null ; y++)
                        {
                            try {
                                LogMessage.write("Y = " + y + "  value " + received[y]+"\n");
                            } catch (IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    concurrent = false;

                }

            }
        }

    }

    private static void closeCamera() {
        try {
            if (selWebCam != null) {
                selWebCam.close();
            }
        } catch (Exception e) {

        }
    }

    public void startAction(ActionEvent event) {
        stopCamera = false;
        startWebCamStream();
        startMonitoringButton.setDisable(true);
        stopMonitoringButton.setDisable(false);
        backgroundTask();
    }

    public void stopAction(ActionEvent event) {

        stopCamera = true;
        startMonitoringButton.setDisable(false);
        stopMonitoringButton.setDisable(true);
         HomeController.timer.cancel();
         HomeController.timer.purge();
    }

    public static void cameraDispose() {

        stopCamera = true;
        start.setDisable(false);
        stop.setDisable(true);
        closeCamera();
        //Webcam.shutdown();
    }

    class WebCamInfo {

        private String webCamName;
        private int webCamIndex;

        public String getWebCamName() {
            return webCamName;
        }

        public void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }

        public int getWebCamIndex() {
            return webCamIndex;
        }

        public void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }

    }
    
   

    @FXML
    private void homeAction(ActionEvent e) {
        myController.setScreen(Main.screen2ID);
    }

    @FXML
    private void addAction(ActionEvent e) {
        myController.setScreen(Main.screen3ID);
    }

    @FXML
    private void showAction(ActionEvent e) {
        mainContainer.loadScreen(Main.screen4ID, Main.screen4file);
        myController.setScreen(Main.screen4ID);
//        ShowPeopleController.trigger.setDisable(false);
//        ShowPeopleController.trigger.fire();

    }

    @FXML
    private void settingsAction(ActionEvent e) {
        myController.setScreen(Main.screen5ID);
    }

    @FXML
    private void logoutAction(ActionEvent e) {
        cameraDispose();
        HomeController.timer.cancel();
         HomeController.timer.purge();
        myController.setScreen(Main.screen1ID);

        WelcomeController.logIn.setText("");
         
    }

}
