package backend.DAO.partidaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import backend.servidor.Conexao;

public class PartidaDAO {

    // Salva o resultado final completo de uma partida no banco
    public void salvarResultadoFinal(int idUsuario, int pontuacaoFinal, int numAcertos, int numErros) {
        String sql = "INSERT INTO Partida (idUsuario, Pontuacao, numAcertos, numErros) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, pontuacaoFinal);
            stmt.setInt(3, numAcertos);
            stmt.setInt(4, numErros);

            stmt.executeUpdate();
            System.out.println("Partida salva com sucesso para o usuário ID: " + idUsuario);

        } catch (SQLException e) {
            System.err.println("Erro ao salvar resultado da partida: " + e.getMessage());
        }
    }

    // Busca uma partida específica pelo ID
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

    // Lista todas as partidas do banco
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

    // Busca as estatísticas acumuladas de um aluno específico
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
            System.err.println("Erro ao buscar estatísticas acumuladas: " + e.getMessage());
        }
        return new DadosAcumuladosAluno(0, 0, 0, 0, 0, 0);
    }

    // Busca as métricas globais para o painel do professor
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
            System.err.println("Erro ao buscar estatísticas gerais: " + e.getMessage());
        }
        return new DadosGeraisProfessor(0, 0, 0, 0, 0, 0);
    }

    // Busca o ranking dos Top 5 alunos usando os nomes reais das colunas da tabela 'usuario'
    public List<RankingItem> buscarRankingGeral() {
        List<RankingItem> lista = new ArrayList<>();
        // CORRIGIDO: u.nomeUsuario e u.idUsuario baseados na tua base de dados
        String sql = "SELECT u.nomeUsuario, SUM(p.Pontuacao) as totalPoints " +
                "FROM Partida p " +
                "JOIN usuario u ON p.idUsuario = u.idUsuario " +
                "GROUP BY p.idUsuario, u.nomeUsuario " +
                "ORDER BY totalPoints DESC LIMIT 5";

        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // CORRIGIDO: buscar o valor de "nomeUsuario"
                lista.add(new RankingItem(rs.getString("nomeUsuario"), rs.getInt("totalPoints")));
            }
        } catch (SQLException e) {
            System.err.println("Erro no ranking com JOIN, executando fallback por ID: " + e.getMessage());
            return buscarRankingGeralFallback();
        }
        return lista;
    }

    // Fallback de segurança que exibe o ID caso a tabela de usuários falhe
    private List<RankingItem> buscarRankingGeralFallback() {
        List<RankingItem> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, SUM(Pontuacao) as totalPoints FROM Partida GROUP BY idUsuario ORDER BY totalPoints DESC LIMIT 5";
        
        try (Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new RankingItem("Aluno ID: " + rs.getInt("idUsuario"), rs.getInt("totalPoints")));
            }
        } catch (SQLException e) {
            System.err.println("Erro no fallback do ranking: " + e.getMessage());
        }
        return lista;
    }

    // Estrutura de dados para o painel do aluno
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

    // Estrutura de dados para o painel do professor
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

    // Estrutura de dados para os itens do ranking
    public static class RankingItem {
        private final String nome;
        private final int pontuacao;

        public RankingItem(String nome, int pontuacao) {
            this.nome = nome;
            this.pontuacao = pontuacao;
        }

        public String getNome() { return nome; }
        public int getPontuacao() { return pontuacao; }
    }
}