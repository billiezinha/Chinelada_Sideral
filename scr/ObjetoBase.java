
public abstract class ObjetoBase {
    protected double x;
    protected double y;
    protected double raio;
    private boolean estaVivo;

    public ObjetoBase(double x, double y, double raio) {
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.estaVivo = true;
    }

    public void desenhar(Tela tela) {

    }

    public void mover() {

    }

    public void morrer() {
        estaVivo = false;
    }

    public boolean estaVivo() {
        return estaVivo;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    public void verificarLimites(double minX, double maxX, double minY, double maxY) {
        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;
    }

    public boolean estaIntersectando(ObjetoBase o) {
        double dx = x - o.x;
        double dy = y - o.y;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        double distanciaMinima = Math.max(raio, o.raio);
        return distancia <= distanciaMinima;
    }
}
