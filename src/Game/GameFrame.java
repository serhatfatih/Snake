package Game;

import javax.swing.*;

public class GameFrame extends JFrame{
    private GameFrame() {
        super("SPACE GAME");
        setResizable(false);
        setFocusable(false);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();

        gamePanel.requestFocus();
        gamePanel.addKeyListener(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.setFocusTraversalKeysEnabled(false);


        add(gamePanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
