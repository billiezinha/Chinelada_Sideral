package com.zetcode.Sprite;

import javax.swing.ImageIcon;

public class Filho extends Sprite {

    public Filho(int x, int y) {
        initFilho(x, y);
    }

    private void initFilho(int x, int y) {
        setImagemFilho(x, y);

        var bombaImg = "src/images/mamona.png";
        var ii = new ImageIcon(bombaImg);
        setImagem(ii.getImage());
    }

    @Override
    public void agir(int direcao) {
        this.x += direcao;
    }

    @Override
    public Sprite getBomba() {
        return new Bomba(x, y);
    }

    private void setImagemFilho(int x, int y) {
        var jogadorImg = "src/images/avo.png";
        var ii = new ImageIcon(jogadorImg);
        setImagem(ii.getImage());

        setX(x);
        setY(y);
    }

    public class Bomba extends Sprite {

        public Bomba(int x, int y) {
            initBomba(x, y);
        }

        private void initBomba(int x, int y) {
            setMorrendo(true);

            this.x = x;
            this.y = y;

            var bombaImg = "src/images/mamona.png";
            var ii = new ImageIcon(bombaImg);
            setImagem(ii.getImage());
        }

        public boolean isMorta() {
            return !estaVisivel();
        }

        public void setMorta(boolean morta) {
            setVisivel(!morta);
        }

        public boolean estaDestruida() {
            return isMorta();
        }

        public void setDestruida(boolean destruida) {
            setMorta(destruida);
        }
    }
}
