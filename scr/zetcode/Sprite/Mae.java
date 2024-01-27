package zetcode.Sprite;

import com.zetcode.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

public class Mae extends Sprite {

    private int largura;

    public Mae() {

        initMae();
    }

    private void initMae() {

        var maeImg = "src/images/mae.png";
        var ii = new ImageIcon(maeImg);

        largura = ii.getImage().getWidth(null);
        setImage(ii.getImage());

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

        if (x >= Commons.BOARD_WIDTH - 2 * largura) {

            x = Commons.BOARD_WIDTH - 2 * largura;
        }
    }

    public void teclaPressionada(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_LEFT) {

            dx = -2;
        }

        if (tecla == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
    }

    public void teclaLiberada(KeyEvent e) {

        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (tecla == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}
