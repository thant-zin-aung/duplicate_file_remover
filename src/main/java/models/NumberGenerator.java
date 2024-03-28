package models;


public class NumberGenerator {
    public static int generate(int start,int end) {
        return (int)(Math.random()*(end-start))+start;
    }
}
