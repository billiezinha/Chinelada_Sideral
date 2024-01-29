package com.zetcode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import com.zetcode.sprite.Alien;

public class AlienController {

    private List<Alien> aliens;
    private int direction = -1;
    private Semaphore semaphore;

    public AlienController(List<Alien> aliens, Semaphore semaphore) {
        this.aliens = aliens;
        this.semaphore = semaphore;
        initializeTimer();
    }

    private void initializeTimer() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveAliens();
            }
        }, 0, 500); // Adjust the delay (500 milliseconds in this case)
    }

    private void moveAliens() {
        try {
            semaphore.acquire(); // Acquire the semaphore before moving the aliens
            for (Alien alien : aliens) {
                int x = alien.getX();
                if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                    direction = -1;
                    moveDown();
                }
                if (x <= Commons.BORDER_LEFT && direction != 1) {
                    direction = 1;
                    moveDown();
                }
            }

            for (Alien alien : aliens) {
                alien.act(direction);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // Release the semaphore after moving the aliens
        }
    }

    private void moveDown() {
        for (Alien alien : aliens) {
            alien.setY(alien.getY() + Commons.GO_DOWN);
        }
    }
}
