
public class Tela {
    private final int largura;
    private final int altura;
    private char[][] matriz;

    public Tela(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.matriz = new char[altura + 2][largura + 2];
    }

    public void definirPonto(double x, double y, char c) {
        int xArredondado = (int) Math.round(x);
        int yArredondado = (int) Math.round(y);
        if (estaNosLimites(xArredondado, yArredondado)) {
            matriz[yArredondado][xArredondado] = c;
        }
    }

    public void desenharMatriz(double x, double y, int[][] matriz, char c) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[i][j] != 0) {
                    definirPonto(x + j, y + i, c);
                }
            }
        }
    }

    public void limpar() {
        this.matriz = new char[altura + 2][largura + 2];
    }

    public void imprimir() {
        System.out.println();

        for (int i = 0; i < altura + 2; i++) {
            for (int j = 0; j < largura + 2; j++) {
                System.out.print(" ");
                System.out.print(matriz[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private boolean estaNosLimites(int xArredondado, int yArredondado) {
        return xArredondado >= 0 && xArredondado < matriz[0].length && yArredondado >= 0 && yArredondado < matriz.length;
    }

//  public int getLargura() {
//      return largura;
//  }
//
//  public int getAltura() {
//      return altura;
//  }
//
//  public char[][] getMatriz() {
//      return matriz;
//  }
}
