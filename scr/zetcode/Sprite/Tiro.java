package zetcode.Sprite;

import javax.swing.ImageIcon;

public class Tiro extends Sprite {

    public Tiro() {
    }

    public Tiro(int x, int y) {

        initTiro(x, y);
    }

    private void initTiro(int x, int y) {

        var tiroImg = "src/images/tiro.png";
        var ii = new ImageIcon(tiroImg);
        setImage(ii.getImage());

        int ESPACO_H = 6;
        setX(x + ESPACO_H);

        int ESPACO_V = 1;
        setY(y - ESPACO_V);
    }
}
