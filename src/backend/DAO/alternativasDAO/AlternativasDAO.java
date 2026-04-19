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

    //criar alternativa
    public void criarAlternativa(int idPergunta, String texto, String imagemURL, int correta){
        String sql = "INSERT INTO Alternativa (idPergunta, texto, imagemURL, correta) VALUES(?,?,?,?)";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,idPergunta);
                stmt.setString(2,texto);
                stmt.setString(3, imagemURL);
                stmt.setInt(4, correta);

                stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Erro ao criar alternativa: " + e.getMessage());
        }
    }

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

    //metodo atualizar alternativa
    public void atualizarAlternativa(int idAlternativa, String texto, String imagemURL, int correta){
        String sql = "UPDATE Alternativa SET texto = ?, imagemURL = ?, correta = ? WHERE idAlternativa = ?";
        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, texto);
            stmt.setString(2, imagemURL);
            stmt.setInt(3, correta);
            stmt.setInt(4,idAlternativa);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Erro ao atualizar alternativa: " + e.getMessage());
        }
    }

    //metodo para deletar alternativa
    public void deletarAlternativa(int id){
        String slq = "DELETE FROM Alternativa WHERE idAlternativa = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(slq)){
            stmt.setInt(1,id);
            
            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Erro ao deletar alternativa: " + e.getMessage());
        }
    }
}