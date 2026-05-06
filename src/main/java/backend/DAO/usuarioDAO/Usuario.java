//imports e package
package backend.DAO.usuarioDAO;

//classe
public class Usuario {

    //atributos
    private final int id;
    private final String nome;
    private final String email;
    private final String tipo;

    // Construtor
    public Usuario(int id, String nome, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    // Getters para usar depois no sistema
    public String getNome() { 
        return nome; 
    }
    
    public String getTipo() { 
        return tipo; 
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}