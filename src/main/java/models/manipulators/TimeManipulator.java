package models.manipulators;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class TimeManipulator {
    private long startTime;
    private long endTime;

    public void startTime() {
        startTime = System.nanoTime();
    }

    public void endTime() {
        endTime = System.nanoTime();
    }

    public long getTotalTime() {
        return endTime-startTime;
    }

    public static void countDown(Label label , int downTime) {
        Task<String> countDownTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                int startDownTime = downTime;
                try {
                    while ( startDownTime > 0 ) {
                        updateMessage(String.valueOf(startDownTime));
                        Thread.sleep(1000);
                        --startDownTime;
                    }

                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                return null;
            }
        };
        Platform.runLater(() -> label.textProperty().bind(countDownTask.messageProperty()) );
        countDownTask.run();
    }

    public void waitTimer(int waitTime) {
        Thread waitThread = new Thread(() -> {
            for ( int id = 0 ; id < waitTime ; id++ ) {
                try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        waitThread.start();
        try {
            waitThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void waitTimerInMilliSec(int waitTime) {
        try {
            Thread.sleep(waitTime);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
