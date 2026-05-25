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

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.RankingMock;
import frontend.mock.DadosMockados.TurmaEstatisticaMock;
import frontend.util.Navegador;

public class TurmasEstatisticasTela extends TelaBase {

    private final List<TurmaEstatisticaMock> turmas = DadosMockados.getTurmasEstatisticas();
    private String filtroAtual = "Primeiro Ano";
    private JPanel painelFiltros;
    private JPanel painelConteudo;

    public TurmasEstatisticasTela() {
        super("QuimLab - Estat\u00edsticas das Turmas");
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(18, 70, 18, 70));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 12));
        conteudo.setOpaque(false);
        conteudo.add(criarTopo(), BorderLayout.NORTH);

        JPanel miolo = new JPanel(new BorderLayout(0, 12));
        miolo.setOpaque(false);

        painelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        painelFiltros.setOpaque(false);
        atualizarFiltros();

        painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setOpaque(false);
        atualizarConteudo();

        miolo.add(painelFiltros, BorderLayout.NORTH);
        miolo.add(painelConteudo, BorderLayout.CENTER);
        conteudo.add(miolo, BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(130, 46));

        JLabel titulo = new JLabel("Estat\u00edsticas das Turmas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(COR_AZUL_ESCURO);

        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));
        JPanel painelVoltar = new JPanel();
        painelVoltar.setOpaque(false);
        painelVoltar.setPreferredSize(new Dimension(130, 46));
        painelVoltar.add(voltarButton);

        topo.add(placeholder, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(painelVoltar, BorderLayout.EAST);
        return topo;
    }

    private void atualizarFiltros() {
        painelFiltros.removeAll();
        painelFiltros.add(criarBotaoFiltro("Primeiro Ano"));
        painelFiltros.add(criarBotaoFiltro("Segundo Ano"));
        painelFiltros.add(criarBotaoFiltro("Terceiro Ano"));
        painelFiltros.add(criarBotaoFiltro("Geral"));
        painelFiltros.revalidate();
        painelFiltros.repaint();
    }

    private JButton criarBotaoFiltro(String filtro) {
        JButton botao = filtro.equals(filtroAtual) ? criarBotaoPrincipal(filtro) : criarBotaoNeutro(filtro);
        botao.setPreferredSize(new Dimension(178, 44));
        botao.setMaximumSize(new Dimension(178, 44));
        botao.addActionListener(evt -> {
            filtroAtual = filtro;
            atualizarFiltros();
            atualizarConteudo();
        });
        return botao;
    }

    private void atualizarConteudo() {
        painelConteudo.removeAll();
        painelConteudo.add("Geral".equals(filtroAtual) ? criarPainelGeral() : criarPainelTurma(getTurmaSelecionada()), BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private JPanel criarPainelTurma(TurmaEstatisticaMock turma) {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 24, 0));
        corpo.setOpaque(false);
        corpo.setPreferredSize(new Dimension(0, 560));

        JPanel colunaEsquerda = new JPanel(new BorderLayout(0, 14));
        colunaEsquerda.setOpaque(false);
        colunaEsquerda.add(criarGridDadosTurma(turma), BorderLayout.NORTH);
        colunaEsquerda.add(criarRankingTurma(turma), BorderLayout.CENTER);

        JPanel colunaDireita = new JPanel(new GridLayout(2, 1, 0, 18));
        colunaDireita.setOpaque(false);
        colunaDireita.add(criarAreaGrafico(
                "Desempenho da turma",
                "Menor, m\u00e9dia e maior pontua\u00e7\u00e3o",
                new BarChartPanel(
                        new String[] {"Menor", "M\u00e9dia", "Maior"},
                        new int[] {turma.getMenorPontuacao(), turma.getMedia(), turma.getMaiorPontuacao()},
                        " pts")));
        colunaDireita.add(criarAreaGrafico(
                "Aproveitamento",
                turma.getNome() + ": " + turma.getAproveitamentoMedio() + "%",
                new DonutChartPanel(turma.getAproveitamentoMedio(), turma.getAproveitamentoMedio() + "%", turma.getNome())));

        corpo.add(colunaEsquerda);
        corpo.add(colunaDireita);
        return corpo;
    }

    private JPanel criarPainelGeral() {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 24, 0));
        corpo.setOpaque(false);
        corpo.setPreferredSize(new Dimension(0, 560));

        JPanel colunaEsquerda = new JPanel(new BorderLayout(0, 14));
        colunaEsquerda.setOpaque(false);
        colunaEsquerda.add(criarGridDadosGeral(), BorderLayout.NORTH);

        JPanel rankings = new JPanel(new GridLayout(3, 1, 0, 10));
        rankings.setOpaque(false);
        for (TurmaEstatisticaMock turma : turmas) {
            rankings.add(criarRankingTurma(turma));
        }
        colunaEsquerda.add(rankings, BorderLayout.CENTER);

        JPanel colunaDireita = new JPanel(new GridLayout(2, 1, 0, 18));
        colunaDireita.setOpaque(false);
        colunaDireita.add(criarAreaGrafico(
                "M\u00e9dia por ano",
                "1\u00ba: 860   |   2\u00ba: 840   |   3\u00ba: 890",
                new BarChartPanel(
                        new String[] {"1\u00ba", "2\u00ba", "3\u00ba"},
                        new int[] {860, 840, 890},
                        " pts")));
        colunaDireita.add(criarAreaGrafico(
                "Aproveitamento por ano",
                "1\u00ba: 86%   |   2\u00ba: 82%   |   3\u00ba: 89%",
                new BarChartPanel(
                        new String[] {"1\u00ba", "2\u00ba", "3\u00ba"},
                        new int[] {86, 82, 89},
                        "%")));

        corpo.add(colunaEsquerda);
        corpo.add(colunaDireita);
        return corpo;
    }

    private JPanel criarGridDadosTurma(TurmaEstatisticaMock turma) {
        JPanel grid = new JPanel(new GridLayout(3, 2, 12, 12));
        grid.setOpaque(false);
        grid.add(criarCartaoEstatistica("Alunos", String.valueOf(turma.getAlunos()), "Ativos"));
        grid.add(criarCartaoEstatistica("M\u00e9dia", turma.getMedia() + " pts", "Geral"));
        grid.add(criarCartaoEstatistica("Maior nota", turma.getMaiorPontuacao() + " pts", turma.getMelhorAluno()));
        grid.add(criarCartaoEstatistica("Menor nota", turma.getMenorPontuacao() + " pts", "Base da turma"));
        grid.add(criarCartaoEstatistica("Aproveitamento", turma.getAproveitamentoMedio() + "%", "M\u00e9dio"));
        grid.add(criarCartaoEstatistica("Melhor aluno", turma.getMelhorAluno(), turma.getNome()));
        return grid;
    }

    private JPanel criarGridDadosGeral() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);
        grid.add(criarCartaoEstatistica("Total de alunos", "78", "Nas tr\u00eas turmas"));
        grid.add(criarCartaoEstatistica("M\u00e9dia geral", "856 pts", "Consolidada"));
        grid.add(criarCartaoEstatistica("Maior nota", "990 pts", "Top geral"));
        grid.add(criarCartaoEstatistica("Aproveitamento", "85%", "Geral"));
        return grid;
    }

    private JPanel criarCartaoEstatistica(String titulo, String valor, String detalhe) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloLabel.setForeground(COR_TEXTO_SUAVE);

        JLabel valorLabel = new JLabel(valor);
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, valor.length() > 12 ? 17 : 22));
        valorLabel.setForeground(COR_AZUL_ESCURO);

        JLabel detalheLabel = new JLabel(detalhe);
        detalheLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detalheLabel.setForeground(COR_TEXTO_SUAVE);

        card.add(tituloLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(valorLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(detalheLabel);
        return card;
    }

    private JPanel criarRankingTurma(TurmaEstatisticaMock turma) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Ranking - " + turma.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(COR_AZUL_ESCURO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(8));

        for (int i = 0; i < turma.getRanking().size(); i++) {
            RankingMock item = turma.getRanking().get(i);
            JPanel linha = new JPanel(new BorderLayout());
            linha.setOpaque(false);
            JLabel nome = criarTexto((i + 1) + ". " + item.getNome());
            nome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel pontos = criarTexto(item.getPontuacao() + " pts");
            pontos.setForeground(COR_TEXTO_SUAVE);
            linha.add(nome, BorderLayout.WEST);
            linha.add(pontos, BorderLayout.EAST);
            card.add(linha);
            card.add(Box.createVerticalStrut(5));
        }
        return card;
    }

    private JPanel criarAreaGrafico(String titulo, String subtitulo, JPanel grafico) {
        JPanel area = new JPanel(new BorderLayout(0, 6));
        area.setOpaque(false);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel tituloLabel = criarTexto(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel subtituloLabel = criarTextoSuave(subtitulo);

        header.add(tituloLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(subtituloLabel);

        grafico.setPreferredSize(new Dimension(0, 210));
        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    private TurmaEstatisticaMock getTurmaSelecionada() {
        for (TurmaEstatisticaMock turma : turmas) {
            if (turma.getNome().equals(filtroAtual)) {
                return turma;
            }
        }
        return turmas.get(0);
    }

    private class DonutChartPanel extends JPanel {
        private final int percentual;
        private final String centro;
        private final String legenda;

        DonutChartPanel(int percentual, String centro, String legenda) {
            this.percentual = percentual;
            this.centro = centro;
            this.legenda = legenda;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(150, Math.min(getHeight() - 28, getWidth() - 240));
            int x = 56;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(232, 236, 240));
            g2.fillOval(x, y, size, size);
            g2.setColor(COR_AZUL_ESCURO);
            g2.fillArc(x, y, size, size, 90, -Math.round(360f * percentual / 100f));
            g2.setColor(COR_BRANCO);
            g2.fillOval(x + 34, y + 34, size - 68, size - 68);

            g2.setColor(COR_AZUL_ESCURO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
            int textoX = x + size / 2 - g2.getFontMetrics().stringWidth(centro) / 2;
            g2.drawString(centro, textoX, y + size / 2 + 9);

            int legendaX = x + size + 56;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString(legenda, legendaX, y + size / 2 - 5);
            g2.setColor(COR_TEXTO_SUAVE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString("aproveitamento m\u00e9dio", legendaX, y + size / 2 + 20);
            g2.dispose();
        }
    }

    private class BarChartPanel extends JPanel {
        private final String[] rotulos;
        private final int[] valores;
        private final String sufixo;

        BarChartPanel(String[] rotulos, int[] valores, String sufixo) {
            this.rotulos = rotulos;
            this.valores = valores;
            this.sufixo = sufixo;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int max = getMaximo();
            int top = 30;
            int bottom = 34;
            int chartHeight = getHeight() - top - bottom;
            int barWidth = 58;
            int gap = 52;
            int totalWidth = valores.length * barWidth + (valores.length - 1) * gap;
            int startX = (getWidth() - totalWidth) / 2;
            int baseY = getHeight() - bottom;

            for (int i = 0; i < valores.length; i++) {
                int height = Math.max(18, Math.round(chartHeight * (valores[i] / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;
                Color cor = i == 0 ? COR_AZUL_ESCURO : (i == 1 ? new Color(118, 148, 184) : COR_VERDE.darker());

                g2.setColor(cor);
                g2.fillRoundRect(x, y, barWidth, height, 14, 14);

                g2.setColor(COR_AZUL_ESCURO);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String valor = valores[i] + sufixo;
                g2.drawString(valor, x + barWidth / 2 - g2.getFontMetrics().stringWidth(valor) / 2, y - 8);

                g2.setColor(COR_TEXTO_SUAVE);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.drawString(rotulos[i], x + barWidth / 2 - g2.getFontMetrics().stringWidth(rotulos[i]) / 2, baseY + 22);
            }
            g2.dispose();
        }

        private int getMaximo() {
            int max = 1;
            for (int valor : valores) {
                max = Math.max(max, valor);
            }
            return max;
        }
    }
}
