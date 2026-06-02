package backend.DAO.partidaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import backend.servidor.Conexao;

public class PartidaDAO {

    public void salvarResultadoFinal(int idUsuario, int pontuacaoFinal, int numAcertos, int numErros) {
        String sql = "INSERT INTO Partida (idUsuario, Pontuacao, numAcertos, numErros) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, pontuacaoFinal);
            stmt.setInt(3, numAcertos);
            stmt.setInt(4, numErros);

            stmt.executeUpdate();
            System.out.println("Partida salva com sucesso para o usuario ID: " + idUsuario);

        } catch (SQLException e) {
            System.err.println("Erro ao salvar resultado da partida: " + e.getMessage());
        }
    }

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
                        rs.getTimestamp("dataInicio").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar partida: " + e.getMessage());
        }
        return null;
    }

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
                        rs.getTimestamp("dataInicio").toLocalDateTime()));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar partidas: " + e.getMessage());
        }
        return lista;
    }

    public DadosAcumuladosAluno buscarEstatisticasDoAluno(int idUsuario) {
        String sql = "SELECT SUM(Pontuacao) as totalPoints, " +
                "COUNT(idPartida) as totalPartidas, " +
                "SUM(numAcertos) as totalAcertos, " +
                "SUM(numErros) as totalErros " +
                "FROM Partida WHERE idUsuario = ?";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int pontos = rs.getInt("totalPoints");
                int partidas = rs.getInt("totalPartidas");
                int acertos = rs.getInt("totalAcertos");
                int erros = rs.getInt("totalErros");
                int questoes = acertos + erros;
                int aproveitamento = questoes > 0 ? (acertos * 100) / questoes : 0;

                return new DadosAcumuladosAluno(pontos, partidas, questoes, acertos, erros, aproveitamento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar estatisticas acumuladas: " + e.getMessage());
        }
        return new DadosAcumuladosAluno(0, 0, 0, 0, 0, 0);
    }

    public DadosGeraisProfessor buscarEstatisticasGerais() {
        String sql = "SELECT COUNT(idPartida) as totalPartidas, " +
                "AVG(Pontuacao) as mediaPontos, " +
                "MAX(Pontuacao) as maxPontos, " +
                "SUM(numAcertos) as totalAcertos, " +
                "SUM(numErros) as totalErros " +
                "FROM Partida";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int totalPartidas = rs.getInt("totalPartidas");
                int media = (int) rs.getDouble("mediaPontos");
                int max = rs.getInt("maxPontos");
                int acertos = rs.getInt("totalAcertos");
                int erros = rs.getInt("totalErros");
                int totalQst = acertos + erros;
                int aproveitamento = totalQst > 0 ? (acertos * 100) / totalQst : 0;

                return new DadosGeraisProfessor(totalPartidas, media, max, acertos, erros, aproveitamento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar estatisticas gerais: " + e.getMessage());
        }
        return new DadosGeraisProfessor(0, 0, 0, 0, 0, 0);
    }

    public List<RankingItem> buscarRankingGeral() {
        List<RankingItem> lista = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.nomeUsuario, " +
                "COALESCE(SUM(p.Pontuacao), 0) as totalPoints, " +
                "COALESCE(SUM(p.numAcertos), 0) as totalAcertos, " +
                "COALESCE(SUM(p.numErros), 0) as totalErros " +
                "FROM usuario u " +
                "LEFT JOIN Partida p ON p.idUsuario = u.idUsuario " +
                "WHERE u.tipo = 'ALUNO' " +
                "GROUP BY u.idUsuario, u.nomeUsuario " +
                "ORDER BY totalPoints DESC, u.nomeUsuario ASC LIMIT 10";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new RankingItem(
                        rs.getInt("idUsuario"),
                        rs.getString("nomeUsuario"),
                        rs.getInt("totalPoints"),
                        rs.getInt("totalAcertos"),
                        rs.getInt("totalErros")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ranking geral: " + e.getMessage());
        }
        return lista;
    }

    public ComparativoAluno buscarComparativoAluno(int idUsuario) {
        String sql = "SELECT " +
                "COALESCE(SUM(CASE WHEN u.idUsuario = ? THEN p.numAcertos ELSE 0 END), 0) as acertosAluno, " +
                "COALESCE(SUM(CASE WHEN u.idUsuario <> ? THEN p.numAcertos ELSE 0 END), 0) as acertosOutros, " +
                "COALESCE(SUM(CASE WHEN u.idUsuario = ? THEN p.numErros ELSE 0 END), 0) as errosAluno, " +
                "COALESCE(SUM(CASE WHEN u.idUsuario <> ? THEN p.numErros ELSE 0 END), 0) as errosOutros " +
                "FROM usuario u " +
                "LEFT JOIN Partida p ON p.idUsuario = u.idUsuario " +
                "WHERE u.tipo = 'ALUNO'";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUsuario);
            stmt.setInt(3, idUsuario);
            stmt.setInt(4, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ComparativoAluno(
                        rs.getInt("acertosAluno"),
                        rs.getInt("acertosOutros"),
                        rs.getInt("errosAluno"),
                        rs.getInt("errosOutros"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar comparativo do aluno: " + e.getMessage());
        }
        return new ComparativoAluno(0, 0, 0, 0);
    }

    public static class DadosAcumuladosAluno {
        private final int pontuacaoTotal;
        private final int partidasJogadas;
        private final int questoesRespondidas;
        private final int acertos;
        private final int erros;
        private final int aproveitamento;

        public DadosAcumuladosAluno(int pt, int pj, int qr, int ac, int er, int ap) {
            this.pontuacaoTotal = pt;
            this.partidasJogadas = pj;
            this.questoesRespondidas = qr;
            this.acertos = ac;
            this.erros = er;
            this.aproveitamento = ap;
        }

        public int getPontuacaoTotal() { return pontuacaoTotal; }
        public int getPartidasJogadas() { return partidasJogadas; }
        public int getQuestoesRespondidas() { return questoesRespondidas; }
        public int getAcertos() { return acertos; }
        public int getErros() { return erros; }
        public int getAproveitamento() { return aproveitamento; }
    }

    public static class DadosGeraisProfessor {
        private final int totalPartidas;
        private final int pontuacaoMedia;
        private final int maiorPontuacao;
        private final int totalAcertos;
        private final int totalErros;
        private final int aproveitamentoGeral;

        public DadosGeraisProfessor(int tp, int pm, int mp, int ta, int te, int ap) {
            this.totalPartidas = tp;
            this.pontuacaoMedia = pm;
            this.maiorPontuacao = mp;
            this.totalAcertos = ta;
            this.totalErros = te;
            this.aproveitamentoGeral = ap;
        }

        public int getTotalPartidas() { return totalPartidas; }
        public int getPontuacaoMedia() { return pontuacaoMedia; }
        public int getMaiorPontuacao() { return maiorPontuacao; }
        public int getTotalAcertos() { return totalAcertos; }
        public int getTotalErros() { return totalErros; }
        public int getAproveitamentoGeral() { return aproveitamentoGeral; }
    }

    public static class RankingItem {
        private final int idUsuario;
        private final String nome;
        private final int pontuacao;
        private final int acertos;
        private final int erros;

        public RankingItem(String nome, int pontuacao) {
            this(0, nome, pontuacao, 0, 0);
        }

        public RankingItem(int idUsuario, String nome, int pontuacao, int acertos, int erros) {
            this.idUsuario = idUsuario;
            this.nome = nome;
            this.pontuacao = pontuacao;
            this.acertos = acertos;
            this.erros = erros;
        }

        public int getIdUsuario() { return idUsuario; }
        public String getNome() { return nome; }
        public int getPontuacao() { return pontuacao; }
        public int getAcertos() { return acertos; }
        public int getErros() { return erros; }
    }

    public static class ComparativoAluno {
        private final int acertosAluno;
        private final int acertosOutros;
        private final int errosAluno;
        private final int errosOutros;

        public ComparativoAluno(int acertosAluno, int acertosOutros, int errosAluno, int errosOutros) {
            this.acertosAluno = acertosAluno;
            this.acertosOutros = acertosOutros;
            this.errosAluno = errosAluno;
            this.errosOutros = errosOutros;
        }

        public int getAcertosAluno() { return acertosAluno; }
        public int getAcertosOutros() { return acertosOutros; }
        public int getErrosAluno() { return errosAluno; }
        public int getErrosOutros() { return errosOutros; }
    }
}
