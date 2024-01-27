
public class Bomba extends ObjetoBase {

    public Bomba(double x, double y) {
        super(x, y, 1);
    }

    @Override
    public void desenhar(Tela tela) {
        tela.definirPonto(x, y, 'B');
    }

    @Override
    public void mover() {
        y++;
    }
}
