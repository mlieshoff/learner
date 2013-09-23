package org.mili.learner.system;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public interface EngineListener {
    void onRightAnswer();

    void onWrongAnswer();

    void onGameStart();

    void onNewRound(RoundObject roundObject);
}
