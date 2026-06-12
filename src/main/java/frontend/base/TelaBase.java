package frontend.base;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import frontend.theme.ThemeAware;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.theme.ThemeToggleButton;
import frontend.util.AppTheme;

public class TelaBase extends JFrame {

    private ThemeToggleButton themeToggleButton;

    protected static final Color COR_FUNDO_ESCURO = AppTheme.BACKGROUND;
    protected static final Color COR_FUNDO_CLARO = AppTheme.BACKGROUND;
    protected static final Color COR_AZUL = AppTheme.SECONDARY_DARK;
    protected static final Color COR_AZUL_ESCURO = AppTheme.PRIMARY_DARK;
    protected static final Color COR_VERDE = new Color(90, 90, 90);
    protected static final Color COR_BRANCO = AppTheme.SURFACE;
    protected static final Color COR_CARTAO = AppTheme.SURFACE;
    protected static final Color COR_BORDA = AppTheme.BORDER;
    protected static final Color COR_TEXTO = AppTheme.TEXT;
    protected static final Color COR_TEXTO_SUAVE = AppTheme.TEXT_MUTED;
    protected static final Color COR_PRETO = AppTheme.PRIMARY_DARK;
    protected static final Color COR_CINZA = new Color(133, 145, 157);
    protected static final Color COR_CINZA_CLARO = AppTheme.SOFT_GRAY;
    protected static final Color COR_VERMELHO_ETEC = AppTheme.RED;
    protected static final Color COR_FUNDO_CLARO_C = AppTheme.BACKGROUND;
    protected static final Color COR_TEXTO_TITULO = AppTheme.RED;

    public TelaBase(String titulo) {
        super(titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1440, 810);
        getContentPane().setBackground(COR_FUNDO_CLARO);
        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            instalarBotaoTema();
            ThemeManager.applyTheme(this);
        }
        super.setVisible(visible);
        if (visible) {
            SwingUtilities.invokeLater(this::posicionarBotaoTema);
        }
    }

    private void instalarBotaoTema() {
        if (themeToggleButton != null) {
            return;
        }

        themeToggleButton = new ThemeToggleButton();
        getLayeredPane().add(themeToggleButton, javax.swing.JLayeredPane.DRAG_LAYER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                posicionarBotaoTema();
            }
        });
        posicionarBotaoTema();
    }

    private void posicionarBotaoTema() {
        if (themeToggleButton == null) {
            return;
        }
        themeToggleButton.setBounds(20, 16, 126, 34);
        themeToggleButton.revalidate();
        themeToggleButton.repaint();
    }

    protected JPanel criarPainelPrincipal() {
        GradientPanel painel = new GradientPanel();
        painel.setLayout(new BorderLayout());
        painel.setBorder(new EmptyBorder(18, 24, 18, 24));
        return painel;
    }

    protected JPanel criarCanvasCentral() {
        RoundedPanel painel = new RoundedPanel(46, COR_BRANCO, new Color(0, 0, 0, 28), 12, 14);
        painel.setLayout(new BorderLayout());
        painel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(new Color(255, 255, 255, 150), 1, 44, 0),
            new EmptyBorder(34, 36, 34, 36)
        ));
        return painel;
    }

    protected JPanel criarColunaCentral(int largura) {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setPreferredSize(new Dimension(largura, 0));
        return painel;
    }

    protected JLabel criarTituloHero(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 56));
        label.setForeground(COR_PRETO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected JLabel criarSubtituloHero(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setForeground(new Color(78, 78, 78));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected JLabel criarTituloSecao(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setForeground(COR_TEXTO);
        return label;
    }

    protected JLabel criarTituloBloco(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(COR_TEXTO);
        return label;
    }

    protected JLabel criarTexto(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(COR_TEXTO);
        return label;
    }

    protected JLabel criarTextoSuave(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(COR_TEXTO_SUAVE);
        return label;
    }

    protected JLabel criarTextoCentral(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(COR_TEXTO);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected JLabel criarBadge(String texto, Color fundo, Color corTexto) {
        RoundedPanel painel = new RoundedPanel(24, fundo, new Color(0, 0, 0, 0), 0, 0);
        painel.setLayout(new BorderLayout());
        painel.setBorder(new EmptyBorder(10, 18, 10, 18));
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(corTexto);
        painel.add(label, BorderLayout.CENTER);
        return wrapPanelAsLabel(painel);
    }

    protected JTextField criarCampoTexto(String texto) {
        JTextField campo = new JTextField(texto);
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        campo.setForeground(COR_CINZA);
        campo.setBackground(COR_CARTAO);
        campo.setBorder(new RoundedLineBorder(COR_BORDA, 2, 30, 18));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        campo.setPreferredSize(new Dimension(0, 68));
        return campo;
    }

    protected JButton criarBotaoPrincipal(String texto) {
        RoundedButton botao = new RoundedButton(texto, COR_PRETO, COR_BRANCO, 34, 20);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        botao.setPreferredSize(new Dimension(420, 70));
        return botao;
    }

    protected JButton criarBotaoSecundario(String texto) {
        RoundedButton botao = new RoundedButton(texto, AppTheme.SECONDARY_DARK, COR_BRANCO, 30, 18);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        botao.setPreferredSize(new Dimension(0, 82));
        return botao;
    }

    protected JButton criarBotaoNeutro(String texto) {
        RoundedButton botao = new RoundedButton(texto, new Color(252, 252, 252), COR_PRETO, 24, 14);
        botao.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(COR_PRETO, 1, 24, 12),
            new EmptyBorder(0, 0, 0, 0)
        ));
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        botao.setPreferredSize(new Dimension(0, 58));
        return botao;
    }

    protected JButton criarBotaoLink(String texto) {
        JButton botao = new JButton("<html><u>" + texto + "</u></html>");
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.PLAIN, 16));
        botao.setForeground(isTextoSaida(texto) ? COR_VERMELHO_ETEC : COR_PRETO);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    protected JPanel criarCartaoSuave() {
        RoundedPanel painel = new RoundedPanel(28, COR_CARTAO, new Color(0, 0, 0, 12), 6, 8, true);
        painel.setLayout(new BorderLayout());
        painel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(new Color(224, 224, 224), 1, 28, 0),
            new EmptyBorder(20, 22, 20, 22)
        ));
        return painel;
    }

    protected JPanel criarLinhaDestaque() {
        RoundedPanel linha = new RoundedPanel(4, COR_VERMELHO_ETEC, new Color(0, 0, 0, 0), 0, 0);
        linha.setPreferredSize(new Dimension(82, 4));
        linha.setMaximumSize(new Dimension(82, 4));
        linha.setAlignmentX(Component.CENTER_ALIGNMENT);
        return linha;
    }

    private boolean isTextoSaida(String texto) {
        String normalizado = texto.toLowerCase();
        return normalizado.contains("sair") || normalizado.contains("encerrar");
    }

    protected JPanel criarLinhaBotoes(JButton... botoes) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        painel.setOpaque(false);
        for (JButton botao : botoes) {
            painel.add(botao);
        }
        return painel;
    }

    protected JPanel criarGrid(int linhas, int colunas, int gap) {
        JPanel painel = new JPanel(new GridLayout(linhas, colunas, gap, gap));
        painel.setOpaque(false);
        return painel;
    }

    protected JLabel criarAvatarPlaceholder(String texto) {
        return new AvatarLabel(texto);
    }

    protected JLabel criarIconeLaboratorio() {
        RoundedPanel painel = new RoundedPanel(28, new Color(248, 248, 248), new Color(0, 0, 0, 10), 4, 6);
        painel.setLayout(new BorderLayout());
        painel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(COR_PRETO, 1, 28, 0),
            new EmptyBorder(12, 14, 12, 14)
        ));
        JLabel label = new JLabel("\u2697", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(COR_PRETO);
        painel.add(label, BorderLayout.CENTER);
        return wrapPanelAsLabel(painel);
    }

    protected JLabel criarPlaceholder(String texto) {
        RoundedPanel painel = new RoundedPanel(28, new Color(246, 246, 246), new Color(0, 0, 0, 10), 4, 6);
        painel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(new Color(226, 226, 226), 1, 28, 0),
            new EmptyBorder(18, 18, 18, 18)
        ));
        painel.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + texto + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setForeground(COR_TEXTO_SUAVE);
        painel.add(label, BorderLayout.CENTER);
        return wrapPanelAsLabel(painel);
    }

    protected JScrollPane criarScroll(JComponent componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_BRANCO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    protected JPanel criarTopoComVoltar(String titulo, JButton voltarButton) {
        JPanel painel = new JPanel(new BorderLayout(16, 0));
        painel.setOpaque(false);

        JPanel tituloPanel = new JPanel();
        tituloPanel.setOpaque(false);
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));

        JLabel tituloLabel = criarTituloSecao(titulo);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloPanel.add(tituloLabel);
        tituloPanel.add(Box.createVerticalStrut(8));
        tituloPanel.add(criarLinhaDestaque());

        painel.add(tituloPanel, BorderLayout.CENTER);
        painel.add(voltarButton, BorderLayout.EAST);
        return painel;
    }

    protected JPanel criarCabecalhoMarca(String titulo, String subtitulo) {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        JLabel tituloLabel = criarTituloHero(titulo);
        JLabel subtituloLabel = criarSubtituloHero(subtitulo);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(tituloLabel);
        painel.add(Box.createVerticalStrut(6));
        painel.add(subtituloLabel);
        return painel;
    }

    private JLabel wrapPanelAsLabel(JPanel painel) {
        JLabel holder = new JLabel();
        holder.setLayout(new BorderLayout());
        holder.add(painel, BorderLayout.CENTER);
        return holder;
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ThemeManager.getCurrentPalette().background());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    protected static class RoundedPanel extends JPanel implements ThemeAware {
        private final int raio;
        private final Color fundo;
        private final Color sombra;
        private final int offsetX;
        private final int offsetY;
        private final boolean destaque;

        RoundedPanel(int raio, Color fundo, Color sombra, int offsetX, int offsetY) {
            this(raio, fundo, sombra, offsetX, offsetY, false);
        }

        RoundedPanel(int raio, Color fundo, Color sombra, int offsetX, int offsetY, boolean destaque) {
            this.raio = raio;
            this.fundo = fundo;
            this.sombra = sombra;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.destaque = destaque;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (sombra.getAlpha() > 0) {
                g2.setColor(ThemeManager.getCurrentPalette().shadow());
                g2.fillRoundRect(offsetX, offsetY, getWidth() - offsetX - 4, getHeight() - offsetY - 2, raio, raio);
            }
            Color fundoAtual = destaque
                    ? ThemeManager.getCurrentPalette().cardBackground()
                    : ThemeManager.resolveBackground(fundo);
            g2.setColor(fundoAtual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), raio, raio);
            if (destaque) {
                Color destaqueAtual = ThemeManager.getCurrentPalette().primaryRed();
                g2.setColor(new Color(destaqueAtual.getRed(), destaqueAtual.getGreen(), destaqueAtual.getBlue(), 190));
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int margemDestaque = Math.max(8, raio / 2);
                int fimDestaque = Math.max(margemDestaque, getWidth() - margemDestaque - 1);
                g2.drawLine(margemDestaque, 2, fimDestaque, 2);
            }
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void applyTheme(ThemePalette palette) {
            repaint();
        }
    }

    protected static class RoundedButton extends JButton implements ThemeAware {
        private final Color fundo;
        // private final Color texto;
        private final int raio;
        // private final int tamanhoFonte;

        RoundedButton(String textoBotao, Color fundo, Color texto, int raio, int tamanhoFonte) {
            super(textoBotao);
            this.fundo = fundo;
            // this.texto = texto;
            this.raio = raio;
            // this.tamanhoFonte = tamanhoFonte;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(new Font("SansSerif", Font.BOLD, tamanhoFonte));
            setForeground(texto);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean hover = getModel().isRollover();
            ThemePalette palette = ThemeManager.getCurrentPalette();
            boolean botaoNeutro = !isDark(fundo);
            Color corBase = botaoNeutro ? palette.neutralButtonBackground() : palette.buttonBackground();
            Color corAtual = getModel().isArmed() ? corBase.darker() : corBase;
            if (hover && botaoNeutro) {
                corAtual = palette.softRed();
            } else if (hover) {
                corAtual = palette.primaryRedHover();
            }
            g2.setColor(palette.shadow());
            g2.fillRoundRect(0, 4, getWidth(), getHeight() - 4, raio, raio);
            g2.setColor(corAtual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight() - 4, raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }

        private boolean isDark(Color cor) {
            return (cor.getRed() + cor.getGreen() + cor.getBlue()) / 3 < 130;
        }

        @Override
        public void applyTheme(ThemePalette palette) {
            setForeground(isDark(fundo) ? palette.buttonText() : palette.textPrimary());
            repaint();
        }
    }

    protected static class RoundedLineBorder extends EmptyBorder {
        private final Color cor;
        private final int espessura;
        private final int raio;

        public RoundedLineBorder(Color cor, int espessura, int raio, int padding) {
            super(padding, padding, padding, padding);
            this.cor = cor;
            this.espessura = espessura;
            this.raio = raio;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ThemeManager.resolveForeground(cor));
            g2.setStroke(new BasicStroke(espessura));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, raio, raio);
            g2.dispose();
        }
    }

    private static class AvatarLabel extends JLabel {
        AvatarLabel(String texto) {
            super("", SwingConstants.CENTER);
            setText("");
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setForeground(COR_TEXTO);
            setFont(new Font("SansSerif", Font.BOLD, 18));
            setPreferredSize(new Dimension(210, 210));
            setMinimumSize(new Dimension(210, 210));
            setMaximumSize(new Dimension(210, 210));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ThemePalette palette = ThemeManager.getCurrentPalette();
            int diametro = Math.min(getWidth(), getHeight()) - 36;
            int x = (getWidth() - diametro) / 2;
            int y = (getHeight() - diametro) / 2;

            g2.setColor(palette.avatarBackground());
            g2.fillOval(x, y, diametro, diametro);
            g2.setColor(palette.primaryRed());
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(x, y, diametro, diametro);

            g2.setColor(palette.avatarShape());
            int cabeca = Math.round(diametro * 0.30f);
            int cabecaX = x + (diametro - cabeca) / 2;
            int cabecaY = y + Math.round(diametro * 0.22f);
            g2.fillOval(cabecaX, cabecaY, cabeca, cabeca);

            int corpoLargura = Math.round(diametro * 0.62f);
            int corpoAltura = Math.round(diametro * 0.30f);
            int corpoX = x + (diametro - corpoLargura) / 2;
            int corpoY = y + Math.round(diametro * 0.58f);
            g2.fillRoundRect(corpoX, corpoY, corpoLargura, corpoAltura, corpoAltura, corpoAltura);

            g2.dispose();
        }
    }
}
