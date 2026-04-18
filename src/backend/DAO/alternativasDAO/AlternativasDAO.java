//imports e package
package backend.DAO.alternativasDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import backend.servidor.Conexao;

//classe
public class AlternativasDAO {

    //metodo para pegar a alternativa do banco por id e colocar no molde
    //da classe alternativa
    public Alternativa getAlternativa(int id) {
        String sql = "SELECT idAlternativa, idPergunta, Texto, imagemURL, correta FROM Alternativa WHERE idAlternativa = ?";
        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Alternativa(
                    rs.getInt("idAlternativa"),
                    rs.getInt("idPergunta"),
                    rs.getString("Texto"), 
                    rs.getString("imagemURL"),
                    rs.getInt("correta")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alternativa: " + e.getMessage());
        }
        return null; 
    }

    //metodo para pegar todas as alternativas de uma pergunta,
    //colocar-las no modelo da classe alternativa e colocar
    // todas as alternativas numa lista
    public List<Alternativa> getAlternativasPorPergunta(int idPergunta) {
        List<Alternativa> lista = new ArrayList<>();
        String sql = "SELECT idAlternativa, idPergunta, Texto, imagemURL, correta FROM Alternativa WHERE idPergunta = ?";
        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPergunta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alternativa alt = new Alternativa(
                    rs.getInt("idAlternativa"),
                    rs.getInt("idPergunta"),
                    rs.getString("Texto"),
                    rs.getString("imagemURL"),
                    rs.getInt("correta")
                );
                lista.add(alt);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar alternativa: " + e.getMessage());
        }
        return lista;
    }
}