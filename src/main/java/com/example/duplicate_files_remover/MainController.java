package com.example.duplicate_files_remover;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AnimationStyle;
import models.FileManipulator;

public class MainController {
    private String drivePath;
    private final double scaleSpeed = 500;
    private final double translateDuration = 500;
    private final double rotateSpeed = 2000;
    private final double sprinkleSpeed = 3000;
    private boolean setHighPerformance = false;
    private final AnimationStyle animationStyle = new AnimationStyle();
    private final FileManipulator fileManipulator;
    @FXML
    private AnchorPane appRoot;
    @FXML
    private AnchorPane scanAnimateRoot;
    @FXML
    private VBox scanButton;
    @FXML
    public Label scanLabel,scanTypeLabel,totalFiles,duplicateFiles;
    @FXML
    public ProgressIndicator totalProgressIndicator,duplicateProgressIndicator;
    @FXML
    private Circle detailCircle1,detailCircle2;
    @FXML
    private HBox switchButton;
    @FXML
    private VBox removeDuplicateButton;

    public MainController() {
        fileManipulator = new FileManipulator();
    }

    public void initialize() {
        playRotateEffectOnDetailCircles();
        removeDuplicateButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            Thread deleteThread = new Thread(() -> {
                fileManipulator.removeDuplicateFiles(duplicateProgressIndicator,scanLabel);
            });
            deleteThread.start();
            if ( deleteThread.isAlive() ) {
                scanTypeLabel.setText("Deleting Files");
            }
        });
    }
    @FXML
    public void clickOnPerformanceSwitchButton() {
        if ( switchButton.getAlignment() == Pos.CENTER_LEFT ) {
            switchButton.setAlignment(Pos.CENTER_RIGHT);
            setHighPerformance = true;
        }
        else {
            switchButton.setAlignment(Pos.CENTER_LEFT);
            setHighPerformance = false;
        }
        System.out.println(setHighPerformance);
    }

    @FXML
    public void clickOnCloseButton() {
        System.exit(0);
    }

    @FXML
    public void clickOnMinimizeButton() {
        ((Stage)appRoot.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void clickOnScanButton() {

        Thread totalFileScanThread = new Thread(() -> {
            //        animationStyle.stopScaleEffect();
            Platform.runLater(()->scanLabel.setText("Scanning"));
//        playScaleEffectOnScanButton();
            playTranslateEffectOnIndicators(0,-8);
            try {
                Thread.sleep((long)translateDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                totalProgressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
//                duplicateProgressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            });
            Thread scanThread = new Thread(() -> {
                fileManipulator.scanTotalListOfFile(drivePath,scanLabel);
            });
            scanThread.start();
            try{
                scanThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // After scanning total files...
            playTranslateEffectOnIndicators(-8,0);

            double totalFileList = fileManipulator.getFileList().size();

            Task<Void> progressTask = new Task() {
                @Override
                protected Object call() throws Exception {
                    for ( int progressCount = 0 ; progressCount <= totalFileList ; progressCount++ ) {
                        updateProgress(progressCount,totalFileList);
                        Thread.sleep(1);
                    }

                    Platform.runLater(()->{
                        scanLabel.setText("SCAN");
                        scanTypeLabel.setText("Duplicate Files");
                    });
                    return true;
                }
            };

            totalProgressIndicator.progressProperty().bind(progressTask.progressProperty());
            progressTask.run();
        });


        Thread duplicateFileScanThread = new Thread(() -> {
            Platform.runLater(()->scanLabel.setText("Scanning"));
            fileManipulator.duplicateFileScanner(setHighPerformance,scanLabel,duplicateProgressIndicator,totalFiles,duplicateFiles,removeDuplicateButton);
        });

        // Thread running stage...
        if ( scanLabel.getText().equalsIgnoreCase("SCAN") && scanTypeLabel.getText().equalsIgnoreCase("Total Files")) {
            displayPathInput(totalFileScanThread);
        } else if (scanLabel.getText().equalsIgnoreCase("SCAN") && scanTypeLabel.getText().equalsIgnoreCase("Duplicate Files")) {
            duplicateFileScanThread.start();
        }

    }

    @FXML
    public void clickOnRemoveDuplicateButton() {
        System.out.println(fileManipulator.getFileList().size());
    }

    public void playScaleEffectOnScanButton() {
            new Thread(()-> {
                animationStyle.playScaleEffect(scanAnimateRoot,scaleSpeed,-1,true,1,1,1.1,1.1);
            }).start();
    }

    private void playRotateEffectOnDetailCircles() {
        new Thread( () -> {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            animationStyle.playRotateEffect(detailCircle1,rotateSpeed,-1,false,0,360);
            animationStyle.playRotateEffect(detailCircle2,rotateSpeed,-1,false,360,0);
        }).start();
    }

    private void playTranslateEffectOnIndicators(double fromY,double toY) {
        animationStyle.playTranslateEffect(totalProgressIndicator,translateDuration,1,false,0,0,fromY,toY);
        animationStyle.playTranslateEffect(duplicateProgressIndicator,translateDuration,1,false,0,0,fromY,toY);
    }

    private void displayPathInput(Thread totalScanThread) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            VBox root = new VBox();
            root.setFillWidth(false);
            Scene scene = new Scene(root,400,150);
            scene.setFill(Color.TRANSPARENT);
            root.setStyle("-fx-border-style: solid; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-border-color: #D14392; " +
                    "-fx-background-radius: 10px; -fx-background-color: #303040;");
            root.setAlignment(Pos.CENTER);
            root.setSpacing(20);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Label successLabel = new Label("Please enter drive scan path:");
            successLabel.setPadding(new Insets(0,0,0,10));
            successLabel.setTextFill(Color.WHITE);
            successLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 17px");
            Button okButton = new Button("OK");
            okButton.setTextFill(Color.WHITE);
            okButton.setPrefWidth(250);
            okButton.setPrefHeight(40);
            okButton.setStyle("-fx-border-style: solid; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #D14392;" +
                    "-fx-background-radius: 10px; -fx-background-color: #303040; -fx-cursor:hand;");
            HBox buttonWrapper = new HBox(okButton);
            buttonWrapper.setAlignment(Pos.CENTER);
            buttonWrapper.setPadding(new Insets(0,10,0,0));
            TextField inputBox = new TextField();
            inputBox.setPrefWidth(300);
            inputBox.setPrefHeight(40);
            inputBox.setStyle("-fx-background-color: #303040; -fx-background-radius: 10px; -fx-border-style: solid;" +
                    "-fx-border-width: 1px; -fx-border-color: #D14392; -fx-border-radius: 10px; -fx-text-fill: white;");
            okButton.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
                drivePath = inputBox.getText();
                totalScanThread.start();
                stage.close();
            });
            root.getChildren().addAll(successLabel,inputBox,buttonWrapper);
            stage.show();
        });

    }


}