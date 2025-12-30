package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

public class Produto {

    private int id;              // vem da API (GET)
    private int categoriaId;     // categoria_id no JSON
    private String nome;
    private String descricao;
    private int unidade;
    private double preco;
    private String validade;     // yyyy-MM-dd
    private String imagem;       // opcional (URL ou nome do ficheiro)

    // 🔹 CONSTRUTOR PARA POST (SEM ID)
    public Produto(String nome,
                   String descricao,
                   int unidade,
                   double preco,
                   String validade,
                   int categoriaId) {

        this.nome = nome;
        this.descricao = descricao;
        this.unidade = unidade;
        this.preco = preco;
        this.validade = validade;
        this.categoriaId = categoriaId;
    }

    // 🔹 CONSTRUTOR VAZIO (OBRIGATÓRIO para JSON parsing)
    public Produto() {}

    // 🔹 GETTERS & SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getUnidade() {
        return unidade;
    }

    public void setUnidade(int unidade) {
        this.unidade = unidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
