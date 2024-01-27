
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ObservadorTeclado extends Thread {

    private final Queue<KeyEvent> eventosTeclado = new ArrayBlockingQueue<>(100);

    @Override
    public void run() {
        JFrame frame;
        frame = new JFrame("Testador de Teclas");
        frame.setTitle("Demo de JFrame Transparente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setUndecorated(true);
        frame.setSize(400, 400);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new GridBagLayout());

        frame.setOpacity(0.0f);
        frame.setVisible(true);

        frame.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.exit(0);
            }
        });

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                eventosTeclado.add(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public boolean temEventosTeclado() {
        return !eventosTeclado.isEmpty();
    }

    public KeyEvent obterEventoDoTopo() {
        return eventosTeclado.poll();
    }
}
