

public class NaveEspacial extends ObjetoBase {

    private static final int[][] matriz = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0},
    };
    private double dx;

    public NaveEspacial(double x, double y) {
        super(x, y, 3);
    }

    public void moverEsquerda() {
        dx = -1;
    }

    public void moverDireita() {
        dx = 1;
    }

    @Override
    public void desenhar(Tela tela) {
        tela.desenharMatriz(x - raio + 1, y - raio + 1, matriz, 'M');
    }

    @Override
    public void mover() {
        x = x + dx;
        verificarLimites(raio, Iniciar.game.getLargura() - raio + 1, 1, Iniciar.game.getAltura() + 1);
    }

    public void atirar() {
        Iniciar.game.getFoguetes().add(new Foguete(x - 2, y));
        Iniciar.game.getFoguetes().add(new Foguete(x + 2, y));
    }
}
