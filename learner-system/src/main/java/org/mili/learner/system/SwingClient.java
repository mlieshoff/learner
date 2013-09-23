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
public class SwingClient implements EngineListener {
    private Engine engine;
    private JFrame frame;
    private StartPanel startPanel;
    private GamePanel gamePanel;

    private SwingClient() throws IOException {
        engine = new Engine();
        engine.setEngineListener(this);
        startPanel = new StartPanel();
        gamePanel = new GamePanel();
        frame = new JFrame("Learner");
        frame.setSize(640, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(BorderLayout.CENTER, startPanel);
    }

    @Override
    public void onGameStart() {
        frame.remove(startPanel);
        frame.add(BorderLayout.CENTER, gamePanel);
        frame.revalidate();
    }

    @Override
    public void onNewRound(RoundObject roundObject) {
        gamePanel.updateRound(roundObject);
    }

    @Override
    public void onRightAnswer() {
        gamePanel.onWin();
    }

    @Override
    public void onWrongAnswer() {
        gamePanel.onLoose();
    }

    private class StartPanel extends JPanel {
        private StartPanel() throws IOException {
            java.util.List<String> list = engine.getLanguages();
            String[] locales = new String[list.size()];
            for (int i = 0, n = list.size(); i < n; i ++) {
                locales[i] = list.get(i);
            }
            final JComboBox<String> from = new JComboBox<String>(locales);
            final JComboBox<String> to = new JComboBox<String>(locales);
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        engine.start((String) from.getSelectedItem(), (String) to.getSelectedItem());
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

        private Font font = new Font("Verdana", Font.BOLD, 24);

        private GamePanel() {
            question.setForeground(Color.BLACK);
            question.setFont(font);
            question.setHorizontalAlignment(JLabel.CENTER);
            setLayout(new BorderLayout());
            add(BorderLayout.CENTER, question);
            JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
            for (int i = 0; i < 4; i ++) {
                final JButton button = new JButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        engine.answer(button.getText());
                    }
                });
                buttons.add(button);
                buttonsPanel.add(button);
            }
            add(BorderLayout.SOUTH, buttonsPanel);
        }

        private void updateRound(RoundObject roundObject) {
            question.setForeground(Color.BLACK);
            question.setText(roundObject.getQuestion());
            java.util.List<String> answers = roundObject.getAnswers();
            for (int i = 0, n = buttons.size(); i < n; i ++) {
                JButton button = buttons.get(i);
                button.setText(answers.get(i));
            }
        }

        private void onWin() {
            question.setForeground(Color.GREEN);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void onLoose() {
            question.setForeground(Color.RED);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
