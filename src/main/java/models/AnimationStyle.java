package models;

import models.manipulators.TimeManipulator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


public class AnimationStyle {

    private ScaleTransition scaleTransition;
    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;
    private FadeTransition fadeTransition;
    private ParallelTransition parallelTransition;
    private ParallelTransition sprinkleParallelTransition;

    public void playScaleEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                        double setFromX, double setFromY, double setToX, double setToY) {
        scaleTransition = new ScaleTransition(Duration.millis(duration),node);
        scaleTransition.setFromX(setFromX);
        scaleTransition.setFromY(setFromY);
        scaleTransition.setToX(setToX);
        scaleTransition.setToY(setToY);
        scaleTransition.setAutoReverse(setToAutoReverse);
        scaleTransition.setCycleCount(setCycleCount);
        scaleTransition.play();
    }

    public void playRotateEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                        double setFromAngle, double setToAngle) {
        rotateTransition = new RotateTransition(Duration.millis(duration),node);
        rotateTransition.setFromAngle(setFromAngle);
        rotateTransition.setToAngle(setToAngle);
        rotateTransition.setAutoReverse(setToAutoReverse);
        rotateTransition.setCycleCount(setCycleCount);
        rotateTransition.play();
    }

    public void playTranslateEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                         double setFromX , double setToX , double setFromY, double setToY) {
        translateTransition = new TranslateTransition(Duration.millis(duration),node);
        translateTransition.setFromX(setFromX);
        translateTransition.setToX(setToX);
        translateTransition.setFromY(setFromY);
        translateTransition.setToY(setToY);
        translateTransition.setAutoReverse(setToAutoReverse);
        translateTransition.setCycleCount(setCycleCount);
        translateTransition.play();
    }

    public void playFadeEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                double setFromValue, double setToValue) {
        fadeTransition = new FadeTransition(Duration.millis(duration),node);
        fadeTransition.setFromValue(setFromValue);
        fadeTransition.setToValue(setToValue);
        fadeTransition.setAutoReverse(setToAutoReverse);
        fadeTransition.setCycleCount(setCycleCount);
        fadeTransition.play();
    }

    public void playRotateTranslateEffect(Node node,double rotateDuration, int setRotateCycleCount, boolean setRotateAutoReverse,
                                                 double setRotateFromAngle, double setRotateToAngle , double translateDuration , int setTranslateCycleCount,
                                                 boolean setTranslateAutoReverse, double setTranslateFromX,  double setTranslateToX ,
                                                 double setTranslateFromY , double setTranslateToY, double setTranslateFromZ , double setTranslateToZ) {
        rotateTransition = new RotateTransition(Duration.millis(rotateDuration),node);
        rotateTransition.setFromAngle(setRotateFromAngle);
        rotateTransition.setToAngle(setRotateToAngle);
        rotateTransition.setAutoReverse(setRotateAutoReverse);
        rotateTransition.setCycleCount(setRotateCycleCount);

        translateTransition = new TranslateTransition(Duration.millis(translateDuration),node);
        translateTransition.setAutoReverse(setTranslateAutoReverse);
        translateTransition.setCycleCount(setTranslateCycleCount);
        translateTransition.setFromX(setTranslateFromX);
        translateTransition.setToX(setTranslateToX);
        translateTransition.setFromY(setTranslateFromY);
        translateTransition.setToY(setTranslateToY);
        translateTransition.setFromZ(setTranslateFromZ);
        translateTransition.setToZ(setTranslateToZ);

        parallelTransition = new ParallelTransition(rotateTransition,translateTransition);
        parallelTransition.play();
    }

    public void playTranslateFadeEffect(Node node, double translateDuration,double fadeDuration,int cycleCount,boolean isAutoReverse,
                                                        double translateFromX,double translateToX,double translateFromY,double translateToY,
                                                        double fadeFromValue,double fadeToValue) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(translateDuration),node);
        translateTransition.setCycleCount(cycleCount);
        translateTransition.setAutoReverse(isAutoReverse);
        translateTransition.setFromX(translateFromX);
        translateTransition.setToX(translateToX);
        translateTransition.setFromY(translateFromY);
        translateTransition.setToY(translateToY);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeDuration),node);
        fadeTransition.setCycleCount(cycleCount);
        fadeTransition.setAutoReverse(isAutoReverse);
        fadeTransition.setFromValue(fadeFromValue);
        fadeTransition.setToValue(fadeToValue);

        parallelTransition = new ParallelTransition(translateTransition,fadeTransition);
        parallelTransition.play();
    }

    public void playTranslateSequentialEffect(Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                              double setFromX , double setToX , double setFromY, double setToY,
                                              double duration1, int setCycleCount1, boolean setToAutoReverse1,
                                              double setFromX1 , double setToX1 , double setFromY1, double setToY1) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        translateTransition = new TranslateTransition(Duration.millis(duration),node);
        translateTransition.setFromX(setFromX);
        translateTransition.setToX(setToX);
        translateTransition.setFromY(setFromY);
        translateTransition.setToY(setToY);
        translateTransition.setAutoReverse(setToAutoReverse);
        translateTransition.setCycleCount(setCycleCount);

        sequentialTransition.getChildren().add(translateTransition);

        translateTransition = new TranslateTransition(Duration.millis(duration1),node);
        translateTransition.setFromX(setFromX1);
        translateTransition.setToX(setToX1);
        translateTransition.setFromY(setFromY1);
        translateTransition.setToY(setToY1);
        translateTransition.setAutoReverse(setToAutoReverse1);
        translateTransition.setCycleCount(setCycleCount1);

        sequentialTransition.getChildren().add(translateTransition);
        sequentialTransition.play();

    }

    private void sprinkleFlyingEffect(Node node, double duration, int[] sprinkleSize, Color sprinkleColor) {
        ObservableList<Circle> sprinkleList = FXCollections.observableArrayList();
        if ( node instanceof AnchorPane) {
            AnchorPane flyingNode = (AnchorPane) node;
            double layoutX = flyingNode.getPrefWidth();
            double layoutY = flyingNode.getPrefHeight();
            double maxSprinkles = ( layoutX > 1500 ) ? 50 : (layoutX > 1000 ) ? 35 : 20;
            double minSprinkles = ( layoutX > 1500 ) ? 40 : (layoutX > 1000 ) ? 25 : 15;
            TranslateTransition translateTransition = null;
            FadeTransition fadeTransition = null;
            sprinkleParallelTransition = new ParallelTransition();
            Circle sprinkle = null;

            for ( int id = 0 ; id < getRandomValue((int)minSprinkles,(int)maxSprinkles) ; id++ ) {
                sprinkle = new Circle(getRandomValue(sprinkleSize[0],sprinkleSize[1]));
                sprinkle.setFill(sprinkleColor);
                sprinkle.setLayoutX(getRandomValue(0,(int)layoutX));
                sprinkle.setLayoutY(layoutY+(sprinkle.getRadius())+10);

                translateTransition = new TranslateTransition(Duration.millis(duration),sprinkle);
                translateTransition.setAutoReverse(false);
                translateTransition.setCycleCount(1);
                translateTransition.setFromX(0);
                translateTransition.setFromY(0);
                // how far and how height
                translateTransition.setToX(getRandomValue(-80,180));
                translateTransition.setToY(-getRandomValue(100,150));

                fadeTransition = new FadeTransition(Duration.millis(duration),sprinkle);
                fadeTransition.setAutoReverse(false);
                fadeTransition.setCycleCount(1);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);

                sprinkleList.add(sprinkle);
                flyingNode.getChildren().add(sprinkle);

                sprinkleParallelTransition.getChildren().addAll(translateTransition,fadeTransition);
            }
            sprinkleParallelTransition.play();



        } else {
            System.out.println("Can't play sprinkle effect. [ Reason - Node is not AnchorPane ]");
        }
    }

    public void sprinkleFlyingEffect(Node node,double duration,int times,int delay,Color sprinkleColor) {
        new Thread( () -> {
            int timesCounter = 0;
            while ( timesCounter < times ) {
                Platform.runLater(() -> sprinkleFlyingEffect(node,duration,new int[]{3,8},sprinkleColor) );
                new TimeManipulator().waitTimerInMilliSec(delay);
                ++timesCounter;
            }
        }).start();
    }

    public void sprinkleFlyingEffect(Node node,double duration,int times,int delay,int[] sprinkleSize,Color sprinkleColor) {
        new Thread( () -> {
            int timesCounter = 0;
            while ( timesCounter < times ) {
                Platform.runLater(() -> sprinkleFlyingEffect(node,duration,sprinkleSize,sprinkleColor) );
                new TimeManipulator().waitTimerInMilliSec(delay);
                ++timesCounter;
            }
        }).start();
    }

    public void stopRotateEffect() { rotateTransition.stop(); }
    public void stopScaleEffect() {
        scaleTransition.stop();
    }
    public  void stopFadeEffect() {
        fadeTransition.stop();
    }
    public void stopSprinkleEffect() { sprinkleParallelTransition.stop(); }

    private double getRandomValue(int min , int max) {
        return NumberGenerator.generate(max,min);
    }
}
