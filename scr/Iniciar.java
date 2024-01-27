
public class Iniciar {
    public static Espaco jogo;

    public static void main(String[] args) {
        jogo = new Espaco(15, 15, 1200);
        jogo.setNave(new NaveEspacial(8, 14));
        jogo.executar();
    }
}
