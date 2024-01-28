package com.zetcode.Sprite;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Sprite {

    private boolean visivel;
    private Image imagem;
    private boolean morrendo;

    int x;
    int y;
    int dx;

    public Sprite() {
        visivel = true;
    }

    public void morrer() {
        visivel = false;
    }

    public boolean estaVisivel() {
        return visivel;
    }

    protected void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }

    public Image getImagem() {
        return imagem;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setMorrendo(boolean morrendo) {
        this.morrendo = morrendo;
    }

    public boolean estaMorrendo() {
        return this.morrendo;
    }

    public boolean isVisible() {
        return visivel;
    }

    public Sprite getBomba() {
        return new Bomba(x, y);
    }

    public void agir(int direcao) {
        this.x += direcao;
    }

    public static class Bomba extends Sprite {
        
        public Bomba(int x, int y) {
            initBomba(x, y);
        }

        private void initBomba(int x, int y) {
            setMorrendo(true);

            this.x = x;
            this.y = y;

            var bombaImg = "src/images/bomba.png";
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
