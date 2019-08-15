package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends JFrame {
    public static void main(String[] args) {
        new GameFrame(); // let the game begin
    }
}

class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("SNAKE GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(new GamePanel());

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

}

class GamePanel extends JPanel {
    private static int SIZE = 300;

    private Timer snakeTimer = new Timer(500, e -> tick());
    private Timer appleTimer = new Timer(1000, e -> tack());

    private boolean draw = true, right = false, left = false, up = true, down = false, eaten = false, toRight = false, toLeft = false, toUp = false, toDown = false, onSnake = false;

    private List<BodyPart> snake;
    private Apple apple;

    private Random r = new Random();

    private int xCoor = 15, yCoor = 15, size = 1, lastX, lastY;
    private int ticks = 0; // time
    private int appleCount = 0;

    private int x, y;

    public GamePanel() {
        setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_RIGHT && !left) {
                    right = true;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_LEFT && !right) {
                    left = true;
                    up = false;
                    down = false;
                }

                if (key == KeyEvent.VK_UP && !down) {
                    up = true;
                    right = false;
                    left = false;
                }

                if (key == KeyEvent.VK_DOWN && !up) {
                    down = true;
                    right = false;
                    left = false;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        setPreferredSize(new Dimension(SIZE, SIZE));

        snake = new ArrayList<>();
        snake.add(new BodyPart(xCoor, yCoor));

        x = r.nextInt(29);
        y = r.nextInt(29);
        apple = new Apple(x, y);

        start();
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, SIZE, SIZE);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, SIZE, SIZE);

        for (BodyPart b : snake) b.draw(g);

        if (apple != null) {
            if (!draw)
                apple.draw(g, lastX, lastY);
             else
                apple.draw(g);
        }
    }

    private void start() {
        // thread for snake
        new Thread(() -> {
            while (true) {
                snakeTimer.start();
                repaint();
            }
        }).start();

        //thread for apples
        new Thread(() -> {
            while (true) {
                appleTimer.start();
                //repaint();
            }
        }).start();
    }

    public void tick() {
        ticks += 500;

        if (right) xCoor++;
        if (left) xCoor--;
        if (up) yCoor--;
        if (down) yCoor++;

        snake.add(new BodyPart(xCoor, yCoor));

        if (snake.size() > size)
            snake.remove(0);

        boolean border = xCoor < 0 || xCoor > 29 || yCoor < 0 || yCoor > 29;

        for (int i = 0; i < snake.size(); i++)
            if (xCoor == snake.get(i).getX() && yCoor == snake.get(i).getY())
                if (i != snake.size() - 1 || border) {
                    draw = false;
                    lastX = apple.getX();
                    lastY = apple.getY();
                    JOptionPane.showMessageDialog(this, "Game Over....\n" + (ticks / 1000.0) + " sn geçti.\n" + appleCount + " tane elma yediniz.");
                    System.exit(0);
                }

    }

    private void util(int rand) {
        switch (rand) {
            case 0:
                toRight = true;
                toLeft = false;
                toUp = false;
                toDown = false;
                break;
            case 1:
                toRight = false;
                toLeft = true;
                toUp = false;
                toDown = false;
                break;
            case 2:
                toRight = false;
                toLeft = false;
                toUp = true;
                toDown = false;
                break;
            case 3:
                toRight = false;
                toLeft = false;
                toUp = false;
                toDown = true;
                break;
        }

        if (toRight) x++;
        if (toLeft) x--;
        if (toUp) y--;
        if (toDown) y++;

        onSnake = false;

        for (BodyPart bp : snake)
            if (bp.getX() == x && bp.getY() == y)
                onSnake = true;
    }

    public void tack() {
        if (xCoor == apple.getX() && yCoor == apple.getY()) {
            eaten = true;
            size++;
            appleCount++;
            apple = null;
        }

        int rand = r.nextInt(4);

        util(rand);

        if (x < 0 || x > 29 || y < 0 || y > 29 || onSnake)
            while (x < 0 || x > 29 || y < 0 || y > 29 || onSnake) {
                util(rand);
                rand = r.nextInt(4);
            }


        if (eaten) {
            eaten = false;
            x = r.nextInt(29);
            y = r.nextInt(29);

            for (BodyPart b : snake)
                if (b.getX() == x && b.getY() == y)
                    while (b.getX() == x && b.getY() == y) {
                        x = r.nextInt(29);
                        y = r.nextInt(29);
                    }
        }

        apple = new Apple(x, y);

    }
}

class BodyPart {
    private int x, y, size;

    public BodyPart(int x, int y) {
        this.x = x;
        this.y = y;
        size = 10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x * size, y * size, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class Apple {
    private int x, y, size;

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
        size = 10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * size, y * size, size, size);
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.RED);
        g.fillRect(x * size, y * size, size, size);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
