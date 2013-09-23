package org.mili.learner.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class Engine {
    private EngineListener engineListener;
    private Statistics _statistics = new Statistics();
    //locale -> group -> key -> value
    private Map<String, Map<String, Map<String, String>>> model = new HashMap<String, Map<String, Map<String, String>>>();

    private String from;
    private RoundObject roundObject;

    public void setEngineListener(EngineListener engineListener) {
        this.engineListener = engineListener;
    }

    public static List<String> getLanguages() throws IOException {
        String data = FileUtils.readFully(Engine.class.getClassLoader().getResourceAsStream("languages.txt"));
        String[] lines = data.split("\n");

        List<String> locales = new ArrayList<String>();
        for (String line : lines) {
            if (line != null && line.length() > 0 && !line.startsWith("#")) {
                String[] array = line.split("_");
                locales.add(array[0]);
                locales.add(array[1]);
            }
        }
        return locales;
    }

    public String getQuestionsFile(String from, String to) throws IOException {
        String filename = String.format("%s_%s.txt", from, to);
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            return getQuestionsFile(to, from);
        }
        String data = FileUtils.readFully(is);
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (line != null && line.length() > 0 && !line.startsWith("#")) {
                Entry fromEntry = parseLine(line);
                fromEntry.locale = from;
                Entry toEntry = new Entry();
                toEntry.locale = to;
                toEntry.key = fromEntry.value;
                toEntry.value = fromEntry.key;
                toEntry.group = fromEntry.group;
                insert(fromEntry);
                insert(toEntry);
            }
        }
        return model.toString();
    }

    private void insert(Entry entry) {
        Map<String, Map<String, String>> groups = model.get(entry.locale);
        if (groups == null) {
            groups = new HashMap<String, Map<String, String>>();
            model.put(entry.locale, groups);
        }
        Map<String, String> keys = groups.get(entry.group);
        if (keys == null) {
            keys = new HashMap<String, String>();
            groups.put(entry.group, keys);
        }
        keys.put(entry.key, entry.value);
    }

    private Entry parseLine(String line) {
        Entry entry = new Entry();
        String[] groupAndKeyAndValue = line.split("=");
        entry.value = groupAndKeyAndValue[1];
        String[] groupAndKey = groupAndKeyAndValue[0].split("[.]");
        entry.group = groupAndKey[0];
        entry.key = groupAndKey[1];
        return entry;
    }

    public void start(String from, String to) throws IOException {
        this.from = from;
        model.clear();
        getQuestionsFile(from, to);
        initRound();
        engineListener.onGameStart();
        engineListener.onNewRound(roundObject);
        _statistics.roundStart();
    }

    public RoundObject initRound() {
        String group = pickGroup();
        String question = pickQuestion(group);
        String correctAnswer = getAnswerFor(group, question);
        List<String> answers = pickAnswersFor(group, question);
        roundObject = new RoundObject(question, correctAnswer, answers);
        return roundObject;
    }

    private List<String> pickAnswersFor(String group, String question) {
        Map<String, String> questions = model.get(from).get(group);
        String correctAnswer = questions.get(question);

        List<String> answers = new ArrayList<String>(questions.values());
        answers.remove(correctAnswer);
        Collections.shuffle(answers);

        List<String> result = new ArrayList<String>();
        result.add(correctAnswer);
        result.addAll(answers.subList(0, 3));

        Collections.shuffle(result);

        return result;
    }

    private String getAnswerFor(String group, String question) {
        return model.get(from).get(group).get(question);
    }

    private String pickQuestion(String group) {
        return pickRandom(new ArrayList<String>(model.get(from).get(group).keySet()));
    }

    private String pickGroup() {
        return pickRandom(new ArrayList<String>(model.get(from).keySet()));
    }

    private String pickRandom(List<String> list) {
        return list.get(getNumber(list.size() - 1));
    }

    private int getNumber(int max) {
        return (int) (Math.random() * max);
    }

    public void answer(String text) {
        if (roundObject.getCorrectAnswer().equals(text)) {
            engineListener.onRightAnswer();
            engineListener.onNewRound(initRound());
            _statistics.roundEnd();
            _statistics.roundStart();
            _statistics.right();
        } else {
            _statistics.roundEnd();
            engineListener.onWrongAnswer();
        }
    }

    public Statistics getStatistics() {
        return _statistics;
    }

    private class Entry {
        private String value;
        private String group;
        private String key;
        private String locale;
    }
}
