package com.zetcode;

import com.zetcode.Sprite.Filho;
import com.zetcode.Sprite.Mae;
import com.zetcode.Sprite.Tiro;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class LogicaJogo implements Runnable {

    private final Tabuleiro tabuleiro;
    private final Semaphore mutex;
    private List<Filho> filhos;
    private Mae mae;
    private Tiro tiro;
    private int direcao = -1;
    private int mortes = 0;

    public LogicaJogo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.mutex = new Semaphore(1);
        this.filhos = new ArrayList<>();
        this.mae = new Mae();
        this.tiro = new Tiro(-1, -1);  
        initFilhos();
    }

    private void initFilhos() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var filho = new Filho(Commons.FILHO_INICIAL_X + 18 * j, Commons.FILHO_INICIAL_Y + 18 * i);
                filhos.add(filho);
            }
        }
    }

    public void iniciarJogo() {
        filhos = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                var filho = new Filho(Commons.FILHO_INICIAL_X + 18 * j,
                        Commons.FILHO_INICIAL_Y + 18 * i);
                filhos.add(filho);
            }
        }

        mae = new Mae();
        tiro = new Tiro(-1,-1);
    }

    @Override
    public void run() {
        while (true) {
            try {
                mutex.acquire();

                updateJogo();
                tabuleiro.repaint();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateJogo() {
        if (mortes == Commons.NUMERO_DE_FILHOS_PARA_DESTRUIR) {
            System.out.println("Jogo ganho!");
            System.exit(0);
        }

        // Atualize a lógica do jogo aqui

        // Atualize a mae
        mae.agir();
        if (tiro.isVisible()) {
            verificarColisaoTiroFilho();
            moverTiro();
        }

        updateFilhos();

        // Atualize as bombas dos filhos
        updateBombas();
    }

    private void moverTiro() {
        int y = tiro.getY();
        y -= 4;

        if (y < 0) {
            tiro.morrer();
        } else {
            tiro.setY(y);
        }
    }

    private void verificarColisaoTiroFilho() {
        int tiroX = tiro.getX();
        int tiroY = tiro.getY();

        Iterator<Filho> filhoIterator = filhos.iterator();

        while (filhoIterator.hasNext()) {
            Filho filho = filhoIterator.next();

            int filhoX = filho.getX();
            int filhoY = filho.getY();

            if (filho.isVisible() && tiro.isVisible()) {
                if (tiroX >= filhoX && tiroX <= (filhoX + Commons.LARGURA_FILHO)
                        && tiroY >= filhoY && tiroY <= (filhoY + Commons.ALTURA_FILHO)) {

                    filho.setMorrendo(true);
                    mortes++;
                    tiro.morrer();
                }
            }
        }
    }

    private void updateFilhos() {
        for (Filho filho : filhos) {
            int x = filho.getX();

            if (x >= Commons.LARGURA_TABULEIRO - Commons.BORDA_DIREITA && direcao != -1) {
                direcao = -1;
                moveFilhosAbaixo();
            }

            if (x <= Commons.BORDA_ESQUERDA && direcao != 1) {
                direcao = 1;
                moveFilhosAbaixo();
            }

            filho.agir(direcao);
        }
    }

    private void moveFilhosAbaixo() {
        for (Filho filho : filhos) {
            filho.setY(filho.getY() + Commons.MOVIMENTO_ABAIXO);
        }
    }

    private void updateBombas() {
        Random gerador = new Random();

        for (Filho filho : filhos) {
            int tiro = gerador.nextInt(15);
            Filho.Bomba bomba = (Filho.Bomba) filho.getBomba();

            if (tiro == Commons.CHANCE && filho.isVisible() && bomba.isMorta()) {
                bomba.setMorta(false);
                bomba.setX(filho.getX());
                bomba.setY(filho.getY());
            }

            int bombaX = bomba.getX();
            int bombaY = bomba.getY();
            int maeX = mae.getX();
            int maeY = mae.getY();

            if (mae.isVisible() && !bomba.isMorta()) {
                if (bombaX >= maeX && bombaX <= (maeX + Commons.LARGURA_MAE)
                        && bombaY >= maeY && bombaY <= (maeY + Commons.ALTURA_MAE)) {

                    mae.setMorrendo(true);
                    bomba.setMorta(true);
                }
            }

            if (!bomba.isMorta()) {
                bomba.setY(bomba.getY() + 1);

                if (bomba.getY() >= Commons.SOLO - Commons.ALTURA_BOMBA) {
                    bomba.setMorta(true);
                }
            }
        }
    }

    public void desenhar(Graphics g) {
        if (mae.isVisible()) {
            g.drawImage(mae.getImagem(), mae.getX(), mae.getY(), tabuleiro);
        }

        for (Filho filho : filhos) {
            if (filho.isVisible()) {
                g.drawImage(filho.getImagem(), filho.getX(), filho.getY(), tabuleiro);
            }

            Filho.Bomba bomba = (Filho.Bomba) filho.getBomba();
            if (!bomba.isMorta()) {
                g.drawImage(bomba.getImagem(), bomba.getX(), bomba.getY(), tabuleiro);
            }
        }

        if (tiro.isVisible()) {
            g.drawImage(tiro.getImagem(), tiro.getX(), tiro.getY(), tabuleiro);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE && !tiro.isVisible()) {
            tiro = new Tiro(mae.getX(), mae.getY());
        } else if (key == KeyEvent.VK_LEFT) {
            direcao = -1;
        } else if (key == KeyEvent.VK_RIGHT) {
            direcao = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            direcao = 0;
        }
    }
}
