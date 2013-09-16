package org.mili.learner.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class Engine {
    //locale -> group -> key -> value
    private Map<Locale, Map<String, Map<String, String>>> model = new HashMap<Locale, Map<String, Map<String, String>>>();

    public Set<String> getGroups(Locale locale) {
        return model.get(locale).keySet();
    }

    public Set<String> getKeysForGroup(Locale locale, String group) {
        return model.get(locale).get(group).keySet();
    }

    public List<Locale> getLanguages() throws IOException {
        String data = FileUtils.readFully(getClass().getClassLoader().getResourceAsStream("languages.txt"));
        String[] lines = data.split("\n");
        List<Locale> locales = new ArrayList<Locale>();
        for (String line : lines) {
            if (line != null && line.length() > 0 && !line.startsWith("#")) {
                String[] array = line.split("_");
                locales.add(Locale.forLanguageTag(array[0]));
                locales.add(Locale.forLanguageTag(array[1]));
            }
        }
        return locales;
    }

    public String getQuestionsFile(Locale from, Locale to) throws IOException {
        String filename = String.format("%s_%s.txt", from.getLanguage(), to.getLanguage());
        String data = FileUtils.readFully(getClass().getClassLoader().getResourceAsStream(filename));
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

    public static void main(String[] args) {
        Engine engine = new Engine();
        try {
            List<Locale> locales = engine.getLanguages();
            System.out.println(engine.getQuestionsFile(locales.get(0), locales.get(1)));
            System.out.println(engine.getGroups(locales.get(0)));
            System.out.println(engine.getKeysForGroup(locales.get(0), "chars"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Entry {
        private String value;
        private String group;
        private String key;
        private Locale locale;
    }
}
