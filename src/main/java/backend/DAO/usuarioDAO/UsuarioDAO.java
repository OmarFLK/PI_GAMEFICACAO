package backend.DAO.usuarioDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.Seguranca.SenhaService;
import backend.servidor.Conexao;

public class UsuarioDAO {

    public Usuario efetuarLogin(String email, String senhaDigitada) {
        String sql = "SELECT idUsuario, nomeUsuario, emailUsuario, tipo, senha FROM usuario WHERE emailUsuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaDoBanco = rs.getString("senha");
                try {
                    if (SenhaService.verificarSenha(senhaDigitada, senhaDoBanco)) {
                        return new Usuario(
                                rs.getInt("idUsuario"),
                                rs.getString("nomeUsuario"),
                                rs.getString("emailUsuario"),
                                rs.getString("tipo"));
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Senha no banco nao esta em formato BCrypt: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao efetuar login: " + e.getMessage());
        }
        return null;
    }

    public boolean cadastrarUsuario(String nome, String email, String senha, String tipo) {
        String sql = "INSERT INTO usuario(nomeUsuario, emailUsuario, senha, tipo) VALUES(?,?,?,?)";
        String senhaCriptografada = SenhaService.hashSenha(senha);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senhaCriptografada);
            stmt.setString(4, tipo);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
        }
        return false;
    }

    /**
     * Atualiza os dados de um usuario existente.
     * Se a senhaNova for null ou vazia, a senha atual e mantida no banco.
     */
    public void atualizarUsuario(int id, String nome, String email, String senhaNova, String tipo) {
        boolean alterarSenha = (senhaNova != null && !senhaNova.trim().isEmpty());
        String sql = alterarSenha
            ? "UPDATE usuario SET nomeUsuario = ?, emailUsuario = ?, senha = ?, tipo = ? WHERE idUsuario = ?"
            : "UPDATE usuario SET nomeUsuario = ?, emailUsuario = ?, tipo = ? WHERE idUsuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);

            if (alterarSenha) {
                stmt.setString(3, SenhaService.hashSenha(senhaNova));
                stmt.setString(4, tipo);
                stmt.setInt(5, id);
            } else {
                stmt.setString(3, tipo);
                stmt.setInt(4, id);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    public void deletarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar: " + e.getMessage());
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT idUsuario, nomeUsuario, emailUsuario, tipo FROM usuario ORDER BY nomeUsuario ASC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nomeUsuario"),
                        rs.getString("emailUsuario"),
                        rs.getString("tipo")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public void atualizarTipo(int id, String novoTipo) {
        String sql = "UPDATE usuario SET tipo = ? WHERE idUsuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoTipo);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tipo de usuario: " + e.getMessage());
        }
    }
}
