package org.mili.learner.system;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 17.09.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class Statistics implements Serializable {

    private long totalTime;
    private long roundTime;
    private int numberOfTotalAnswers;
    private int numberOfRightAnswers;

    public void right() {
        numberOfRightAnswers ++;
    }

    public void roundStart() {
        roundTime -= System.currentTimeMillis();
    }

    public void roundEnd() {
        roundTime += System.currentTimeMillis();
        totalTime += roundTime;
        numberOfTotalAnswers ++;
    }

    public int getNumberOfTotalAnswers() {
        return numberOfTotalAnswers;
    }

    public int getNumberOfRightAnswers() {
        return numberOfRightAnswers;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getAvgRoundTime() {
        if (numberOfTotalAnswers == 0) {
            return 0;
        }
        return (int) ((totalTime / numberOfTotalAnswers) / 1000);
    }
}
