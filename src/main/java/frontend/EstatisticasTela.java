package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backend.DAO.usuarioDAO.Usuario;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.util.Navegador;

public class EstatisticasTela extends TelaBase {

    private static final int MEDIA_TURMA = 860;
    private static final int TOP_1_TURMA = 980;

    private final String tipoUsuario;

    public EstatisticasTela(String tipoUsuario) {
        super("QuimLab - Estat\u00edsticas do Aluno");
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        DadosMockados.EstatisticasAlunoMock dados = DadosMockados.getEstatisticasAlunoMock();
        String nomeAluno = getNomeAluno();

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(18, 72, 18, 72));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 16));
        conteudo.setOpaque(false);
        conteudo.add(criarTopo(), BorderLayout.NORTH);
        conteudo.add(criarCorpo(nomeAluno, dados), BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private String getNomeAluno() {
        Usuario usuario = SessaoUsuario.getInstancia().getUsuario();
        if (usuario != null && usuario.getNome() != null && !usuario.getNome().isBlank()) {
            return usuario.getNome();
        }
        return "Omar da Silva";
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 54));

        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(130, 48));

        JLabel titulo = new JLabel("Estat\u00edsticas do Aluno", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(COR_AZUL_ESCURO);

        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        JPanel painelVoltar = new JPanel();
        painelVoltar.setOpaque(false);
        painelVoltar.setPreferredSize(new Dimension(130, 48));
        painelVoltar.add(voltarButton);

        topo.add(placeholder, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(painelVoltar, BorderLayout.EAST);
        return topo;
    }

    private JPanel criarResumoAluno(String nomeAluno, DadosMockados.EstatisticasAlunoMock dados) {
        JPanel resumo = criarCartaoSuave();
        resumo.setLayout(new BorderLayout(12, 0));
        resumo.setPreferredSize(new Dimension(0, 112));
        resumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        resumo.setMinimumSize(new Dimension(0, 112));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel nome = criarTexto(nomeAluno);
        nome.setFont(new Font("Segoe UI", Font.BOLD, 21));
        JLabel detalhe = criarTextoSuave("1\u00ba ano B | " + dados.getPontuacaoTotal() + " pts | " + dados.getRankingGeral());

        textos.add(nome);
        textos.add(Box.createVerticalStrut(6));
        textos.add(detalhe);
        resumo.add(textos, BorderLayout.CENTER);
        resumo.add(criarBadge("MINHA TURMA", new Color(228, 245, 239), COR_VERDE.darker()), BorderLayout.EAST);
        return resumo;
    }

    private JPanel criarCorpo(String nomeAluno, DadosMockados.EstatisticasAlunoMock dados) {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 24, 0));
        corpo.setOpaque(false);

        JPanel colunaDados = new JPanel(new BorderLayout(0, 14));
        colunaDados.setOpaque(false);

        JPanel topoDados = new JPanel();
        topoDados.setOpaque(false);
        topoDados.setLayout(new BoxLayout(topoDados, BoxLayout.Y_AXIS));
        topoDados.add(criarResumoAluno(nomeAluno, dados));
        topoDados.add(Box.createVerticalStrut(14));

        JLabel tituloDados = criarTexto("Dados do Aluno");
        tituloDados.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tituloDados.setAlignmentX(LEFT_ALIGNMENT);
        topoDados.add(tituloDados);

        JPanel cards = new JPanel(new GridLayout(3, 2, 14, 14));
        cards.setOpaque(false);
        cards.add(criarCartaoEstatistica("Pontua\u00e7\u00e3o", dados.getPontuacaoTotal() + " pts", "Total acumulado"));
        cards.add(criarCartaoEstatistica("Quest\u00f5es", String.valueOf(dados.getQuestoesRespondidas()), "Respondidas"));
        cards.add(criarCartaoEstatistica("Acertos", String.valueOf(dados.getAcertos()), "Corretas"));
        cards.add(criarCartaoEstatistica("Erros", String.valueOf(dados.getErros()), "Incorretas"));
        cards.add(criarCartaoEstatistica("Aproveitamento", dados.getAproveitamento() + "%", "Taxa de acerto"));
        cards.add(criarCartaoEstatistica("Ranking", dados.getRankingGeral(), "Na turma"));

        colunaDados.add(topoDados, BorderLayout.NORTH);
        colunaDados.add(cards, BorderLayout.CENTER);

        JPanel colunaGraficos = new JPanel(new GridLayout(2, 1, 0, 18));
        colunaGraficos.setOpaque(false);
        colunaGraficos.add(criarAreaGrafico(
                "Aproveitamento",
                "Aluno: " + dados.getAproveitamento() + "%   |   Turma: 78%",
                new DonutChartPanel(dados.getAproveitamento(), "84%", "Aluno", "Turma", 78)));
        colunaGraficos.add(criarAreaGrafico(
                "Compara\u00e7\u00e3o de pontua\u00e7\u00e3o",
                "Valores em pontos",
                new BarChartPanel(
                        new String[] {"Voc\u00ea", "M\u00e9dia", "Top 1"},
                        new int[] {dados.getPontuacaoTotal(), MEDIA_TURMA, TOP_1_TURMA},
                        " pts")));

        corpo.add(colunaDados);
        corpo.add(colunaGraficos);
        return corpo;
    }

    private JPanel criarCartaoEstatistica(String titulo, String valor, String detalhe) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloLabel.setForeground(COR_TEXTO_SUAVE);

        JLabel valorLabel = new JLabel(valor);
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        valorLabel.setForeground(COR_AZUL_ESCURO);

        JLabel detalheLabel = new JLabel(detalhe);
        detalheLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detalheLabel.setForeground(COR_TEXTO_SUAVE);

        card.add(tituloLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valorLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(detalheLabel);
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

        grafico.setPreferredSize(new Dimension(0, 185));
        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    private class DonutChartPanel extends JPanel {
        private final int principal;
        private final String centro;
        private final String rotuloPrincipal;
        private final String rotuloComparacao;
        private final int comparacao;

        DonutChartPanel(int principal, String centro, String rotuloPrincipal, String rotuloComparacao, int comparacao) {
            this.principal = principal;
            this.centro = centro;
            this.rotuloPrincipal = rotuloPrincipal;
            this.rotuloComparacao = rotuloComparacao;
            this.comparacao = comparacao;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(150, Math.min(getHeight() - 28, getWidth() - 250));
            int x = 48;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(232, 236, 240));
            g2.fillOval(x, y, size, size);
            g2.setColor(new Color(150, 160, 170));
            g2.fillArc(x, y, size, size, 90, -Math.round(360f * comparacao / 100f));
            g2.setColor(COR_BRANCO);
            g2.fillOval(x + 24, y + 24, size - 48, size - 48);

            int inner = size - 58;
            int ix = x + 29;
            int iy = y + 29;
            g2.setColor(new Color(235, 238, 242));
            g2.fillOval(ix, iy, inner, inner);
            g2.setColor(COR_AZUL_ESCURO);
            g2.fillArc(ix, iy, inner, inner, 90, -Math.round(360f * principal / 100f));
            g2.setColor(COR_BRANCO);
            g2.fillOval(ix + 20, iy + 20, inner - 40, inner - 40);

            g2.setColor(COR_AZUL_ESCURO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 25));
            int textoX = x + size / 2 - g2.getFontMetrics().stringWidth(centro) / 2;
            g2.drawString(centro, textoX, y + size / 2 + 9);

            int legendaX = x + size + 56;
            desenharLegenda(g2, legendaX, y + 48, COR_AZUL_ESCURO, rotuloPrincipal + ": " + principal + "%");
            desenharLegenda(g2, legendaX, y + 88, new Color(150, 160, 170), rotuloComparacao + ": " + comparacao + "%");
            g2.dispose();
        }

        private void desenharLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y - 12, 24, 14, 8, 8);
            g2.setColor(COR_TEXTO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(texto, x + 34, y);
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
