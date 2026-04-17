package backend.UsuarioDAO;

import java.sql.*;

import backend.servidor.Conexao;

public class UsuarioDAO {

    public Usuario efetuarLogin(String email, String senha) {
        // Query usando os nomes exatos das suas colunas
        String sql = "SELECT idUsuario, nomeUsuario, emailUsuario, tipo FROM usuario WHERE emailUsuario = ? AND senha = ?";
        
        try (Connection conn = Conexao.conectar();
            // o preparedstatement limpa os dados que o usuario digita eliminando o hackers
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Se encontrou, retorna o objeto Usuario preenchido
                return new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nomeUsuario"),
                    rs.getString("emailUsuario"),
                    rs.getString("tipo")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
        return null; // Retorna null se o login falhar
    }

    public void cadastrarUsuario(String nome,String email, String senha, String tipo){
        String sql = "INSERT INTO usuario(nomeUsuario, emailUsuario,senha, tipo) VALUES(?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, tipo);

            stmt.executeUpdate();
            
        }catch(SQLException e){
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
        public void deletarUsuario(int id){
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);

            stmt.executeUpdate();
            
        }catch(SQLException e){
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }

}