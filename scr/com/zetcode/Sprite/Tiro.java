package com.zetcode.Sprite;

import javax.swing.ImageIcon;

public class Tiro extends Sprite {

    public Tiro(int x, int y) {
        initTiro(x, y);
    }

    private void initTiro(int x, int y) {
        setMorrendo(true);

        this.x = x;
        this.y = y;

        var tiroImg = "src/images/chinela.png";
        var ii = new ImageIcon(tiroImg);
        setImagem(ii.getImage());
    }

    public boolean isMorto() {
        return !estaVisivel();
    }

    public void setMorto(boolean morto) {
        setVisivel(!morto);
    }
}
