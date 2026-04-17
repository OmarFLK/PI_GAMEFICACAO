package backend.UsuarioDAO;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String tipo;

    // Construtor
    public Usuario(int id, String nome, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    // Getters para usar depois no sistema
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
}