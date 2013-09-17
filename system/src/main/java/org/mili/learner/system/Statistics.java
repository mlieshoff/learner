package org.mili.learner.system;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 17.09.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class Statistics {

    private long totalTime;
    private long roundTime;
    private int rounds;
    private int fails;

    public void fail() {
        fails ++;
    }

    public void roundStart() {
        roundTime = System.currentTimeMillis();
    }

    public void roundEnd() {
        roundTime -= System.currentTimeMillis();
        totalTime += roundTime;
        rounds ++;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "totalTime=" + totalTime +
                ", roundTime=" + roundTime +
                ", rounds=" + rounds +
                ", fails=" + fails +
                '}';
    }
}
