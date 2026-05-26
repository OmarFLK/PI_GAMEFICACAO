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
import backend.DAO.partidaDAO.PartidaDAO.DadosGeraisProfessor;
import backend.DAO.partidaDAO.PartidaDAO.RankingItem;
import frontend.base.TelaBase;
import frontend.util.Navegador;

public class TurmasEstatisticasTela extends TelaBase {

    private final PartidaDAO partidaDAO;

    // Paleta de Cores do Dashboard Moderno
    private final Color COR_FUNDO_CARD = Color.WHITE;
    private final Color COR_BORDA_CARD = new Color(230, 235, 240);
    private final Color COR_TEXTO_PRINCIPAL = new Color(44, 62, 80);
    private final Color COR_TEXTO_MUTED = new Color(127, 140, 141);
    
    private final Color COLOR_ACERTOS = new Color(46, 204, 113); // Verde
    private final Color COLOR_ERROS = new Color(231, 76, 60);    // Vermelho
    private final Color COLOR_PONTOS = new Color(52, 152, 219);  // Azul

    public TurmasEstatisticasTela() {
        super("QuimLab - Painel Geral de Estatísticas");
        this.partidaDAO = new PartidaDAO();
        initComponents();
    }

    private void initComponents() {
        // Coleta os dados reais agregados direto do banco MySQL
        DadosGeraisProfessor dadosGlobais = partidaDAO.buscarEstatisticasGerais();
        List<RankingItem> rankingGeral = partidaDAO.buscarRankingGeral();

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 20));
        conteudo.setOpaque(false);
        
        conteudo.add(criarTopo(), BorderLayout.NORTH);
        conteudo.add(criarCorpo(dadosGlobais, rankingGeral), BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JLabel titulo = new JLabel("Painel Geral de Desempenho", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(COR_TEXTO_PRINCIPAL);

        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));
        
        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        painelVoltar.setOpaque(false);
        painelVoltar.add(voltarButton);

        topo.add(titulo, BorderLayout.CENTER);
        topo.add(painelVoltar, BorderLayout.EAST);
        return topo;
    }

    private JPanel criarCorpo(DadosGeraisProfessor dados, List<RankingItem> ranking) {
        JPanel corpo = new JPanel(new GridLayout(1, 2, 30, 0));
        corpo.setOpaque(false);

        // COLUNA DA ESQUERDA - Indicadores Brutos e Tabela de Ranking
        JPanel colunaDados = new JPanel();
        colunaDados.setOpaque(false);
        colunaDados.setLayout(new BoxLayout(colunaDados, BoxLayout.Y_AXIS));

        JLabel tituloDados = new JLabel("Métricas Globais do Sistema");
        tituloDados.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDados.setForeground(COR_TEXTO_PRINCIPAL);
        tituloDados.setAlignmentX(LEFT_ALIGNMENT);
        colunaDados.add(tituloDados);
        colunaDados.add(Box.createVerticalStrut(14));

        // Grid com os cartões puxando dados reais das somas do banco
        JPanel cardsGrid = new JPanel(new GridLayout(3, 2, 16, 16));
        cardsGrid.setOpaque(false);
        
        cardsGrid.add(criarCartaoEstatistica("Quizzes Concluídos", String.valueOf(dados.getTotalPartidas()), "Total de partidas jogadas", COLOR_PONTOS));
        cardsGrid.add(criarCartaoEstatistica("Pontuação Média", dados.getPontuacaoMedia() + " pts", "Média por partida", COLOR_PONTOS));
        cardsGrid.add(criarCartaoEstatistica("Recorde do Quiz", dados.getMaiorPontuacao() + " pts", "Maior pontuação única", COLOR_ACERTOS));
        cardsGrid.add(criarCartaoEstatistica("Aproveitamento Geral", dados.getAproveitamentoGeral() + "%", "Taxa global de acertos", COLOR_ACERTOS));
        cardsGrid.add(criarCartaoEstatistica("Respostas Corretas", String.valueOf(dados.getTotalAcertos()), "Total acumulado", COLOR_ACERTOS));
        cardsGrid.add(criarCartaoEstatistica("Respostas Incorretas", String.valueOf(dados.getTotalErros()), "Total acumulado", COLOR_ERROS));

        colunaDados.add(cardsGrid);
        colunaDados.add(Box.createVerticalStrut(24));
        colunaDados.add(criarRankingAlunos(ranking));

        // COLUNA DA DIREITA - Gráficos Analíticos Dinâmicos
        JPanel colunaGraficos = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaGraficos.setOpaque(false);
        
        colunaGraficos.add(criarAreaGrafico(
                "Aproveitamento Geral de Respostas",
                "Percentual agregado de acertos vs erros de todos os estudantes",
                new DonutChartPanel(dados.getAproveitamentoGeral(), dados.getAproveitamentoGeral() + "%")));
        
        colunaGraficos.add(criarAreaGrafico(
                "Volume Absoluto de Respostas",
                "Comparação direta da quantidade de questões respondidas",
                new BarChartPanel(
                        new String[] {"Acertos", "Erros"},
                        new int[] {dados.getTotalAcertos(), dados.getTotalErros()})));

        corpo.add(colunaDados);
        corpo.add(colunaGraficos);
        return corpo;
    }

    private JPanel criarCartaoEstatistica(String titulo, String valor, String detalhe, Color corValor) {
        JPanel card = new PainelArredondado(14, COR_FUNDO_CARD, COR_BORDA_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloLabel.setForeground(COR_TEXTO_MUTED);

        JLabel valorLabel = new JLabel(valor);
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valorLabel.setForeground(corValor);

        JLabel detalheLabel = new JLabel(detalhe);
        detalheLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detalheLabel.setForeground(COR_TEXTO_MUTED);

        card.add(tituloLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(valorLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(detalheLabel);
        return card;
    }

    private JPanel criarRankingAlunos(List<RankingItem> ranking) {
        JPanel card = new PainelArredondado(16, COR_FUNDO_CARD, COR_BORDA_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("Líderes de Pontuação (Global)", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(COR_TEXTO_PRINCIPAL);
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(12));

        if (ranking.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma partida registrada até o momento.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(COR_TEXTO_MUTED);
            card.add(vazio);
        } else {
            for (int i = 0; i < ranking.size(); i++) {
                RankingItem item = ranking.get(i);
                JPanel linha = new JPanel(new BorderLayout());
                linha.setOpaque(false);
                
                JLabel nome = new JLabel((i + 1) + ". " + item.getNome());
                nome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                nome.setForeground(COR_TEXTO_PRINCIPAL);
                
                JLabel pontos = new JLabel(item.getPontuacao() + " pts");
                pontos.setFont(new Font("Segoe UI", Font.BOLD, 13));
                pontos.setForeground(i == 0 ? COLOR_ACERTOS : COR_TEXTO_MUTED);
                
                linha.add(nome, BorderLayout.WEST);
                linha.add(pontos, BorderLayout.EAST);
                card.add(linha);
                card.add(Box.createVerticalStrut(6));
            }
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
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloLabel.setForeground(COR_TEXTO_PRINCIPAL);
        
        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtituloLabel.setForeground(COR_TEXTO_MUTED);

        header.add(tituloLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(subtituloLabel);

        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    // Componente gráfico customizado para bordas arredondadas organizadas
    private class PainelArredondado extends JPanel {
        private final int raio;
        private final Color corFundo;
        private final Color corBorda;

        PainelArredondado(int raio, Color corFundo, Color corBorda) {
            this.raio = raio;
            this.corFundo = corFundo;
            this.corBorda = corBorda;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(corFundo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            
            g2.setColor(corBorda);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.dispose();
        }
    }

    // Gráfico de Donut Global (Acertos vs Erros do Sistema)
    private class DonutChartPanel extends JPanel {
        private final int percentagem;
        private final String textoCentral;

        DonutChartPanel(int percentagem, String textoCentral) {
            this.percentagem = percentagem;
            this.textoCentral = textoCentral;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(130, Math.min(getHeight() - 20, getWidth() - 220));
            int x = 20;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(240, 244, 248));
            g2.fillOval(x, y, size, size);
            
            g2.setColor(COLOR_ACERTOS);
            g2.fillArc(x, y, size, size, 90, -Math.round(360f * percentagem / 100f));
            
            g2.setColor(getBackground());
            g2.fillOval(x + 22, y + 22, size - 44, size - 44);

            g2.setColor(COR_TEXTO_PRINCIPAL);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            int textoX = x + size / 2 - g2.getFontMetrics().stringWidth(textoCentral) / 2;
            g2.drawString(textoCentral, textoX, y + size / 2 + 8);

            int legendaX = x + size + 40;
            int legendaY = y + (size / 2) - 15;
            
            desenharLegenda(g2, legendaX, legendaY, COLOR_ACERTOS, "Acertos: " + percentagem + "%");
            desenharLegenda(g2, legendaX, legendaY + 26, COLOR_ERROS, "Erros: " + (100 - percentagem) + "%");
            g2.dispose();
        }

        private void desenharLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y - 10, 14, 14, 6, 6);
            g2.setColor(COR_TEXTO_PRINCIPAL);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.drawString(texto, x + 24, y + 2);
        }
    }

    // Gráfico de Barras Global (Métricas Brutas Totais)
    private class BarChartPanel extends JPanel {
        private final String[] rotulos;
        private final int[] valores;

        BarChartPanel(String[] rotulos, int[] valores) {
            this.rotulos = rotulos;
            this.valores = valores;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int max = Math.max(1, getMaximo());
            int topSpace = 25;
            int bottomSpace = 25;
            int chartHeight = getHeight() - topSpace - bottomSpace;
            int barWidth = 52;
            int gap = 45;
            int totalWidth = valores.length * barWidth + (valores.length - 1) * gap;
            int startX = (getWidth() - totalWidth) / 2;
            int baseY = getHeight() - bottomSpace;

            for (int i = 0; i < valores.length; i++) {
                int height = Math.max(8, Math.round(chartHeight * (valores[i] / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;
                
                Color corBarra = (i == 0) ? COLOR_ACERTOS : COLOR_ERROS;

                g2.setColor(corBarra);
                g2.fillRoundRect(x, y, barWidth, height, 10, 10);
                g2.fillRect(x, baseY - 5, barWidth, 5); 

                g2.setColor(COR_TEXTO_PRINCIPAL);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valorStr = valores[i] + " qst";
                g2.drawString(valorStr, x + barWidth / 2 - g2.getFontMetrics().stringWidth(valorStr) / 2, y - 6);

                g2.setColor(COR_TEXTO_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString(rotulos[i], x + barWidth / 2 - g2.getFontMetrics().stringWidth(rotulos[i]) / 2, baseY + 18);
            }
            g2.dispose();
        }

        private int getMaximo() {
            int max = 0;
            for (int val : valores) {
                if (val > max) max = val;
            }
            return max;
        }
    }
}