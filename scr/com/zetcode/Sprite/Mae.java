package com.zetcode.Sprite;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.zetcode.Commons;

public class Mae extends Sprite {

    private final String naveMae = "src/images/avo.png";
    private int largura;

    public Mae() {
        initMae();
    }

    private void initMae() {
        ImageIcon ii = new ImageIcon(naveMae);
        largura = ii.getImage().getWidth(null);
        setImagem(ii.getImage());
        int START_X = 270;
        setX(START_X);
        int START_Y = 280;
        setY(START_Y);
    }

    public void agir() {
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= Commons.LARGURA_TABULEIRO - 2 * largura) {
            x = Commons.LARGURA_TABULEIRO - 2 * largura;
        }
    }

    public void teclaLiberada(KeyEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'teclaLiberada'");
    }

    public void teclaPressionada(KeyEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'teclaPressionada'");
    }
}
