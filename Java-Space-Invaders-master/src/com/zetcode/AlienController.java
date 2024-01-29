package com.zetcode;

import java.util.List;
import java.util.concurrent.Semaphore;

import com.zetcode.sprite.Alien;

public class AlienController implements Runnable {
    private List<Alien> aliens;
    private int direction = -1;
    private Semaphore semaphore;

    public AlienController(List<Alien> aliens, Semaphore semaphore) {
        this.aliens = aliens;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphore.acquire(); // Aguarda a permissão do semáforo
                moveAliens();
                Thread.sleep(500); // Aguarda um tempo antes de mover novamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); // Libera o semáforo
            }
        }
    }

    private void moveAliens() {
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
    }

    private void moveDown() {
        for (Alien alien : aliens) {
            alien.setY(alien.getY() + Commons.GO_DOWN);
        }
    }
}
