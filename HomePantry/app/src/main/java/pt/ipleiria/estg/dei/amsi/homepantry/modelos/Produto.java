package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "produtos",
        foreignKeys = @ForeignKey(
                entity = Categoria.class,
                parentColumns = "id",
                childColumns = "categoria_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "categoria_id")}
)
public class Produto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoria_id")
    private int categoriaId;

    private String nome;

    private String descricao;

    // Na tua BD é INT
    private int unidade;

    // decimal(10,2) -> no Room usa-se normalmente double ou BigDecimal (double é o mais comum em projetos académicos)
    private double preco;

    // DATE -> vamos guardar como String "yyyy-MM-dd" para evitar TypeConverters
    private String validade;

    private String imagem;

    // Construtor (sem id porque é auto-increment)
    public Produto(int categoriaId, String nome, String descricao, int unidade, double preco, String validade, String imagem) {
        this.categoriaId = categoriaId;
        this.nome = nome;
        this.descricao = descricao;
        this.unidade = unidade;
        this.preco = preco;
        this.validade = validade;
        this.imagem = imagem;
    }

    // Getters/Setters
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
