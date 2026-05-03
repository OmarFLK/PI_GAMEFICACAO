//package e imports
package backend.DAO.partidaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import backend.servidor.Conexao;

//classes
public class PartidaDAO {

    //criar partida
    public void criarPartida(int idUsuario, int Pontuacao){
        String sql = "INSERT INTO Partida (idUsuario, Pontuacao) VALUES(?,?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, Pontuacao);

                stmt.executeUpdate();
                
        }catch(SQLException e){;
            System.err.println("Erro ao criar partida: " + e.getMessage());
        }
    }
    
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

    // metodo para atualizar partida
    public void atualizarPontuacao(int idPartida, int novaPontuacao) {
        String sql = "UPDATE Partida SET Pontuacao = ? WHERE idPartida = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, novaPontuacao);
            stmt.setInt(2, idPartida);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Pontuação da partida " + idPartida + " atualizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar partida: " + e.getMessage());
        }
    }

}
