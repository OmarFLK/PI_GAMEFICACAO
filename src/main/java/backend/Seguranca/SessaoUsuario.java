package backend.Seguranca;

import backend.DAO.usuarioDAO.Usuario;

public class SessaoUsuario {
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado; // Guarda o objeto completo

    private SessaoUsuario() {}

    public static SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public Usuario getUsuario() {
        return usuarioLogado;
    }
    
    // Método utilitário para pegar o ID rápido se precisar
    public int getIdUsuario() {
        return (usuarioLogado != null) ? usuarioLogado.getId() : -1;
    }

    public void encerrarSessao() {
        usuarioLogado = null;
    }
}