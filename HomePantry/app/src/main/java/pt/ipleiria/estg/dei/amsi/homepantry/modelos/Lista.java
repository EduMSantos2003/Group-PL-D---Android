package pt.ipleiria.estg.dei.amsi.homepantry.modelos;

public class Lista {
    private int id;
    private int casa_id;
    private String nome;

    public Lista(int casa_id, String nome) {
        this.casa_id = casa_id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public int getCasa_id() { return casa_id; }
    public String getNome() { return nome; }

    public void setId(int id) { this.id = id; }
    public void setCasa_id(int casa_id) { this.casa_id = casa_id; }
    public void setNome(String nome) { this.nome = nome; }
}
