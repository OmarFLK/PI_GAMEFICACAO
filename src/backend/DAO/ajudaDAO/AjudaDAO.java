//package e import
package backend.DAO.ajudaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import backend.servidor.Conexao;

//classe
public class AjudaDAO {

    // metodo para pegar "ajuda" do banco de dados e colocar no molde ajuda
    public Ajuda getAjuda(int id) {
        String sql = "SELECT idAjuda, nome, descrição FROM ajuda WHERE idAjuda = ?";

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

    
}
