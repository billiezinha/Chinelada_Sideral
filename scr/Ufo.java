public class Ufo extends ObjetoBase {

    private static final int[][] matriz = {
        {0, 0, 1, 0, 0},
        {0, 0, 1, 0, 0},
        {1, 1, 1, 1, 1},
        {0, 1, 1, 1, 0},
        {0, 0, 0, 0, 0},
    };

    public Ufo(double x, double y) {
        super(x, y, 3);
    }

    @Override
    public void desenhar(Tela tela) {
        tela.desenharMatriz(x - raio + 1, y - raio + 1, matriz, 'U');
    }

    @Override
    public void mover() {
        double dx = Math.random() * 2 - 1;
        double dy = Math.random() * 2 - 1;

        x += dx;
        y += dy;

        verificarLimites(raio, Iniciar.jogo.getLargura() - raio + 1, raio - 1, Iniciar.jogo.getAltura() / 2 - 1);

        int random10 = (int) (Math.random() * 10);
        if (random10 == 0) {
            atirar();
        }
    }

    public void atirar() {
        Iniciar.jogo.getBombas().add(new Bomba(x, y + 3));
    }
}
