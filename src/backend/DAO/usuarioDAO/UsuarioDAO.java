//imports e package
package backend.DAO.usuarioDAO;

import java.sql.*;

import backend.Seguranca.SenhaService;
import backend.servidor.Conexao;

//classe
public class UsuarioDAO {

    //metodo para efetuar login 
    public Usuario efetuarLogin(String email, String senhaDigitada) {
        // Query usando os nomes exatos das suas colunas
        String sql = "SELECT idUsuario, nomeUsuario, emailUsuario, tipo, senha FROM usuario WHERE emailUsuario = ?";
        
        try (Connection conn = Conexao.conectar();
            // o preparedstatement limpa os dados que o usuario digita eliminando o hackers
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String senhaDoBanco = rs.getString("senha");

                if (SenhaService.verificarSenha(senhaDigitada, senhaDoBanco)) {
                // verifica se a senha digitada é igual o senha criptografada (hash)
                return new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nomeUsuario"),
                    rs.getString("emailUsuario"),
                    rs.getString("tipo")
                );
            }
            }
            

        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
        return null; // Retorna null se o login falhar
    }

    //metodo para fazer cadastro
    public void cadastrarUsuario(String nome,String email, String senha, String tipo){
        String sql = "INSERT INTO usuario(nomeUsuario, emailUsuario,senha, tipo) VALUES(?,?,?,?)";
        String senhaCriptografada = SenhaService.hashSenha(senha);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senhaCriptografada);
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