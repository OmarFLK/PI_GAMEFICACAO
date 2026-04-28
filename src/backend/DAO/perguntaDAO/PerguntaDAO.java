//imports e package
package backend.DAO.perguntaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import backend.servidor.Conexao;

//classe
public class PerguntaDAO {

    //metodo para criar e adicionar uma pergunta no banco de dados
    public void criarPergunta(String enunciado,String imagemURL,String dificuldade,int criadoPor){
        String sql = "INSERT INTO perguntas(enunciado, imagemURL, dificuldade, criadoPor) VALUES(?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, enunciado);
            stmt.setString(2, imagemURL);
            stmt.setString(3, dificuldade);
            stmt.setInt(4, criadoPor);

            stmt.executeUpdate();
            
        }catch(SQLException e){
            System.err.println("Erro para criar pergunta: " + e.getMessage());
        }
    }
    
    //metodo para pegar uma pergunta do banco de dados e coloca-lá no molde da classe pergunta
    public Pergunta getPergunta(int idPergunta){
        String sql = "SELECT idPergunta, enunciado, imagemURL, dificuldade, criadoPor, ativa FROM perguntas WHERE idPergunta = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idPergunta);

                ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Pergunta(
                    rs.getInt("idPergunta"),
                    rs.getString("enunciado"),
                    rs.getString("imagemURL"),
                    rs.getString("dificuldade"),
                    rs.getInt("criadoPor"),
                    rs.getInt("ativa")
                );
            }
        }catch(SQLException e){
            System.err.println("Erro ao buscar pergunta: " + e.getMessage());
        }
        return null;
    }

    //metodo para puxar peguntas pela dificuldade, o metodo retorna uma lista de perguntas para a partida
    public List<Pergunta> getPerguntasPorDificuldade(String dificuldade) {
    List<Pergunta> lista = new ArrayList<>();
    //UPPER para garantir que bata com o ENUM do banco (FACIL, MEDIO, DIFICIL)
    String sql = "SELECT idPergunta, enunciado, imagemURL, dificuldade, criadoPor, ativa " +
                 "FROM perguntas WHERE dificuldade = UPPER(?) AND ativa = 1 ORDER BY RAND()";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, dificuldade);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            lista.add(new Pergunta(
                rs.getInt("idPergunta"),
                rs.getString("enunciado"),
                rs.getString("imagemURL"),
                rs.getString("dificuldade"),
                rs.getInt("criadoPor"),
                rs.getInt("ativa")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Erro ao buscar perguntas por dificuldade: " + e.getMessage());
    }
    return lista;
}

    //metodo para puxar perguntas pela dificuldade
    
}
