package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class ChineladaSideral extends JFrame  {

    public ChineladaSideral() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Chinelada Sideral");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new ChineladaSideral();
            ex.setVisible(true);
        });
    }
}
