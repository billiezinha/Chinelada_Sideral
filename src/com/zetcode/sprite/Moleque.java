package com.zetcode.sprite;

import javax.swing.ImageIcon;

public class Moleque extends Sprite {

    private Mamona mamona;

    public Moleque(int x, int y) {

        initMoleque(x, y);
    }

    private void initMoleque(int x, int y) {

        this.x = x;
        this.y = y;

        mamona = new Mamona(x, y);

        var molequeImg = "src/images/moleque.png";
        var ii = new ImageIcon(molequeImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Mamona getMamona() {

        return mamona;
    }

    public class Mamona extends Sprite {

        private boolean destroyed;

        public Mamona(int x, int y) {

            initMamona(x, y);
        }

        private void initMamona(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var mamonaImg = "src/images/mamona.png";
            var ii = new ImageIcon(mamonaImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
