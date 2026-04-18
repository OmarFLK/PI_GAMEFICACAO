//package e imports
package backend.DAO.partidaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import backend.servidor.Conexao;

//classes
public class PartidaDAO {
    
    //metodo para pegar partidas no banco de dados 
    public Partida getPartida(int id){
        String sql = "SELECT idPartida, idUsuario, pontuacao, dataInicio FROM Partida WHERE idPartida = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1,id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    return new Partida(
                        rs.getInt("idPartida"),
                        rs.getInt("idUsuario"),
                        rs.getInt("pontuacao"),
                        rs.getTimestamp("dataInicio").toLocalDateTime()
                    );

                }
            }catch(SQLException e){
                System.err.println("Erro ao buscar partida: " + e.getMessage());
            }
        return null;
    }
}
