//package e imports
package backend.DAO.ajudaDAO;

//classe
public class Ajuda {

    //atributos
    private final int id;
    private final String nome;
    private final String descricao;

    //construtor
    public Ajuda(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
    
    
}
