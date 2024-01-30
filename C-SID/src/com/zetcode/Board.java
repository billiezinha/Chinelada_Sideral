package com.zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

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
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;
    private Thread animator;

    private Semaphore alienSemaphore;

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
        aliens = new ArrayList<>();
        alienSemaphore = new Semaphore(Commons.NUMBER_OF_ALIENS_TO_DESTROY);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var alien = new Alien(Commons.ALIEN_INIT_X + 18 * j, Commons.ALIEN_INIT_Y + 18 * i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();

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
            drawAliens(bufferGraphics);
            drawPlayer(bufferGraphics);
            drawShot(bufferGraphics);
            drawBombing(bufferGraphics);
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
    player.act();

    // shot
    if (shot.isVisible()) {
        int shotX = shot.getX();
        int shotY = shot.getY();

        for (Alien alien : aliens) {
            int alienX = alien.getX();
            int alienY = alien.getY();

            if (alien.isVisible() && shot.isVisible()) {
                if (shotX >= (alienX) && shotX <= (alienX + Commons.ALIEN_WIDTH) && shotY >= (alienY)
                        && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    alien.setImage(ii.getImage());
                    alien.setDying(true);
                    deaths++;
                    shot.die();

                    // acquire the semaphore before launching the alien's bomb
                    try {
                        alienSemaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // launch the alien's bomb
                    Alien.Bomb bomb = alien.getBomb();
                    bomb.setDestroyed(false);
                    bomb.setX(alien.getX());
                    bomb.setY(alien.getY());
                }
            }
        }

        int y = shot.getY();
        y -= 6;

        if (y < 0) {
            shot.die();
        } else {
            shot.setY(y);
        }
    }

    // aliens
    for (Alien alien : aliens) {
        int x = alien.getX();

        if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
            direction = -1;

            Iterator<Alien> i1 = aliens.iterator();
            while (i1.hasNext()) {
                Alien a2 = i1.next();
                a2.setY(a2.getY() + Commons.GO_DOWN);
            }
        }

        if (x <= Commons.BORDER_LEFT && direction != 1) {
            direction = 1;

            Iterator<Alien> i2 = aliens.iterator();
            while (i2.hasNext()) {
                Alien a = i2.next();
                a.setY(a.getY() + Commons.GO_DOWN);
            }
        }
    }

    Iterator<Alien> it = aliens.iterator();

    while (it.hasNext()) {
        Alien alien = it.next();

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

    for (Alien alien : aliens) {
        int shot = generator.nextInt(15);
        Alien.Bomb bomb = alien.getBomb();

        if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {
            bomb.setDestroyed(false);
            bomb.setX(alien.getX());
            bomb.setY(alien.getY());
        }

        int bombX = bomb.getX();
        int bombY = bomb.getY();
        int playerX = player.getX();
        int playerY = player.getY();

        if (player.isVisible() && !bomb.isDestroyed()) {
            if (bombX >= (playerX) && bombX <= (playerX + Commons.PLAYER_WIDTH) && bombY >= (playerY)
                    && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                var ii = new ImageIcon(explImg);
                player.setImage(ii.getImage());
                player.setDying(true);
                bomb.setDestroyed(true);

                // release the semaphore after the bomb has been launched
                alienSemaphore.release();
            }
        }

        if (!bomb.isDestroyed()) {
            bomb.setY(bomb.getY() + 1);

            if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                bomb.setDestroyed(true);
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
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }

    private void drawAliens(Graphics g) {
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
}