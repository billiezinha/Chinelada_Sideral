package zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Mae;
import com.zetcode.sprite.Tiro;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Tabuleiro extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Mae mae;
    private Tiro tiro;

    private int direcao = -1;
    private int mortes = 0;

    private boolean emJogo = true;
    private String imgExplosao = "src/images/explosao.png";
    private String mensagem = "Fim de Jogo";

    private Timer temporizador;

    public Tabuleiro() {
        initTabuleiro();
        inicializarJogo();
    }

    private void initTabuleiro() {
        addKeyListener(new AdaptadorTeclado());
        setFocusable(true);
        d = new Dimension(Commons.LARGURA_TABULEIRO, Commons.ALTURA_TABULEIRO);
        setBackground(Color.black);

        temporizador = new Timer(Commons.DELAY, new CicloJogo());
        temporizador.start();

        inicializarJogo();
    }

    private void inicializarJogo() {
        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var alien = new Alien(Commons.ALIEN_X_INICIAL + 18 * j,
                        Commons.ALIEN_Y_INICIAL + 18 * i);
                aliens.add(alien);
            }
        }

        mae = new Mae();
        tiro = new Tiro();
    }

    private void desenharAliens(Graphics g) {
        for (Alien alien : aliens) {
            if (alien.estaVisivel()) {
                g.drawImage(alien.getImagem(), alien.getX(), alien.getY(), this);
            }

            if (alien.estaMorrendo()) {
                alien.morrer();
            }
        }
    }

    private void desenharMae(Graphics g) {
        if (mae.estaVisivel()) {
            g.drawImage(mae.getImagem(), mae.getX(), mae.getY(), this);
        }

        if (mae.estaMorrendo()) {
            mae.morrer();
            emJogo = false;
        }
    }

    private void desenharTiro(Graphics g) {
        if (tiro.estaVisivel()) {
            g.drawImage(tiro.getImagem(), tiro.getX(), tiro.getY(), this);
        }
    }

    private void desenharBombas(Graphics g) {
        for (Alien alien : aliens) {
            Alien.Bomba bomba = alien.getBomba();

            if (!bomba.estaDestruida()) {
                g.drawImage(bomba.getImagem(), bomba.getX(), bomba.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        fazerDesenho(g);
    }

    private void fazerDesenho(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (emJogo) {
            g.drawLine(0, Commons.SOLO,
                    Commons.LARGURA_TABULEIRO, Commons.SOLO);

            desenharAliens(g);
            desenharMae(g);
            desenharTiro(g);
            desenharBombas(g);

        } else {
            if (temporizador.isRunning()) {
                temporizador.stop();
            }

            fimDeJogo(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void fimDeJogo(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.LARGURA_TABULEIRO, Commons.ALTURA_TABULEIRO);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.LARGURA_TABULEIRO / 2 - 30, Commons.LARGURA_TABULEIRO - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.LARGURA_TABULEIRO / 2 - 30, Commons.LARGURA_TABULEIRO - 100, 50);

        var pequena = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(pequena);

        g.setColor(Color.white);
        g.setFont(pequena);
        g.drawString(mensagem, (Commons.LARGURA_TABULEIRO - fontMetrics.stringWidth(mensagem)) / 2,
                Commons.LARGURA_TABULEIRO / 2);
    }

    private void atualizar() {
        if (mortes == Commons.NUMERO_DE_ALIENS_PARA_DESTRUIR) {
            emJogo = false;
            temporizador.stop();
            mensagem = "Jogo ganho!";
        }

        // mae
        mae.agir();

        // tiro
        if (tiro.estaVisivel()) {
            int tiroX = tiro.getX();
            int tiroY = tiro.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.estaVisivel() && tiro.estaVisivel()) {
                    if (tiroX >= (alienX)
                            && tiroX <= (alienX + Commons.LARGURA_ALIEN)
                            && tiroY >= (alienY)
                            && tiroY <= (alienY + Commons.ALTURA_ALIEN)) {

                        var ii = new ImageIcon(imgExplosao);
                        alien.setImagem(ii.getImage());
                        alien.setMorrendo(true);
                        mortes++;
                        tiro.morrer();
                    }
                }
            }

            int y = tiro.getY();
            y -= 4;

            if (y < 0) {
                tiro.morrer();
            } else {
                tiro.setY(y);
            }
        }

        // aliens
        for (Alien alien : aliens) {
            int x = alien.getX();

            if (x >= Commons.LARGURA_TABULEIRO - Commons.BORDA_DIREITA && direcao != -1) {
                direcao = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {
                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.MOVIMENTO_ABAIXO);
                }
            }

            if (x <= Commons.BORDA_ESQUERDA && direcao != 1) {
                direcao = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {
                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.MOVIMENTO_ABAIXO);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = it.next();

            if (alien.estaVisivel()) {
                int y = alien.getY();

                if (y > Commons.SOLO - Commons.ALTURA_ALIEN) {
                    emJogo = false;
                    mensagem = "Invasão!";
                }

                alien.agir(direcao);
            }
        }

        // bombas
        var gerador = new Random();

        for (Alien alien : aliens) {
            int shot = gerador.nextInt(15);
            Alien.Bomba bomba = alien.getBomba();

            if (shot == Commons.CHANCE && alien.estaVisivel() && bomba.estaDestruida()) {
                bomba.setDestruida(false);
                bomba.setX(alien.getX());
                bomba.setY(alien.getY());
            }

            int bombX = bomba.getX();
            int bombY = bomba.getY();
            int playerX = mae.getX();
            int playerY = mae.getY();

            if (mae.estaVisivel() && !bomba.estaDestruida()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.LARGURA_MAE)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.ALTURA_MAE)) {

                    var ii = new ImageIcon(imgExplosao);
                    mae.setImagem(ii.getImage());
                    mae.setMorrendo(true);
                    bomba.setDestruida(true);
                }
            }

            if (!bomba.estaDestruida()) {
                bomba.setY(bomba.getY() + 1);

                if (bomba.getY() >= Commons.SOLO - Commons.ALTURA_BOMBA) {
                    bomba.setDestruida(true);
                }
            }
        }
    }

    private void fazerCicloJogo() {
        atualizar();
        repaint();
    }

    private class CicloJogo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fazerCicloJogo();
        }
    }

    private class AdaptadorTeclado extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            mae.teclaLiberada(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            mae.teclaPressionada(e);

            int x = mae.getX();
            int y = mae.getY();

            int tecla = e.getKeyCode();

            if (tecla == KeyEvent.VK_SPACE) {
                if (emJogo) {
                    if (!tiro.estaVisivel()) {
                        tiro = new Tiro(x, y);
                    }
                }
            }
        }
    }
}
