package com.zetcode;

import com.zetcode.sprite.Moleque;
import com.zetcode.sprite.SenhoraChinelo;
import com.zetcode.sprite.Chinelo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class Board extends JPanel implements Runnable {

    private Dimension d;
    private List<Moleque> moleques;
    private SenhoraChinelo senhoraChinelo;
    private Chinelo chinelo;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/fantasma.png";
    private String message = "Game Over";

    private Timer timer;
    private Thread animator;

    private Semaphore molequeSemaphore;

    private BufferedImage bufferImage;

    public Board() {
        initBoard();
        gameInit();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
    }

    private void gameInit() {
        moleques = new ArrayList<>();
        molequeSemaphore = new Semaphore(Commons.NUMBER_OF_ALIENS_TO_DESTROY);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var moleque = new Moleque(Commons.ALIEN_INIT_X + 18 * j, Commons.ALIEN_INIT_Y + 18 * i);
                moleques.add(moleque);
            }
        }

        senhoraChinelo = new SenhoraChinelo();
        chinelo = new Chinelo();

        // Start the game loop thread
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (bufferImage == null) {
            bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics bufferGraphics = bufferImage.getGraphics();
        bufferGraphics.setColor(Color.black);
        bufferGraphics.fillRect(0, 0, d.width, d.height);
        bufferGraphics.setColor(Color.green);

        if (inGame) {
            bufferGraphics.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawMoleques(bufferGraphics);
            drawSenhoraChinelo(bufferGraphics);
            drawChinelo(bufferGraphics);
            drawMamonas(bufferGraphics);
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            gameOver(bufferGraphics);
        }

        g.drawImage(bufferImage, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);
    }

    private void update(){    
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
        inGame = false;
        timer.stop();
        message = "Game won!";
        }

    // player
    senhoraChinelo.act();

    // shot
    if (chinelo.isVisible()) {
        int chineloX = chinelo.getX();
        int chineloY = chinelo.getY();

        for (Moleque moleque : moleques) {
            int molequeX = moleque.getX();
            int molequeY = moleque.getY();

            if (moleque.isVisible() && chinelo.isVisible()) {
                if (chineloX >= (molequeX) && chineloX <= (molequeX + Commons.ALIEN_WIDTH) && chineloY >= (chineloY)
                        && chineloY <= (molequeY + Commons.ALIEN_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    moleque.setImage(ii.getImage());
                    moleque.setDying(true);
                    deaths++;
                    chinelo.die();

                    // acquire the semaphore before launching the alien's bomb
                    try {
                        molequeSemaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // launch the alien's bomb
                    Moleque.Mamona mamona= moleque.getMamona();
                    mamona.setDestroyed(false);
                    mamona.setX(moleque.getX());
                    mamona.setY(moleque.getY());
                }
            }
        }

        int y = chinelo.getY();
        y -= 6;

        if (y < 0) {
            chinelo.die();
        } else {
            chinelo.setY(y);
        }
    }

    // aliens
    for (Moleque moleque : moleques) {
        int x = moleque.getX();

        if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
            direction = -1;

            Iterator<Moleque> i1 = moleques.iterator();
            while (i1.hasNext()) {
                Moleque a2 = i1.next();
                a2.setY(a2.getY() + Commons.GO_DOWN);
            }
        }

        if (x <= Commons.BORDER_LEFT && direction != 1) {
            direction = 1;

            Iterator<Moleque> i2 = moleques.iterator();
            while (i2.hasNext()) {
                Moleque a = i2.next();
                a.setY(a.getY() + Commons.GO_DOWN);
            }
        }
    }

    Iterator<Moleque> it = moleques.iterator();

    while (it.hasNext()) {
        Moleque alien = it.next();

        if (alien.isVisible()) {
            int y = alien.getY();

            if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                inGame = false;
                message = "Invasion!";
            }

            alien.act(direction);
        }
    }

    // bombs
    var generator = new Random();

    for (Moleque moleque : moleques) {
        int shot = generator.nextInt(15);
        Moleque.Mamona mamona = moleque.getMamona();

        if (shot == Commons.CHANCE && moleque.isVisible() && mamona.isDestroyed()) {
            mamona.setDestroyed(false);
            mamona.setX(moleque.getX());
            mamona.setY(moleque.getY());
        }

        int mamonaX = mamona.getX();
        int mamonaY = mamona.getY();
        int senhoraChineloX = senhoraChinelo.getX();
        int senhoraChineloY = senhoraChinelo.getY();

        if (senhoraChinelo.isVisible() && !mamona.isDestroyed()) {
            if (mamonaX >= (senhoraChineloX) && mamonaX <= (senhoraChineloX + Commons.PLAYER_WIDTH) && mamonaY >= (senhoraChineloY) && mamonaY <= (senhoraChineloY + Commons.PLAYER_HEIGHT)) {

                var ii = new ImageIcon(explImg);
                senhoraChinelo.setImage(ii.getImage());
                senhoraChinelo.setDying(true);
                mamona.setDestroyed(true);

                // release the semaphore after the bomb has been launched
                molequeSemaphore.release();
            }
        }

        if (!mamona.isDestroyed()) {
            mamona.setY(mamona.getY() + 1);

            if (mamona.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                mamona.setDestroyed(true);
            }
        }
    }
}

    private void doGameCycle() {
        update();
        repaint();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (inGame) {
            doGameCycle();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = 10 * Commons.DELAY - timeDiff % (10 * Commons.DELAY);

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            senhoraChinelo.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            senhoraChinelo.keyPressed(e);

            int x = senhoraChinelo.getX();
            int y = senhoraChinelo.getY();
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!chinelo.isVisible()) {
                        chinelo = new Chinelo(x, y);
                    }
                }
            }
        }
    }

    private void drawMoleques(Graphics g) {
        for (Moleque moleque : moleques) {
            if (moleque.isVisible()) {
                g.drawImage(moleque.getImage(), moleque.getX(), moleque.getY(), this);
            }

            if (moleque.isDying()) {
                moleque.die();
            }
        }
    }

    private void drawSenhoraChinelo(Graphics g) {
        if (senhoraChinelo.isVisible()) {
            g.drawImage(senhoraChinelo.getImage(), senhoraChinelo.getX(), senhoraChinelo.getY(), this);
        }

        if (senhoraChinelo.isDying()) {
            senhoraChinelo.die();
            inGame = false;
        }
    }

    private void drawChinelo(Graphics g) {
        if (chinelo.isVisible()) {
            g.drawImage(chinelo.getImage(), chinelo.getX(), chinelo.getY(), this);
        }
    }

    private void drawMamonas(Graphics g) {
        for (Moleque moleque : moleques) {
            Moleque.Mamona mamona = moleque.getMamona();

            if (!mamona.isDestroyed()) {
                g.drawImage(mamona.getImage(), mamona.getX(), mamona.getY(), this);
            }
        }
    }
}
