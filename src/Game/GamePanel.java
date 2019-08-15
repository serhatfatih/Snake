package Game;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private class Ates {
        private int x;
        private int y;

        public Ates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private Timer timer = new Timer(1, this);
    private List<Ates> atesler = new ArrayList<>();
    private BufferedImage image;

    private int gecenSure = 0;
    private int harcananAtes = 0;
    private int addAtesY = 1;
    private int topX = 0;
    private int addTopX = 2;
    private int spaceShipX = 0;
    private int addSpaceShipX = 20;


    public GamePanel() {
        try {
            image = ImageIO.read(new FileInputStream("uzay gemisi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBackground(Color.WHITE);
        timer.start();
    }

    private boolean check() {
        for (Ates ates : atesler)
            if (new Rectangle(ates.getX(), ates.getY(), 5, 10).intersects(new Rectangle(topX, 0, 20, 20)))
                return true;

        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        gecenSure += 3;

        g.setColor(Color.RED);
        g.fillOval(topX, 0, 20, 20);

        g.drawImage(image, spaceShipX, 488, image.getWidth()/4, image.getHeight()/4, this);

        for (Ates ates : atesler)
            if (ates.getY() < 0)
                atesler.remove(ates);

        g.setColor(Color.BLUE);

        for (Ates ates : atesler)
            g.fillRect(ates.getX(), ates.getY(), 5, 10);

        if (check()) {
            timer.stop();
            String msg = "Oyun bitti...\n" +
                         "Harcanan Ateş : " + harcananAtes +
                         "\nGeçen Süre: " + gecenSure / 1000.0;

            JOptionPane.showMessageDialog(this, msg);
            System.exit(0);
        }

    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Ates ates : atesler)
            ates.setY(ates.getY() - addAtesY);


        topX += addTopX;

        if (topX >= 770 || topX <= 0)
            addTopX = -addTopX;

        repaint();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT){
            if (spaceShipX >= 740)
                spaceShipX = 740;
            else
                spaceShipX += addSpaceShipX;
        }

        else if (key == KeyEvent.VK_LEFT) {
            if (spaceShipX <= 0)
                spaceShipX = 0;
            else
                spaceShipX -= addSpaceShipX;
        }

        else if (key == KeyEvent.VK_CONTROL) {
            atesler.add(new Ates(spaceShipX + 19, 485));

            harcananAtes++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
