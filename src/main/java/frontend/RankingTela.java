package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backend.DAO.partidaDAO.PartidaDAO;
import backend.DAO.partidaDAO.PartidaDAO.ComparativoAluno;
import backend.DAO.partidaDAO.PartidaDAO.RankingItem;
import backend.DAO.usuarioDAO.Usuario;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.util.Navegador;

public class RankingTela extends TelaBase {

    private final String tipoUsuario;
    private final PartidaDAO partidaDAO;

    private final Color corTextoPrincipal = new Color(44, 62, 80);
    private final Color corTextoMuted = new Color(127, 140, 141);
    private final Color corAcertos = new Color(46, 204, 113);
    private final Color corErros = new Color(231, 76, 60);
    private final Color corPontos = new Color(52, 152, 219);
    private final Color corOutros = new Color(118, 148, 184);

    public RankingTela(String tipoUsuario) {
        super("QuimLab - Ranking Geral");
        this.tipoUsuario = tipoUsuario;
        this.partidaDAO = new PartidaDAO();
        initComponents();
    }

    private void initComponents() {
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuario();
        int idUsuario = usuarioLogado != null ? usuarioLogado.getId() : -1;
        String nomeAluno = usuarioLogado != null ? usuarioLogado.getNome() : "Aluno logado";

        List<RankingItem> ranking = partidaDAO.buscarRankingGeral();
        ComparativoAluno comparativo = partidaDAO.buscarComparativoAluno(idUsuario);

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 20));
        conteudo.setOpaque(false);

        conteudo.add(criarTopo(), BorderLayout.NORTH);
        conteudo.add(criarCorpo(nomeAluno, ranking, comparativo), BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JLabel titulo = new JLabel("Ranking Geral", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(corTextoPrincipal);

        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        painelVoltar.setOpaque(false);
        painelVoltar.add(voltarButton);

        topo.add(titulo, BorderLayout.CENTER);
        topo.add(painelVoltar, BorderLayout.EAST);
        return topo;
    }

    private JPanel criarCorpo(String nomeAluno, List<RankingItem> ranking, ComparativoAluno comparativo) {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 30, 0));
        corpo.setOpaque(false);

        JPanel colunaRanking = new JPanel();
        colunaRanking.setOpaque(false);
        colunaRanking.setLayout(new BoxLayout(colunaRanking, BoxLayout.Y_AXIS));
        colunaRanking.add(criarAreaGrafico(
                "Pontuacao dos Alunos",
                "Comparacao geral por pontos acumulados",
                new RankingBarChartPanel(ranking)));
        colunaRanking.add(Box.createVerticalStrut(18));
        colunaRanking.add(criarListaRanking(ranking));

        JPanel colunaComparativos = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaComparativos.setOpaque(false);
        colunaComparativos.add(criarAreaGrafico(
                "Acertos: voce vs outros alunos",
                nomeAluno + " comparado ao acumulado dos demais alunos",
                new DonutComparativoPanel(
                        comparativo.getAcertosAluno(),
                        comparativo.getAcertosOutros(),
                        "Voce",
                        "Outros alunos",
                        corAcertos,
                        corOutros)));
        colunaComparativos.add(criarAreaGrafico(
                "Erros: voce vs outros alunos",
                nomeAluno + " comparado ao acumulado dos demais alunos",
                new DonutComparativoPanel(
                        comparativo.getErrosAluno(),
                        comparativo.getErrosOutros(),
                        "Voce",
                        "Outros alunos",
                        corErros,
                        corOutros)));

        corpo.add(colunaRanking);
        corpo.add(colunaComparativos);
        return corpo;
    }

    private JPanel criarListaRanking(List<RankingItem> ranking) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(0, 180));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        JLabel titulo = new JLabel("Top alunos por pontuacao", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(corTextoPrincipal);
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(10));

        if (ranking.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma partida registrada ate o momento.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(corTextoMuted);
            card.add(vazio);
            return card;
        }

        int limite = Math.min(5, ranking.size());
        for (int i = 0; i < limite; i++) {
            RankingItem item = ranking.get(i);
            JPanel linha = new JPanel(new BorderLayout());
            linha.setOpaque(false);

            JLabel nome = new JLabel((i + 1) + ". " + item.getNome());
            nome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nome.setForeground(corTextoPrincipal);

            JLabel pontos = new JLabel(item.getPontuacao() + " pts");
            pontos.setFont(new Font("Segoe UI", Font.BOLD, 13));
            pontos.setForeground(i == 0 ? corAcertos : corTextoMuted);

            linha.add(nome, BorderLayout.WEST);
            linha.add(pontos, BorderLayout.EAST);
            card.add(linha);
            card.add(Box.createVerticalStrut(6));
        }
        return card;
    }

    private JPanel criarAreaGrafico(String titulo, String subtitulo, JPanel grafico) {
        JPanel area = new JPanel(new BorderLayout(0, 8));
        area.setOpaque(false);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLabel.setForeground(corTextoPrincipal);

        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtituloLabel.setForeground(corTextoMuted);

        header.add(tituloLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(subtituloLabel);

        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    private class RankingBarChartPanel extends JPanel {
        private final List<RankingItem> ranking;

        RankingBarChartPanel(List<RankingItem> ranking) {
            this.ranking = ranking;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 350));
            setMinimumSize(new Dimension(0, 300));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (ranking.isEmpty()) {
                desenharMensagemVazia(g2, "Sem dados para montar o ranking.", getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int limite = Math.min(7, ranking.size());
            int max = 1;
            for (int i = 0; i < limite; i++) {
                max = Math.max(max, ranking.get(i).getPontuacao());
            }

            int topSpace = 36;
            int bottomSpace = 78;
            int leftSpace = 18;
            int rightSpace = 18;
            int chartHeight = getHeight() - topSpace - bottomSpace;
            int availableWidth = getWidth() - leftSpace - rightSpace;
            int gap = Math.max(12, availableWidth / Math.max(1, limite) / 5);
            int barWidth = Math.max(36, (availableWidth - gap * (limite - 1)) / limite);
            int totalWidth = limite * barWidth + (limite - 1) * gap;
            int startX = leftSpace + Math.max(0, (availableWidth - totalWidth) / 2);
            int baseY = getHeight() - bottomSpace;

            g2.setColor(new Color(233, 238, 244));
            g2.drawLine(leftSpace, baseY, getWidth() - rightSpace, baseY);

            for (int i = 0; i < limite; i++) {
                RankingItem item = ranking.get(i);
                int height = Math.max(8, Math.round(chartHeight * (item.getPontuacao() / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;

                Color corBarra = i == 0 ? corAcertos : corPontos;
                g2.setColor(corBarra);
                g2.fillRoundRect(x, y, barWidth, height, 12, 12);
                g2.fillRect(x, baseY - 5, barWidth, 5);

                g2.setColor(corTextoPrincipal);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valor = item.getPontuacao() + " pts";
                int valorX = x + barWidth / 2 - g2.getFontMetrics().stringWidth(valor) / 2;
                g2.drawString(valor, valorX, y - 8);

                g2.setColor(corTextoMuted);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                String nome = abreviarNome(item.getNome());
                int nomeX = x + barWidth / 2 - g2.getFontMetrics().stringWidth(nome) / 2;
                g2.drawString(nome, nomeX, baseY + 24);
            }
            g2.dispose();
        }
    }

    private class DonutComparativoPanel extends JPanel {
        private final int valorAluno;
        private final int valorOutros;
        private final String labelAluno;
        private final String labelOutros;
        private final Color corAluno;
        private final Color corOutrosGrafico;

        DonutComparativoPanel(int valorAluno, int valorOutros, String labelAluno, String labelOutros, Color corAluno, Color corOutrosGrafico) {
            this.valorAluno = valorAluno;
            this.valorOutros = valorOutros;
            this.labelAluno = labelAluno;
            this.labelOutros = labelOutros;
            this.corAluno = corAluno;
            this.corOutrosGrafico = corOutrosGrafico;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int total = valorAluno + valorOutros;
            if (total <= 0) {
                desenharMensagemVazia(g2, "Sem respostas registradas.", getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int size = Math.min(150, Math.min(getHeight() - 24, getWidth() - 260));
            size = Math.max(110, size);
            int x = 24;
            int y = (getHeight() - size) / 2;

            int arcoAluno = Math.round(360f * valorAluno / total);
            g2.setColor(corOutrosGrafico);
            g2.fillOval(x, y, size, size);
            g2.setColor(corAluno);
            g2.fillArc(x, y, size, size, 90, -arcoAluno);

            g2.setColor(COR_BRANCO);
            int furo = Math.round(size * 0.62f);
            int furoOffset = (size - furo) / 2;
            g2.fillOval(x + furoOffset, y + furoOffset, furo, furo);

            int percentualAluno = Math.round(100f * valorAluno / total);
            String centro = percentualAluno + "%";
            g2.setColor(corTextoPrincipal);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            int textoX = x + size / 2 - g2.getFontMetrics().stringWidth(centro) / 2;
            g2.drawString(centro, textoX, y + size / 2 + 8);

            int legendaX = x + size + 44;
            int legendaY = y + size / 2 - 24;
            desenharLegenda(g2, legendaX, legendaY, corAluno, labelAluno + ": " + valorAluno);
            desenharLegenda(g2, legendaX, legendaY + 32, corOutrosGrafico, labelOutros + ": " + valorOutros);
            g2.dispose();
        }

        private void desenharLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y - 12, 16, 16, 6, 6);
            g2.setColor(corTextoPrincipal);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString(texto, x + 26, y + 1);
        }
    }

    private void desenharMensagemVazia(Graphics2D g2, String texto, int largura, int altura) {
        g2.setColor(corTextoMuted);
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        int x = largura / 2 - g2.getFontMetrics().stringWidth(texto) / 2;
        int y = altura / 2;
        g2.drawString(texto, Math.max(12, x), y);
    }

    private String abreviarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Aluno";
        }

        String texto = nome.trim();
        if (texto.length() <= 12) {
            return texto;
        }
        return texto.substring(0, 11) + "...";
    }
}
