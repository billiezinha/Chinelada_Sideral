package zetcode.Sprite;

import javax.swing.ImageIcon;

public class Filho extends Sprite {

    private Bomba bomba;

    public Filho(int x, int y) {

        initFilho(x, y);
    }

    private void initFilho(int x, int y) {

        this.x = x;
        this.y = y;

        bomba = new Bomba(x, y);

        var filhoImg = "src/images/filho.png";
        var ii = new ImageIcon(filhoImg);

        setImage(ii.getImage());
    }

    public void agir(int direcao) {

        this.x += direcao;
    }

    public Bomba getBomba() {

        return bomba;
    }

    public class Bomba extends Sprite {

        private boolean destruida;

        public Bomba(int x, int y) {

            initBomba(x, y);
        }

        private void initBomba(int x, int y) {

            setDestruida(true);

            this.x = x;
            this.y = y;

            var bombaImg = "src/images/bomba.png";
            var ii = new ImageIcon(bombaImg);
            setImage(ii.getImage());
        }

        public void setDestruida(boolean destruida) {

            this.destruida = destruida;
        }

        public boolean estaDestruida() {

            return destruida;
        }
    }
}
