//package e import
package backend.DAO.ajudaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import backend.servidor.Conexao;

//classe
public class AjudaDAO {

    //metodo para criar ajuda
    public void criarAjuda(String nome, String descricao){
        String sql = "INSERT INTO ajuda(nome, descricao) VALUES(?,?)";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setString(1,nome);
            stmt.setString(2, descricao);

            stmt.executeUpdate();

        }catch(SQLException e){
            System.err.println("Erro ao criar ajuda: " + e.getMessage());
        }
    }

    // metodo para pegar "ajuda" do banco de dados e colocar no molde ajuda
    public Ajuda getAjuda(int id){
        String sql = "SELECT idAjuda, nome, descricao FROM ajuda WHERE idAjuda = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                return new Ajuda(
                    rs.getInt("idAjuda"),
                    rs.getString("nome"),
                    rs.getString("descricao")
                );
            }
        }catch(SQLException e){
            System.err.println("Erro ao buscar ajuda: " + e.getMessage());
        }
        return null;
    }

    public void atualizarAjuda(int id, String nome, String descricao){
        String sql = "UPDATE ajuda SET nome  = ?, descricao = ? WHERE idAjuda = ?";
        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1, nome);
                stmt.setString(2, descricao);
                stmt.setInt(3, id);

                stmt.executeUpdate();
            }catch(SQLException e){
                System.err.println("Erro ao atualizar ajuda: " + e.getMessage());
            }
    }

        public void deletarAjuda(int id){
        String sql = "DELETE FROM ajuda WHERE idAjuda = ?";
        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,id);
                stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Erro ao deletar ajuda: " + e.getMessage());
        }
    }
}