
public class Foguete extends ObjetoBase {
    public Foguete(double x, double y) {
        super(x, y, 1);
    }

    @Override
    public void mover() {
        y--;
    }

    @Override
    public void desenhar(Tela tela) {
        tela.definirPonto(x, y, 'R');
    }
}
