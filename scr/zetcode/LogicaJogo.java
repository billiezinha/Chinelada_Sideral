package com.zetcode;

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
        this.tiro = new Tiro();
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
        // Inicialize aqui qualquer configuração necessária para o início do jogo
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

            // Aguarde um curto período de tempo para evitar sobrecarga
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateJogo() {
        if (mortes == Commons.NUMERO_DE_FILHOS_PARA_DESTRUIR) {
            // Se o número necessário de filhos for destruído, encerre o jogo
            // Você pode adicionar lógica adicional aqui
            System.out.println("Jogo ganho!");
        }

        // Atualize a lógica do jogo aqui

        // Atualize a mae
        mae.agir();

        // Atualize o tiro
        if (tiro.isVisible()) {
            verificarColisaoTiroFilho();
            moverTiro();
        }

        // Atualize os filhos
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

                    filho.setMorrer(true);
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
            Filho.Bomba bomba = filho.getBomba();

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

                    mae.setMorrer(true);
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
}
