package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import backend.DAO.partidaDAO.PartidaDAO;
import backend.DAO.partidaDAO.PartidaDAO.DadosGeraisProfessor;
import backend.DAO.partidaDAO.PartidaDAO.RankingItem;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.Navegador;

public class TurmasEstatisticasTela extends TelaBase {

    private final PartidaDAO partidaDAO;
    private JPanel conteudo; 

    public TurmasEstatisticasTela() {
        super("QuimLab - Painel Geral de Estatísticas");
        this.partidaDAO = new PartidaDAO();
        initComponents();
        carregarDadosEmSegundoPlano(); 
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JPanel canvas = criarCanvasCentral();
        conteudo = new JPanel(new BorderLayout(0, 20));
        conteudo.setOpaque(false);
        
        conteudo.add(criarTopo(), BorderLayout.NORTH);
        
        // Tela de carregamento com cor dinâmica
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setOpaque(false);
        JLabel loadingLabel = new JLabel("Buscando dados das turmas e calculando gráficos...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        loadingLabel.setForeground(ThemeManager.getCurrentPalette().textSecondary());
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        
        conteudo.add(loadingPanel, BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void carregarDadosEmSegundoPlano() {
        SwingWorker<Object[], Void> worker = new SwingWorker<>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                DadosGeraisProfessor dadosGlobais = partidaDAO.buscarEstatisticasGerais();
                List<RankingItem> rankingGeral = partidaDAO.buscarRankingGeral();
                return new Object[]{dadosGlobais, rankingGeral};
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                try {
                    Object[] resultados = get();
                    DadosGeraisProfessor dadosGlobais = (DadosGeraisProfessor) resultados[0];
                    List<RankingItem> rankingGeral = (List<RankingItem>) resultados[1];

                    BorderLayout layout = (BorderLayout) conteudo.getLayout();
                    Component centerComponent = layout.getLayoutComponent(BorderLayout.CENTER);
                    if (centerComponent != null) {
                        conteudo.remove(centerComponent);
                    }

                    conteudo.add(criarCorpo(dadosGlobais, rankingGeral), BorderLayout.CENTER);
                    
                    conteudo.revalidate();
                    conteudo.repaint();

                } catch (Exception e) {
                    BorderLayout layout = (BorderLayout) conteudo.getLayout();
                    Component centerComponent = layout.getLayoutComponent(BorderLayout.CENTER);
                    if (centerComponent != null) {
                        conteudo.remove(centerComponent);
                    }
                    JLabel errorLabel = new JLabel("Erro ao buscar estatísticas. Tente novamente mais tarde.", SwingConstants.CENTER);
                    errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    errorLabel.setForeground(ThemeManager.getCurrentPalette().dangerRed());
                    conteudo.add(errorLabel, BorderLayout.CENTER);
                    conteudo.revalidate();
                    conteudo.repaint();
                }
            }
        };
        worker.execute();
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JLabel titulo = new JLabel("Painel Geral de Desempenho", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(ThemeManager.getCurrentPalette().textPrimary());

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
        ThemePalette palette = ThemeManager.getCurrentPalette();

        JPanel corpo = new JPanel(new BorderLayout(0, 12));
        corpo.setOpaque(false);

        JLabel tituloDados = new JLabel("Métricas Globais do Sistema");
        tituloDados.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDados.setForeground(palette.textPrimary());
        tituloDados.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel cabecalhosDashboard = new JPanel(new GridLayout(1, 2, 30, 0));
        cabecalhosDashboard.setOpaque(false);
        cabecalhosDashboard.add(tituloDados);

        JPanel espacoCabecalhoGraficos = new JPanel();
        espacoCabecalhoGraficos.setOpaque(false);
        cabecalhosDashboard.add(espacoCabecalhoGraficos);

        JPanel cardsGrid = new JPanel(new GridLayout(3, 2, 16, 16));
        cardsGrid.setOpaque(false);
        
        cardsGrid.add(criarCartaoEstatistica("Quizzes Concluídos", String.valueOf(dados.getTotalPartidas()), "Total de partidas jogadas", palette.textPrimary(), palette));
        cardsGrid.add(criarCartaoEstatistica("Pontuação Média", dados.getPontuacaoMedia() + " pts", "Média por partida", palette.textPrimary(), palette));
        cardsGrid.add(criarCartaoEstatistica("Recorde do Quiz", dados.getMaiorPontuacao() + " pts", "Maior pontuação única", palette.textPrimary(), palette));
        cardsGrid.add(criarCartaoEstatistica("Aproveitamento Geral", dados.getAproveitamentoGeral() + "%", "Taxa global de acertos", palette.textPrimary(), palette));
        cardsGrid.add(criarCartaoEstatistica("Respostas Corretas", String.valueOf(dados.getTotalAcertos()), "Total acumulado", palette.textPrimary(), palette));
        cardsGrid.add(criarCartaoEstatistica("Respostas Incorretas", String.valueOf(dados.getTotalErros()), "Total acumulado", palette.dangerRed(), palette));

        JPanel colunaGraficos = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaGraficos.setOpaque(false);
        
        colunaGraficos.add(criarAreaGrafico(
                "Aproveitamento Geral de Respostas",
                "Percentual agregado de acertos vs erros de todos os estudantes",
                new DonutChartPanel(dados.getAproveitamentoGeral(), dados.getAproveitamentoGeral() + "%"), palette));
        
        colunaGraficos.add(criarAreaGrafico(
                "Volume Absoluto de Respostas",
                "Comparação direta da quantidade de questões respondidas",
                new BarChartPanel(
                        new String[] {"Acertos", "Erros"},
                        new int[] {dados.getTotalAcertos(), dados.getTotalErros()}), palette));

        JPanel colunaDados = new JPanel(new BorderLayout(0, 24));
        colunaDados.setOpaque(false);
        cardsGrid.setPreferredSize(new Dimension(0, 250));
        colunaDados.add(cardsGrid, BorderLayout.NORTH);
        colunaDados.add(criarRankingAlunos(ranking, palette), BorderLayout.CENTER);

        JPanel dashboard = new JPanel(new GridLayout(1, 2, 30, 0));
        dashboard.setOpaque(false);
        dashboard.add(colunaDados);
        dashboard.add(colunaGraficos);

        corpo.add(cabecalhosDashboard, BorderLayout.NORTH);
        corpo.add(dashboard, BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarCartaoEstatistica(String titulo, String valor, String detalhe, Color corValor, ThemePalette palette) {
        JPanel card = new PainelArredondado(14);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloLabel.setForeground(palette.textSecondary());

        JLabel valorLabel = new JLabel(valor);
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valorLabel.setForeground(corValor);

        JLabel detalheLabel = new JLabel(detalhe);
        detalheLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detalheLabel.setForeground(palette.textSecondary());

        card.add(tituloLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(valorLabel);
        card.add(Box.createVerticalStrut(1));
        card.add(detalheLabel);
        return card;
    }

    private JPanel criarRankingAlunos(List<RankingItem> ranking, ThemePalette palette) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BorderLayout(0, 8));

        JLabel titulo = new JLabel("Líderes de Pontuação (Global)", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(palette.textPrimary());
        card.add(titulo, BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));

        if (ranking.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma partida registrada até o momento.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(palette.textSecondary());
            lista.add(vazio);
        } else {
            // Cor de destaque inteligente para o 1º colocado (Dourado no Dark, Laranja no Light)
            Color corTop1 = ThemeManager.isDarkMode() ? new Color(255, 193, 7) : new Color(210, 120, 0);
            
            for (int i = 0; i < ranking.size(); i++) {
                RankingItem item = ranking.get(i);
                JPanel linha = new JPanel(new BorderLayout());
                linha.setOpaque(false);
                linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
                
                JLabel nome = new JLabel((i + 1) + ". " + abreviarNome(item.getNome()));
                nome.setToolTipText(item.getNome());
                nome.setFont(new Font("Segoe UI", i == 0 ? Font.BOLD : Font.PLAIN, 13));
                nome.setForeground(i == 0 ? corTop1 : palette.textPrimary());
                
                JLabel pontos = new JLabel(item.getPontuacao() + " pts");
                pontos.setFont(new Font("Segoe UI", Font.BOLD, 13));
                pontos.setForeground(i == 0 ? corTop1 : palette.textPrimary());
                
                linha.add(nome, BorderLayout.WEST);
                linha.add(pontos, BorderLayout.EAST);
                lista.add(linha);
                if (i < ranking.size() - 1) {
                    lista.add(Box.createVerticalStrut(3));
                }
            }
        }

        JScrollPane scroll = new JScrollPane(
                lista,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private String abreviarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return "Aluno";
        }
        String texto = nome.trim();
        return texto.length() <= 24 ? texto : texto.substring(0, 23) + "...";
    }

    private JPanel criarAreaGrafico(String titulo, String subtitulo, JPanel grafico, ThemePalette palette) {
        JPanel area = criarCartaoSuave();
        area.setLayout(new BorderLayout(0, 8));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloLabel.setForeground(palette.textPrimary());
        
        JLabel subtituloLabel = new JLabel(subtitulo);
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtituloLabel.setForeground(palette.textSecondary());

        header.add(tituloLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(subtituloLabel);

        area.add(header, BorderLayout.NORTH);
        area.add(grafico, BorderLayout.CENTER);
        return area;
    }

    private class PainelArredondado extends JPanel {
        private final int raio;

        PainelArredondado(int raio) {
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ThemePalette palette = ThemeManager.getCurrentPalette();
            
            g2.setColor(palette.cardBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            
            g2.setColor(palette.border());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.dispose();
        }
    }

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
            ThemePalette palette = ThemeManager.getCurrentPalette();

            int size = Math.min(130, Math.min(getHeight() - 16, getWidth() - 220));
            size = Math.max(80, size);
            int x = 20;
            int y = (getHeight() - size) / 2;

            g2.setColor(palette.chartTrack());
            g2.fillOval(x, y, size, size);
            
            g2.setColor(palette.neutralChart());
            g2.fillArc(x, y, size, size, 90, -Math.round(360f * percentagem / 100f));
            
            int tamanhoFuro = size - 44;
            int furoX = x + 22;
            int furoY = y + 22;
            g2.setColor(palette.cardBackground());
            g2.fillOval(furoX, furoY, tamanhoFuro, tamanhoFuro);

            g2.setColor(palette.textPrimary());
            ajustarFonteAoDonut(g2, textoCentral, tamanhoFuro - 12);
            int textoX = furoX + (tamanhoFuro - g2.getFontMetrics().stringWidth(textoCentral)) / 2;
            int textoY = furoY + (tamanhoFuro - g2.getFontMetrics().getHeight()) / 2
                    + g2.getFontMetrics().getAscent();
            g2.drawString(textoCentral, textoX, textoY);

            int legendaX = x + size + 40;
            int legendaY = y + (size / 2) - 15;
            
            desenharLegenda(g2, legendaX, legendaY, palette.neutralChart(), "Acertos: " + percentagem + "%");
            desenharLegenda(g2, legendaX, legendaY + 26, palette.dangerRed(), "Erros: " + (100 - percentagem) + "%");
            g2.dispose();
        }

        private void ajustarFonteAoDonut(Graphics2D g2, String texto, int larguraMaxima) {
            int tamanhoFonte = 22;
            do {
                g2.setFont(new Font("Segoe UI", Font.BOLD, tamanhoFonte));
                tamanhoFonte--;
            } while (tamanhoFonte >= 14 && g2.getFontMetrics().stringWidth(texto) > larguraMaxima);
        }

        private void desenharLegenda(Graphics2D g2, int x, int y, Color cor, String texto) {
            g2.setColor(cor);
            g2.fillRoundRect(x, y - 10, 14, 14, 6, 6);
            g2.setColor(ThemeManager.getCurrentPalette().textPrimary());
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.drawString(texto, x + 24, y + 2);
        }
    }

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
            ThemePalette palette = ThemeManager.getCurrentPalette();

            int max = Math.max(1, getMaximo());
            int topSpace = 25;
            int bottomSpace = 25;
            int chartHeight = Math.max(40, getHeight() - topSpace - bottomSpace);
            int barWidth = 52;
            int gap = 45;
            int totalWidth = valores.length * barWidth + (valores.length - 1) * gap;
            int startX = Math.max(24, (getWidth() - totalWidth) / 2);
            int baseY = getHeight() - bottomSpace;

            for (int i = 0; i < valores.length; i++) {
                int height = Math.max(8, Math.round(chartHeight * (valores[i] / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;
                
                Color corBarra = (i == 0) ? palette.neutralChart() : palette.dangerRed();

                g2.setColor(corBarra);
                g2.fillRoundRect(x, y, barWidth, height, 10, 10);
                g2.fillRect(x, baseY - 5, barWidth, 5); 

                g2.setColor(palette.textPrimary());
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valorStr = valores[i] + " qst";
                g2.drawString(valorStr, x + barWidth / 2 - g2.getFontMetrics().stringWidth(valorStr) / 2, y - 6);

                g2.setColor(palette.textSecondary());
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