package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Tabuleiro extends JPanel implements ActionListener, KeyListener {

    private final Timer timer;
    private final LogicaJogo logicaJogo;

    public Tabuleiro() {
        initTabuleiro();
        logicaJogo = new LogicaJogo(this);
        addKeyListener(this);

        setFocusable(true);
        setBackground(Color.BLACK);

        timer = new Timer(Commons.DELAY, this);
        timer.start();
    }

    private void initTabuleiro() {
        setPreferredSize(new Dimension(Commons.LARGURA_TABULEIRO, Commons.ALTURA_TABULEIRO));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (logicaJogo != null) {
            logicaJogo.desenhar(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (logicaJogo != null) {
            logicaJogo.run();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (logicaJogo != null) {
            logicaJogo.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (logicaJogo != null) {
            logicaJogo.keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // não é necessário implementar, mas deve estar presente devido à interface KeyListener
    }
}
