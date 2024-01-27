package zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class InvasaoEspacial extends JFrame {

    public InvasaoEspacial() {
        initUI();
    }

    private void initUI() {
        add(new Tabuleiro());

        setTitle("Invasão Espacial");
        setSize(Commons.LARGURA_TABULEIRO, Commons.ALTURA_TABULEIRO);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var invasaoEspacial = new InvasaoEspacial();
            invasaoEspacial.setVisible(true);
        });
    }
}
