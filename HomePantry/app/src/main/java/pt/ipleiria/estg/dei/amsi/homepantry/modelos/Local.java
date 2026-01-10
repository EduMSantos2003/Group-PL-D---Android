package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

public class Local {

    private int id;
    private String nome;
    private String descricao;
    private String fotoPath; // caminho local da foto (opcional)

    // 🔹 Construtor vazio (OBRIGATÓRIO para JSON)
    public Local() {
    }

    // 🔹 Construtor para criar Local
    public Local(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}

