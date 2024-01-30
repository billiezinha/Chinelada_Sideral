package com.zetcode.sprite;

import javax.swing.ImageIcon;

public class Chinelo extends Sprite {

    public Chinelo() {
    }

    public Chinelo(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {

        var chineloImg = "src/images/chinelo.png";
        var ii = new ImageIcon(chineloImg);
        setImage(ii.getImage());

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}
