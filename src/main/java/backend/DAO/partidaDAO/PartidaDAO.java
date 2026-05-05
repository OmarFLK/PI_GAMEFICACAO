package backend.DAO.partidaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import backend.servidor.Conexao;

public class PartidaDAO {

    /**
     * Salva o resultado final de uma partida no banco de dados.
     * Este método deve ser chamado apenas quando o aluno finaliza o quiz.
     */
    public void salvarResultadoFinal(int idUsuario, int pontuacaoFinal) {
        // SQL baseado na sua modelagem: idPartida é AI, dataInicio é TIMESTAMP automático
        String sql = "INSERT INTO Partida (idUsuario, Pontuacao) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, pontuacaoFinal);

            stmt.executeUpdate();
            System.out.println("Partida salva com sucesso para o usuário ID: " + idUsuario);
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar resultado da partida: " + e.getMessage());
        }
    }

    /**
     * Busca os dados de uma partida específica pelo seu ID.
     */
    public Partida getPartida(int idPartida) {
        String sql = "SELECT idPartida, idUsuario, Pontuacao, dataInicio FROM Partida WHERE idPartida = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPartida);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Partida(
                    rs.getInt("idPartida"),
                    rs.getInt("idUsuario"),
                    rs.getInt("Pontuacao"),
                    rs.getTimestamp("dataInicio").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar partida: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista todas as partidas registradas no banco (útil para relatórios do professor).
     */
    public List<Partida> listarTodasPartidas() {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT idPartida, idUsuario, Pontuacao, dataInicio FROM Partida ORDER BY dataInicio DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Partida(
                    rs.getInt("idPartida"),
                    rs.getInt("idUsuario"),
                    rs.getInt("Pontuacao"),
                    rs.getTimestamp("dataInicio").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar partidas: " + e.getMessage());
        }
        return lista;
    }
}