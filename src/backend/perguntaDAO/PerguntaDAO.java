package backend.perguntaDAO;

import java.sql.*;

import backend.servidor.Conexao;

public class PerguntaDAO {

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
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
    
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
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
        return null;
    }

}
