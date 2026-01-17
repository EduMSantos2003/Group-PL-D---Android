package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

public class StockUpdate {
    private int quantidade;

    public StockUpdate(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
