
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Espaco {

    private final int largura;
    private final int altura;
    private static int atraso;

    private NaveEspacial nave;
    private final List<Ufo> ufos = new ArrayList<>();
    private final List<Foguete> foguetes = new ArrayList<>();
    private final List<Bomba> bombas = new ArrayList<>();

    public Espaco(int largura, int altura, int atraso) {
        this.largura = largura;
        this.altura = altura;
        Espaco.atraso = atraso;
    }

    public void executar() {
        Tela tela = new Tela(largura, altura);
        ObservadorTeclado observadorTeclado = new ObservadorTeclado();
        observadorTeclado.start();

        while (nave.estaVivo()) {
            if (observadorTeclado.temEventosTeclado()) {
                KeyEvent evento = observadorTeclado.obterEventoDoTopo();
                System.out.print(evento.getKeyCode());
                if (evento.getKeyCode() == KeyEvent.VK_LEFT) {
                    nave.moverEsquerda();
                } else if (evento.getKeyCode() == KeyEvent.VK_RIGHT) {
                    nave.moverDireita();
                } else if (evento.getKeyCode() == KeyEvent.VK_SPACE) {
                    nave.atirar();
                }
            }
            moverTodosOsItens();
            verificarBombas();
            verificarFoguetes();
            removerMortos();
            criarUfo();
            tela.limpar();
            desenhar(tela);
            tela.imprimir();
            Espaco.sleep();
        }
        System.out.println("Fim de jogo!!!");
    }

    public static void sleep() {
        try {
            Thread.sleep(atraso);
        } catch (InterruptedException ignorado) {
        }
    }

    public void desenhar(Tela tela) {
        for (int i = 0; i < largura + 2; i++) {
            for (int j = 0; j < altura + 2; j++) {
                tela.definirPonto(i, j, '.');
            }
        }

        for (int i = 0; i < largura + 2; i++) {
            tela.definirPonto(i, 0, '-');
            tela.definirPonto(i, altura + 1, '-');
        }

        for (int i = 0; i < altura + 2; i++) {
            tela.definirPonto(0, i, '|');
            tela.definirPonto(largura + 1, i, '|');
        }

        for (ObjetoBase objeto : obterTodosOsItens()) {
            objeto.desenhar(tela);
        }
    }

    public void moverTodosOsItens() {
        for (ObjetoBase objeto : obterTodosOsItens()) {
            objeto.mover();
        }
    }

    public List<ObjetoBase> obterTodosOsItens() {
        ArrayList<ObjetoBase> lista = new ArrayList<>(ufos);
        lista.add(nave);
        lista.addAll(bombas);
        lista.addAll(foguetes);
        return lista;
    }

    public void criarUfo() {
        if (ufos.size() > 0) {
            return;
        }
        int random10 = (int) (Math.random() * 10);
        if (random10 == 0) {
            double x = Math.random() * largura;
            double y = Math.random() * altura / 2;
            ufos.add(new Ufo(x, y));
        }
    }

    public void verificarBombas() {
        for (Bomba bomba : bombas) {
            if (nave.estaIntersectando(bomba)) {
                nave.morrer();
                bomba.morrer();
            }
            if (bomba.getY() >= altura) {
                bomba.morrer();
            }
        }
    }

    public void verificarFoguetes() {
        for (Foguete foguete : foguetes) {
            for (Ufo ufo : ufos) {
                if (ufo.estaIntersectando(foguete)) {
                    ufo.morrer();
                    foguete.morrer();
                }
            }
            if (foguete.getY() <= 0) {
                foguete.morrer();
            }
        }
    }

    public void removerMortos() {
        for (ObjetoBase objeto : new ArrayList<>(bombas)) {
            if (!objeto.estaVivo()) {
                bombas.remove(objeto);
            }
        }

        for (ObjetoBase objeto : new ArrayList<>(foguetes)) {
            if (!objeto.estaVivo()) {
                foguetes.remove(objeto);
            }
        }

        for (ObjetoBase objeto : new ArrayList<>(ufos)) {
            if (!objeto.estaVivo()) {
                ufos.remove(objeto);
            }
        }
    }

    public void setNave(NaveEspacial nave) {
        this.nave = nave;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public List<Foguete> getFoguetes() {
        return foguetes;
    }

    public List<Bomba> getBombas() {
        return bombas;
    }
}
