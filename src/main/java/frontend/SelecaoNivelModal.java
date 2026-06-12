package frontend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.theme.ThemeAware;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class SelecaoNivelModal extends JDialog {
    private final String nivelSelecionado = null;
    private final JFrame pai;
    private final String tipoUsuario;

    public SelecaoNivelModal(JFrame pai, String tipoUsuario) {
        super(pai, true);
        this.pai = pai;
        this.tipoUsuario = tipoUsuario;
        
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        initComponents();
        ThemeManager.applyTheme(this);
        
        pack();
        setLocationRelativeTo(pai);
    }

    private void initComponents() {
        JPanel painel = new PainelModal();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(34, 44, 42, 54));

        JLabel titulo = new JLabel("Dificuldade");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel sub = new JLabel("Escolha o nível do desafio:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setForeground(AppTheme.TEXT_MUTED);

        JButton btnFacil = criarBotaoModal("FÁCIL", AppTheme.DIFFICULTY_EASY, AppTheme.STUDENT_HIGHLIGHT);
        JButton btnMedio = criarBotaoModal("MÉDIO", AppTheme.DIFFICULTY_MEDIUM, AppTheme.TEXT);
        JButton btnDificil = criarBotaoModal("DIFÍCIL", AppTheme.DIFFICULTY_HARD, Color.WHITE);
        JButton btnProgressivo = criarBotaoModal("PROGRESSIVO", AppTheme.DIFFICULTY_PROGRESSIVE, Color.WHITE);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(CENTER_ALIGNMENT);
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(AppTheme.TEXT_MUTED);

        btnFacil.addActionListener(e -> iniciarJogo("FACIL",btnFacil));
        btnMedio.addActionListener(e -> iniciarJogo("MEDIO",btnMedio));
        btnDificil.addActionListener(e -> iniciarJogo("DIFICIL",btnDificil));
        btnProgressivo.addActionListener(e -> iniciarJogo("PROGRESSIVO",btnProgressivo));
        btnCancelar.addActionListener(e -> dispose());

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(6));
        painel.add(sub);
        painel.add(Box.createVerticalStrut(28));
        painel.add(btnFacil);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnMedio);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnDificil);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnProgressivo);
        painel.add(Box.createVerticalStrut(18));
        painel.add(btnCancelar);

        add(painel);
    }

    private void iniciarJogo(String nivel, JButton btnClicado) {
        String textoOriginal = btnClicado.getText();
        btnClicado.setText("CARREGANDO...");
        btnClicado.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new Thread(() -> {
            try {
                GameplayTela tela = new GameplayTela(tipoUsuario, nivel);
                
                java.awt.EventQueue.invokeLater(()->{
                    this.dispose();
                    Navegador.abrirTela(pai, tela);
                    setCursor(Cursor.getDefaultCursor());
                });     

            } catch (Exception ex) {
                java.awt.EventQueue.invokeLater(()->{
                    btnClicado.setText(textoOriginal);
                    btnClicado.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro ao carregar partida!" + ex.getMessage());
                });
            }
        }).start();
    }

    private JButton criarBotaoModal(String texto, Color corFundo, Color corTexto) {
        JButton btn = new BotaoNivel(texto, corFundo, corTexto);
        btn.setMaximumSize(new Dimension(300, 52));
        btn.setPreferredSize(new Dimension(300, 52));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        return btn;
    }

    private static class PainelModal extends JPanel {
        PainelModal() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int largura = getWidth() - 10;
            int altura = getHeight() - 12;
            ThemePalette palette = ThemeManager.getCurrentPalette();
            g2.setColor(palette.surface());
            g2.fillRoundRect(0, 0, largura, altura, 32, 32);
            g2.setColor(palette.border());
            g2.drawRoundRect(0, 0, largura - 1, altura - 1, 32, 32);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class BotaoNivel extends JButton implements ThemeAware {
        private final Color corFundo;
        private final Color corTexto;

        BotaoNivel(String texto, Color corFundo, Color corTexto) {
            super(texto);
            this.corFundo = corFundo;
            this.corTexto = corTexto;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color corBase = ThemeManager.resolveBackground(corFundo);
            Color corAtual = getModel().isRollover()
                    ? (ThemeManager.isDarkMode() ? corBase.brighter() : corBase.darker())
                    : corBase;
            if (getModel().isArmed()) {
                corAtual = corAtual.darker();
            }

            g2.setColor(corAtual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();

            super.paintComponent(g);
        }

        @Override
        public void applyTheme(ThemePalette palette) {
            setForeground(ThemeManager.isDarkMode() && Color.WHITE.equals(corTexto)
                    ? palette.buttonText()
                    : ThemeManager.resolveForeground(corTexto));
            repaint();
        }
    }

    public String getNivel() {
        return nivelSelecionado;
    }
}
