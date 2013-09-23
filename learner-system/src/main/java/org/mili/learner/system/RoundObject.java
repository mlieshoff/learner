package org.mili.learner.system;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class RoundObject {
    private String _question;
    private List<String> _answers;
    private String _correctAnswer;

    public RoundObject(String question, String correctAnswer, List<String> answers) {
        _question = question;
        _answers = answers;
        _correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return _correctAnswer;
    }

    public String getQuestion() {
        return _question;
    }

    public List<String> getAnswers() {
        return _answers;
    }

    @Override
    public String toString() {
        return "RoundObject{" +
                "_question='" + _question + '\'' +
                ", _answers=" + _answers +
                ", _correctAnswer='" + _correctAnswer + '\'' +
                '}';
    }
}
