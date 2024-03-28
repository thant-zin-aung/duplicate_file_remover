package models;

import com.example.duplicate_files_remover.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileManipulator {
    private final double scaleSpeed = 500;
    private AnimationStyle animationStyle;
    private static final List<String> excludeExtList = new ArrayList<>(List.of("cr2","file","ffff"));
    private final List<File> fileList;
    private int totalFileListCount;
    private int duplicateFileListCount=100;
    private final Set<File> duplicateFileList;

    private boolean isScanThreadFinished;

    public FileManipulator() {
        animationStyle = new AnimationStyle();
        fileList = new ArrayList<>();
        duplicateFileList = new HashSet<>();
//        scanTotalListOfFile("C:\\Users\\Blacksky\\Desktop\\Master Spring framework, Spring Boot, REST, JPA, Hibernate 2022-3",fileList);

//
//        duplicateFileList.forEach( df -> System.out.println(df.getAbsolutePath()));
//        System.out.println(getExtFromFile("blacksky.mpegf"));
//        System.out.println(getFilenameWithoutExt("blacksky.mpegf"));
    }

    public boolean isScanThreadFinished() {
        return this.isScanThreadFinished;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void scanTotalListOfFile(String directoryName, Label updateFileLabel) {
        File directory = new File(directoryName);
        // Get all files from a directory.
        File[] files = directory.listFiles();
        if(files != null)
            for (File file : files) {
                if (file.isFile()) {
//                    System.out.println(file.getName());
                    synchronized (this) {
                        fileList.add(file);
                        ++totalFileListCount;
                        Platform.runLater(() -> updateFileLabel.setText(String.valueOf(totalFileListCount)) );
                    }
                } else if (file.isDirectory()) {
                    scanTotalListOfFile(file.getAbsolutePath(),updateFileLabel);
                }
            }
    }
    public void duplicateFileScanner(boolean setHighPerformance, Label updateFileLabel, ProgressIndicator duplicateProgress, Label totalFiles, Label duplicateFiles, VBox removeDuplicateButton) {

        Task<Void> progressTask = new Task() {
            @Override
            protected Object call() throws Exception {
                List<File> tempFileList = new ArrayList<>(fileList);
                if ( setHighPerformance ) {
                    fileList.parallelStream().forEach( file -> {
                        tempFileList.parallelStream().forEach(tempFile -> {
                            if ( isFileDuplicate(file,tempFile,duplicateFileList) ) {
                                duplicateFileList.add(tempFile);
                                ++duplicateFileListCount;
                                Platform.runLater(()->updateFileLabel.setText(String.valueOf(duplicateFileListCount)));
                                updateProgress(duplicateFileListCount,totalFileListCount);
                            }
                        });
                    });
                } else {
                    fileList.forEach( file -> {
                        tempFileList.forEach(tempFile -> {
                            if ( isFileDuplicate(file,tempFile,duplicateFileList) ) {
                                duplicateFileList.add(tempFile);
                                ++duplicateFileListCount;
                                Platform.runLater(()->updateFileLabel.setText(String.valueOf(duplicateFileListCount)));
                                updateProgress(duplicateFileListCount,totalFileListCount);
                            }
                        });
                    });
                }

                Platform.runLater(()->{
                    totalFiles.setText("Total Files - "+totalFileListCount);
                    duplicateFiles.setText("Duplicate Files - "+duplicateFileListCount);
                });

                removeDuplicateButton.setVisible(true);
                animationStyle.playScaleEffect(removeDuplicateButton,scaleSpeed,1,false,0,0,1,1);

                return true;
            }
        };

        Platform.runLater(()->duplicateProgress.progressProperty().bind(progressTask.progressProperty()));
        progressTask.run();

    }

    public void removeDuplicateFiles(ProgressIndicator duplicateProgress, Label updateFileLabel) {
        Task<Void> progressTask = new Task() {
            int deleteFileCount;
            @Override
            protected Object call() throws Exception {
//                for ( File filename : duplicateFileList ) {
//                    filename.delete();
//                    deleteFileCount++;
//                    updateProgress(deleteFileCount,duplicateFileListCount);
//                    Platform.runLater(()->updateFileLabel.setText(String.valueOf(duplicateFileListCount-deleteFileCount)));
//                }

                for ( int count = 0 ; count < duplicateFileListCount ; count++ ) {
                    Thread.sleep(1);
                    deleteFileCount++;
                    updateProgress(deleteFileCount,duplicateFileListCount);
                    Platform.runLater(()->updateFileLabel.setText(String.valueOf(duplicateFileListCount-deleteFileCount)));
                }
                displaySuccessBox();
                return true;
            }
        };

        Platform.runLater(() -> duplicateProgress.progressProperty().bind(progressTask.progressProperty()));
        progressTask.run();

    }
    private boolean isFileDuplicate(File file , File tempFile , Set<File> duplicateFileList ) {

        return !file.getAbsolutePath().equals(tempFile.getAbsolutePath()) && file.getName().equals(tempFile.getName()) &&
                !duplicateFileList.contains(file) && file.length() == tempFile.length()
                || ( !file.getAbsolutePath().equals(tempFile.getAbsolutePath()) &&
                !duplicateFileList.contains(file) && getFilenameWithoutExt(file.getName()).equals(getFilenameWithoutExt(tempFile.getName())) &&
                excludeExtList.contains(getExtFromFile(tempFile.getName())) );

    }
    private String getExtFromFile(String filename) {
        int dotIndex = 0;
        for ( int index = filename.length()-1 ; index >= 0 ; index-- ) {
            if ( filename.charAt(index) == '.' ) {
                dotIndex = index;
                break;
            }
        }
        return filename.substring(++dotIndex);
    }
    private String getFilenameWithoutExt(String filename) {
        int dotIndex = 0;
        for ( int index = filename.length()-1 ; index >= 0 ; index-- ) {
            if ( filename.charAt(index) == '.' ) {
                dotIndex = index;
                break;
            }
        }
        return filename.substring(0,dotIndex);
    }

    private boolean isExtSame(String oriFile , String oldFile) {
        return getExtFromFile(oriFile).equals(getExtFromFile(oldFile));
    }

    public void displaySuccessBox() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            VBox root = new VBox();
            Scene scene = new Scene(root,400,150);
            scene.setFill(Color.TRANSPARENT);
            root.setStyle("-fx-border-style: solid; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-border-color: #D14392; " +
                    "-fx-background-radius: 10px; -fx-background-color: #303040;");
            root.setAlignment(Pos.CENTER);
            root.setSpacing(20);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Label successLabel = new Label("Progress finished successfully.");
            successLabel.setPadding(new Insets(0,0,0,10));
            successLabel.setTextFill(Color.WHITE);
            successLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 17px");
            Button okButton = new Button("OK");
            okButton.setTextFill(Color.WHITE);
            okButton.setPrefWidth(250);
            okButton.setPrefHeight(40);
            okButton.setStyle("-fx-border-style: solid; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #D14392;" +
                    "-fx-background-radius: 10px; -fx-background-color: #303040; -fx-cursor:hand;");
            okButton.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
                stage.close();
            });
            HBox buttonWrapper = new HBox(okButton);
            buttonWrapper.setAlignment(Pos.CENTER);
            buttonWrapper.setPadding(new Insets(0,10,0,0));
            root.getChildren().addAll(successLabel,buttonWrapper);
            stage.show();
        });

    }
}
