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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backend.DAO.partidaDAO.PartidaDAO;
import backend.DAO.partidaDAO.PartidaDAO.DadosAcumuladosAluno;
import backend.DAO.usuarioDAO.Usuario;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class EstatisticasTela extends TelaBase {

    private final String tipoUsuario;
    private final PartidaDAO partidaDAO;

    private static final Color COR_FUNDO_CARD = AppTheme.SURFACE;
    private static final Color COR_BORDA_CARD = AppTheme.BORDER;
    private static final Color COR_TEXTO_PRINCIPAL = AppTheme.TEXT;
    private static final Color COR_TEXTO_MUTED = AppTheme.TEXT_MUTED;
    private static final Color COR_DESTAQUE_ALUNO = AppTheme.STUDENT_HIGHLIGHT;
    private static final Color COR_ERROS = AppTheme.ERROR_HIGHLIGHT;

    public EstatisticasTela(String tipoUsuario) {
        super("QuimLab - Estatísticas do Aluno");
        this.tipoUsuario = tipoUsuario;
        this.partidaDAO = new PartidaDAO();
        initComponents();
    }

    private void initComponents() {
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuario();
        int idUsuario = (usuarioLogado != null) ? usuarioLogado.getId() : 1; 
        String nomeAluno = (usuarioLogado != null && usuarioLogado.getNome() != null) ? usuarioLogado.getNome() : "Aluno";

        DadosAcumuladosAluno dadosReais = partidaDAO.buscarEstatisticasDoAluno(idUsuario);

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 20));
        conteudo.setOpaque(false);
        
        conteudo.add(criarTopo(), BorderLayout.NORTH);
        conteudo.add(criarCorpo(nomeAluno, dadosReais), BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JLabel titulo = new JLabel("Minhas Estatísticas", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
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

    private JPanel criarResumoAluno(String nomeAluno, DadosAcumuladosAluno dados) {
        JPanel resumo = new PainelArredondado(16, COR_FUNDO_CARD, COR_BORDA_CARD);
        resumo.setLayout(new BoxLayout(resumo, BoxLayout.Y_AXIS));
        resumo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        resumo.setPreferredSize(new Dimension(0, 88));
        resumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        resumo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel nome = new JLabel(nomeAluno);
        nome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nome.setForeground(COR_DESTAQUE_ALUNO);
        nome.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel detalhe = new JLabel("Desempenho Geral | " + dados.getPartidasJogadas() + " quizzes finalizados");
        detalhe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detalhe.setForeground(COR_TEXTO_MUTED);
        detalhe.setAlignmentX(LEFT_ALIGNMENT);

        resumo.add(nome);
        resumo.add(Box.createVerticalStrut(4));
        resumo.add(detalhe);
        return resumo;
    }

    private JPanel criarCorpo(String nomeAluno, DadosAcumuladosAluno dados) {
        JPanel corpo = new JPanel(new BorderLayout(0, 20));
        corpo.setOpaque(false);
        corpo.add(criarResumoAluno(nomeAluno, dados), BorderLayout.NORTH);

        JLabel tituloDados = new JLabel("Histórico de Progresso");
        tituloDados.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDados.setForeground(COR_TEXTO_PRINCIPAL);
        tituloDados.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel cabecalhosDashboard = new JPanel(new GridLayout(1, 2, 30, 0));
        cabecalhosDashboard.setOpaque(false);
        cabecalhosDashboard.add(tituloDados);

        JPanel espacoCabecalhoGraficos = new JPanel();
        espacoCabecalhoGraficos.setOpaque(false);
        cabecalhosDashboard.add(espacoCabecalhoGraficos);

        JPanel cardsGrid = new JPanel(new GridLayout(3, 2, 16, 16));
        cardsGrid.setOpaque(false);
        
        cardsGrid.add(criarCartaoEstatistica("Pontuação Total", dados.getPontuacaoTotal() + " pts", "Acumulado geral", COR_DESTAQUE_ALUNO));
        cardsGrid.add(criarCartaoEstatistica("Questões Respondidas", String.valueOf(dados.getQuestoesRespondidas()), "Total de perguntas", COR_DESTAQUE_ALUNO));
        cardsGrid.add(criarCartaoEstatistica("Total de Acertos", String.valueOf(dados.getAcertos()), "Respostas corretas", COR_DESTAQUE_ALUNO));
        cardsGrid.add(criarCartaoEstatistica("Total de Erros", String.valueOf(dados.getErros()), "Respostas incorretas", COR_ERROS));
        cardsGrid.add(criarCartaoEstatistica("Aproveitamento", dados.getAproveitamento() + "%", "Taxa média de acerto", COR_DESTAQUE_ALUNO));
        cardsGrid.add(criarCartaoEstatistica("Quizzes Concluídos", String.valueOf(dados.getPartidasJogadas()), "Partidas jogadas", COR_DESTAQUE_ALUNO));

        JPanel colunaGraficos = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaGraficos.setOpaque(false);
        
        colunaGraficos.add(criarAreaGrafico(
                "Aproveitamento Geral",
                "Percentagem de acertos sobre o total de questões",
                new DonutChartPanel(dados.getAproveitamento(), dados.getAproveitamento() + "%")));
        
        colunaGraficos.add(criarAreaGrafico(
                "Volume de Respostas",
                "Comparação entre respostas certas e erradas",
                new BarChartPanel(
                        new String[] {"Acertos", "Erros"},
                        new int[] {dados.getAcertos(), dados.getErros()})));

        JPanel dashboard = new JPanel(new GridLayout(1, 2, 30, 0));
        dashboard.setOpaque(false);
        dashboard.add(cardsGrid);
        dashboard.add(colunaGraficos);

        JPanel secoes = new JPanel(new BorderLayout(0, 12));
        secoes.setOpaque(false);
        secoes.add(cabecalhosDashboard, BorderLayout.NORTH);
        secoes.add(dashboard, BorderLayout.CENTER);

        corpo.add(secoes, BorderLayout.CENTER);
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
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
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

    private JPanel criarAreaGrafico(String titulo, String subtitulo, JPanel grafico) {
        JPanel area = criarCartaoSuave();
        area.setLayout(new BorderLayout(0, 8));

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

    // Painel Customizado para Bordas Arredondadas Polidas
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
            
            Color fundoAtual = corFundo.equals(AppTheme.SURFACE)
                    ? ThemeManager.getCurrentPalette().cardBackground()
                    : ThemeManager.resolveBackground(corFundo);
            g2.setColor(fundoAtual);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            
            g2.setColor(ThemeManager.resolveForeground(corBorda));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.dispose();
        }
    }

    // Gráfico Circular (Donut) do Aluno
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

            int size = Math.min(130, Math.min(getHeight() - 16, getWidth() - 200));
            size = Math.max(80, size);
            int x = 20;
            int y = (getHeight() - size) / 2;

            g2.setColor(palette.chartTrack());
            g2.fillOval(x, y, size, size);
            
            g2.setColor(palette.primaryRed());
            g2.fillArc(x, y, size, size, 90, -Math.round(360f * percentagem / 100f));
            
            g2.setColor(palette.cardBackground());
            int tamanhoFuro = size - 44;
            int furoX = x + 22;
            int furoY = y + 22;
            g2.fillOval(furoX, furoY, tamanhoFuro, tamanhoFuro);

            g2.setColor(palette.textPrimary());
            ajustarFonteAoDonut(g2, textoCentral, tamanhoFuro - 12);
            int textoX = furoX + (tamanhoFuro - g2.getFontMetrics().stringWidth(textoCentral)) / 2;
            int textoY = furoY + (tamanhoFuro - g2.getFontMetrics().getHeight()) / 2
                    + g2.getFontMetrics().getAscent();
            g2.drawString(textoCentral, textoX, textoY);

            int legendaX = x + size + 40;
            int legendaY = y + (size / 2) - 15;
            
            desenharLegenda(g2, legendaX, legendaY, palette.primaryRed(), "Acertos: " + percentagem + "%");
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

    // Gráfico de Barras Minimalista (Acertos vs Erros)
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
            int barWidth = 50;
            int gap = 45;
            int totalWidth = valores.length * barWidth + (valores.length - 1) * gap;
            int startX = Math.max(24, (getWidth() - totalWidth) / 2);
            int baseY = getHeight() - bottomSpace;

            for (int i = 0; i < valores.length; i++) {
                int height = Math.max(8, Math.round(chartHeight * (valores[i] / (float) max)));
                int x = startX + i * (barWidth + gap);
                int y = baseY - height;
                
                Color corBarra = (i == 0) ? palette.primaryRed() : palette.dangerRed();

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
