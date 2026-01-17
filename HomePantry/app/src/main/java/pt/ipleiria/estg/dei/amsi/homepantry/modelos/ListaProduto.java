package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

import com.google.gson.annotations.SerializedName;

public class ListaProduto {

    @SerializedName("id")
    private int id;

    @SerializedName("lista_id")
    private int lista_id;

    @SerializedName("produto_id")
    private int produto_id;

    @SerializedName("produto_nome")
    private String produto_nome;

    @SerializedName("quantidade")
    private double quantidade;

    @SerializedName("precoUnitario")
    private double precoUnitario;

    @SerializedName("subTotal")
    private double subTotal;

    public ListaProduto() {
    }

    public ListaProduto(int produto_id, double quantidade) {
        this.produto_id = produto_id;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public int getLista_id() { return lista_id; }
    public int getProduto_id() { return produto_id; }
    public String getProduto_nome() { return produto_nome; }
    public double getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public double getSubTotal() { return subTotal; }

    public void setId(int id) { this.id = id; }
    public void setLista_id(int lista_id) { this.lista_id = lista_id; }
    public void setProduto_id(int produto_id) { this.produto_id = produto_id; }
    public void setProduto_nome(String produto_nome) { this.produto_nome = produto_nome; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }
}
