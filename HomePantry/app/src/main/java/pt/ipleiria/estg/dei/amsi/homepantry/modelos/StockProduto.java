package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

public class StockProduto {

    private int id;
    private int produto_id;
    private String nome;
    private int quantidade;
    private String validade;
    private String local;

    // GETTERS
    public int getId() { return id; }
    public int getProduto_id() { return produto_id; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public String getValidade() { return validade; }
    public String getLocal() { return local; }

    // SETTERS (OBRIGATÓRIOS PARA EDITAR STOCK)
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}

