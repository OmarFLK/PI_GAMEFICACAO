package backend.util;

import backend.DAO.usuarioDAO.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado;

    public static void setUsuario(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioLogado;
    }

    public static boolean estaLogado() {
        return usuarioLogado != null;
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }
}