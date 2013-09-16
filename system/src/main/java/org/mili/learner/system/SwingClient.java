package org.mili.learner.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class SwingClient {
    private Engine engine = new Engine();
    private JFrame frame;
    private StartPanel startPanel;
    private GamePanel gamePanel;

    private SwingClient() throws IOException {
        startPanel = new StartPanel();
        gamePanel = new GamePanel();
        frame = new JFrame("Learner");
        frame.setSize(640, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(BorderLayout.CENTER, startPanel);
    }

    private void start(Locale from, Locale to) throws IOException {
        engine.init(from, to);
        gamePanel.nextRound(engine.initRound());
        frame.remove(startPanel);
        frame.add(BorderLayout.CENTER, gamePanel);
        frame.revalidate();
    }

    private class StartPanel extends JPanel {
        private StartPanel() throws IOException {
            java.util.List<Locale> list = engine.getLanguages();
            Locale[] locales = new Locale[list.size()];
            for (int i = 0, n = list.size(); i < n; i ++) {
                locales[i] = list.get(i);
            }
            final JComboBox<Locale> from = new JComboBox<Locale>(locales);
            from.setRenderer(new ListCellRenderer<Locale>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Locale> list, Locale value, int index, boolean isSelected, boolean cellHasFocus) {
                    return new JLabel(value.getDisplayLanguage());
                }
            });
            final JComboBox<Locale> to = new JComboBox<Locale>(locales);
            to.setRenderer(new ListCellRenderer<Locale>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Locale> list, Locale value, int index, boolean isSelected, boolean cellHasFocus) {
                    return new JLabel(value.getDisplayLanguage());
                }
            });
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        start((Locale) from.getSelectedItem(), (Locale) to.getSelectedItem());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            add(from);
            add(to);
            add(startButton);
        }
    }

    private class GamePanel extends JPanel {
        private JLabel question = new JLabel();
        private java.util.List<JButton> buttons = new ArrayList<JButton>();
        private RoundObject _roundObject;

        private GamePanel() {
            setLayout(new BorderLayout());

            add(BorderLayout.NORTH, question);

            JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));

            for (int i = 0; i < 4; i ++) {
                final JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (engine.check(_roundObject, button.getText())) {
                            gamePanel.nextRound(engine.initRound());
                        }
                    }
                });
                buttons.add(button);
                buttonsPanel.add(button);
            }

            add(BorderLayout.SOUTH, buttonsPanel);
        }

        public void nextRound(RoundObject roundObject) {
            _roundObject = roundObject;
            question.setText(_roundObject.getQuestion());
            java.util.List<String> answers = _roundObject.getAnswers();
            for (int i = 0, n = buttons.size(); i < n; i ++) {
                JButton button = buttons.get(i);
                button.setText(answers.get(i));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new SwingClient();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        });
    }
}
