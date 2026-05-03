//package e imports
package backend.DAO.perguntaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import backend.servidor.Conexao;

//classe
public class PerguntaDAO {

    // Método para criar e adicionar uma pergunta no banco de dados
    public void criarPergunta(String enunciado, String imagemURL, String dificuldade, int criadoPor) {
        String sql = "INSERT INTO perguntas(enunciado, imagemURL, dificuldade, criadoPor) VALUES(?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enunciado);
            // Trata o null para evitar strings vazias no banco
            if (imagemURL == null || imagemURL.trim().isEmpty()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, imagemURL);
            }
            stmt.setString(3, dificuldade);
            stmt.setInt(4, criadoPor);

            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro para criar pergunta: " + e.getMessage());
        }
    }

    // Método para atualizar uma pergunta existente
    public void atualizarPergunta(int id, String enunciado, String imagemURL, String dificuldade, int ativa) {
        String sql = "UPDATE perguntas SET enunciado = ?, imagemURL = ?, dificuldade = ?, ativa = ? WHERE idPergunta = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enunciado);
            // Trata o null no update também
            if (imagemURL == null || imagemURL.trim().isEmpty()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, imagemURL);
            }
            stmt.setString(3, dificuldade);
            stmt.setInt(4, ativa);
            stmt.setInt(5, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pergunta: " + e.getMessage());
        }
    }

    // Método para buscar uma pergunta específica pelo ID
    public Pergunta getPergunta(int idPergunta) {
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
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pergunta: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar perguntas por dificuldade (usado na Gameplay)
    public List<Pergunta> getPerguntasPorDificuldade(String dificuldade) {
        List<Pergunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM perguntas WHERE dificuldade = UPPER(?) AND ativa = 1 ORDER BY RAND()";
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
            System.err.println("Erro ao buscar por dificuldade: " + e.getMessage());
        }
        return lista;
    }

    // Método para buscar todas as perguntas (usado no Gerenciamento)
    public List<Pergunta> getTodasPerguntas() {
        List<Pergunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM perguntas ORDER BY idPergunta DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
            System.err.println("Erro ao listar todas as perguntas: " + e.getMessage());
        }
        return lista;
    }

    // Método para deletar uma pergunta
    public void deletarPergunta(int id) {
        String sql = "DELETE FROM perguntas WHERE idPergunta = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar pergunta: " + e.getMessage());
        }
    }
}