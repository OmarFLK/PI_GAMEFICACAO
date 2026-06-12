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
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class RankingTela extends TelaBase {

    private final String tipoUsuario;
    private final PartidaDAO partidaDAO;

    private static final Color COR_TEXTO_PRINCIPAL = AppTheme.TEXT;
    private static final Color COR_TEXTO_MUTED = AppTheme.TEXT_MUTED;
    private static final Color COR_ALUNO = AppTheme.STUDENT_HIGHLIGHT;
    private static final Color COR_TOP_1 = AppTheme.RANK_FIRST;
    private static final Color COR_OUTROS = AppTheme.NEUTRAL_DARK;

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
        conteudo.add(criarCorpo(idUsuario, nomeAluno, ranking, comparativo), BorderLayout.CENTER);

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
        titulo.setForeground(COR_TEXTO_PRINCIPAL);

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

    private JPanel criarCorpo(int idUsuario, String nomeAluno, List<RankingItem> ranking, ComparativoAluno comparativo) {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 30, 0));
        corpo.setOpaque(false);

        JPanel colunaRanking = new JPanel();
        colunaRanking.setOpaque(false);
        colunaRanking.setLayout(new BoxLayout(colunaRanking, BoxLayout.Y_AXIS));
        colunaRanking.add(criarAreaGrafico(
                "Pontuação dos Alunos",
                "Comparação geral por pontos acumulados",
                new RankingBarChartPanel(ranking, idUsuario)));
        colunaRanking.add(Box.createVerticalStrut(18));
        colunaRanking.add(criarListaRanking(ranking, idUsuario));

        JPanel colunaComparativos = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaComparativos.setOpaque(false);
        colunaComparativos.add(criarAreaGrafico(
                "Acertos: você vs outros alunos",
                nomeAluno + " comparado ao acumulado dos demais alunos",
                new DonutComparativoPanel(
                        comparativo.getAcertosAluno(),
                        comparativo.getAcertosOutros(),
                        "Você",
                        "Outros alunos",
                        COR_ALUNO,
                        COR_OUTROS)));
        colunaComparativos.add(criarAreaGrafico(
                "Erros: você vs outros alunos",
                nomeAluno + " comparado ao acumulado dos demais alunos",
                new DonutComparativoPanel(
                        comparativo.getErrosAluno(),
                        comparativo.getErrosOutros(),
                        "Você",
                        "Outros alunos",
                        COR_ALUNO,
                        COR_OUTROS)));

        corpo.add(colunaRanking);
        corpo.add(colunaComparativos);
        return corpo;
    }

    private JPanel criarListaRanking(List<RankingItem> ranking, int idUsuario) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(0, 220));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        JLabel titulo = new JLabel("Top alunos por pontuação", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(COR_TEXTO_PRINCIPAL);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        card.add(titulo);
        card.add(Box.createVerticalStrut(8));

        if (ranking.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma partida registrada até o momento.", SwingConstants.CENTER);
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(COR_TEXTO_MUTED);
            vazio.setAlignmentX(CENTER_ALIGNMENT);
            vazio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            card.add(vazio);
            return card;
        }

        int limite = Math.min(5, ranking.size());
        for (int i = 0; i < limite; i++) {
            card.add(criarLinhaRanking(ranking.get(i), i, idUsuario));
            if (i < limite - 1) {
                card.add(Box.createVerticalStrut(3));
            }
        }
        return card;
    }

    private JPanel criarLinhaRanking(RankingItem item, int indice, int idUsuario) {
        Color corDestaque = getRankingHighlightColor(item, indice, idUsuario);
        boolean isAlunoLogado = isLoggedStudent(item, idUsuario);
        int estiloFonte = indice == 0 || isAlunoLogado ? Font.BOLD : Font.PLAIN;

        JPanel linha = new JPanel(new BorderLayout(10, 0));
        linha.setOpaque(false);
        linha.setAlignmentX(LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        linha.setPreferredSize(new Dimension(0, 25));
        linha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, corDestaque),
                BorderFactory.createEmptyBorder(2, 10, 2, 6)));

        JLabel posicao = new JLabel((indice + 1) + ".", SwingConstants.CENTER);
        posicao.setPreferredSize(new Dimension(28, 20));
        posicao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        posicao.setForeground(corDestaque);

        String nomeExibido = abreviarNome(item.getNome(), isAlunoLogado ? 20 : 27);
        if (isAlunoLogado) {
            nomeExibido += " (Você)";
        }
        JLabel nome = new JLabel(nomeExibido);
        nome.setToolTipText(item.getNome());
        nome.setFont(new Font("Segoe UI", estiloFonte, 14));
        nome.setForeground(corDestaque);

        JLabel pontos = new JLabel(item.getPontuacao() + " pts");
        pontos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pontos.setForeground(corDestaque);

        linha.add(posicao, BorderLayout.WEST);
        linha.add(nome, BorderLayout.CENTER);
        linha.add(pontos, BorderLayout.EAST);
        return linha;
    }

    private JPanel criarAreaGrafico(String titulo, String subtitulo, JPanel grafico) {
        JPanel area = criarCartaoSuave();
        area.setLayout(new BorderLayout(0, 8));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLabel.setForeground(COR_TEXTO_PRINCIPAL);

        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtituloLabel.setForeground(COR_TEXTO_MUTED);

        header.add(tituloLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(subtituloLabel);

        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    private class RankingBarChartPanel extends JPanel {
        private final List<RankingItem> ranking;
        private final int idUsuario;

        RankingBarChartPanel(List<RankingItem> ranking, int idUsuario) {
            this.ranking = ranking;
            this.idUsuario = idUsuario;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 260));
            setMinimumSize(new Dimension(0, 190));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ThemePalette palette = ThemeManager.getCurrentPalette();

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

            g2.setColor(palette.chartTrack());
            g2.drawLine(leftSpace, baseY, getWidth() - rightSpace, baseY);

            for (int i = 0; i < limite; i++) {
                RankingItem item = ranking.get(i);
                int height = Math.max(8, Math.round(chartHeight * (item.getPontuacao() / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;

                Color corBarra = getRankingHighlightColor(item, i, idUsuario);
                g2.setColor(corBarra);
                g2.fillRoundRect(x, y, barWidth, height, 12, 12);
                g2.fillRect(x, baseY - 5, barWidth, 5);

                g2.setColor(palette.textPrimary());
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valor = item.getPontuacao() + " pts";
                int valorX = x + barWidth / 2 - g2.getFontMetrics().stringWidth(valor) / 2;
                g2.drawString(valor, valorX, y - 8);

                g2.setColor(corBarra);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                String nome = isLoggedStudent(item, idUsuario) ? "Você" : abreviarNome(item.getNome());
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
            ThemePalette palette = ThemeManager.getCurrentPalette();

            int total = valorAluno + valorOutros;
            if (total <= 0) {
                desenharMensagemVazia(g2, "Sem respostas registradas.", getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int size = Math.min(140, Math.min(getHeight() - 16, getWidth() - 220));
            size = Math.max(90, size);
            int x = 24;
            int y = (getHeight() - size) / 2;

            int arcoAluno = Math.round(360f * valorAluno / total);
            g2.setColor(ThemeManager.resolveForeground(corOutrosGrafico));
            g2.fillOval(x, y, size, size);
            g2.setColor(ThemeManager.resolveForeground(corAluno));
            g2.fillArc(x, y, size, size, 90, -arcoAluno);

            g2.setColor(palette.cardBackground());
            int furo = Math.round(size * 0.62f);
            int furoOffset = (size - furo) / 2;
            g2.fillOval(x + furoOffset, y + furoOffset, furo, furo);

            int percentualAluno = Math.round(100f * valorAluno / total);
            String centro = percentualAluno + "%";
            g2.setColor(palette.textPrimary());
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            int textoX = x + size / 2 - g2.getFontMetrics().stringWidth(centro) / 2;
            g2.drawString(centro, textoX, y + size / 2 + 8);

            int legendaX = x + size + 44;
            int legendaY = y + size / 2 - 24;
            desenharLegenda(g2, legendaX, legendaY, ThemeManager.resolveForeground(corAluno), labelAluno + ": " + valorAluno);
            desenharLegenda(g2, legendaX, legendaY + 32, ThemeManager.resolveForeground(corOutrosGrafico), labelOutros + ": " + valorOutros);
            g2.dispose();
        }

        private void desenharLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y - 12, 16, 16, 6, 6);
            g2.setColor(ThemeManager.getCurrentPalette().textPrimary());
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString(texto, x + 26, y + 1);
        }
    }

    private void desenharMensagemVazia(Graphics2D g2, String texto, int largura, int altura) {
        g2.setColor(ThemeManager.getCurrentPalette().textSecondary());
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        int x = largura / 2 - g2.getFontMetrics().stringWidth(texto) / 2;
        int y = altura / 2;
        g2.drawString(texto, Math.max(12, x), y);
    }

    private String abreviarNome(String nome) {
        return abreviarNome(nome, 12);
    }

    private String abreviarNome(String nome, int limite) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Aluno";
        }

        String texto = nome.trim();
        if (texto.length() <= limite) {
            return texto;
        }
        return texto.substring(0, Math.max(1, limite - 1)) + "...";
    }

    private boolean isLoggedStudent(RankingItem item, int idUsuario) {
        return idUsuario > 0 && item.getIdUsuario() == idUsuario;
    }

    private Color getRankingHighlightColor(RankingItem item, int indice, int idUsuario) {
        ThemePalette palette = ThemeManager.getCurrentPalette();
        if (indice == 0) {
            return palette.topOneGold();
        }
        if (isLoggedStudent(item, idUsuario)) {
            return palette.primaryRed();
        }
        return palette.neutralChart();
    }
}
