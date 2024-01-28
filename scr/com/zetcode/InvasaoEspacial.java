package com.zetcode;

import javax.swing.*;
import java.awt.*;

public class InvasaoEspacial extends JFrame {

    private Tabuleiro tabuleiro;
    private LogicaJogo logicaJogo;

    public InvasaoEspacial() {
        initUI();
        iniciarJogo();
    }

    private void initUI() {
        tabuleiro = new Tabuleiro();
        add(tabuleiro);

        setTitle("Invasão Espacial");
        setSize(Commons.LARGURA_TABULEIRO, Commons.ALTURA_TABULEIRO);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void iniciarJogo() {
        logicaJogo = new LogicaJogo(tabuleiro);
        logicaJogo.iniciarJogo();

        Thread gameThread = new Thread(logicaJogo);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            InvasaoEspacial invasaoEspacial = new InvasaoEspacial();
            invasaoEspacial.setVisible(true);
        });
    }
}
