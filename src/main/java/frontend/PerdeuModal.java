package frontend;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.theme.ThemeManager;
import frontend.util.AppTheme;

public class PerdeuModal extends JDialog {
    private final JFrame pai;
    private Component glassPaneOriginal;
    private ComponentAdapter rastreadorMovimento;

    public PerdeuModal(JFrame pai) {
        super(pai, true);
        this.pai = pai;
        setUndecorated(true);

        initComponents();
        ThemeManager.applyTheme(this);
        pack();
        setLocationRelativeTo(pai);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            glassPaneOriginal = pai.getGlassPane();
            JPanel mascaraEscura = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(0, 0, 0, 160));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            mascaraEscura.setOpaque(false);
            mascaraEscura.addMouseListener(new MouseAdapter() {});
            pai.setGlassPane(mascaraEscura);
            mascaraEscura.setVisible(true);

            rastreadorMovimento = new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) { setLocationRelativeTo(pai); }
                @Override
                public void componentResized(ComponentEvent e) { setLocationRelativeTo(pai); }
            };
            pai.addComponentListener(rastreadorMovimento);
        }
        super.setVisible(b);
        if (b) {
            if (rastreadorMovimento != null) pai.removeComponentListener(rastreadorMovimento);
            if (glassPaneOriginal != null) {
                pai.getGlassPane().setVisible(false);
                pai.setGlassPane(glassPaneOriginal);
            }
        }
    }

    private void initComponents() {
        JPanel painel = new JPanel();
        painel.setOpaque(true);
        painel.setBackground(AppTheme.SURFACE);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 2, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        JLabel titulo = new JLabel("Você perdeu!");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel sub = new JLabel("Boa sorte na próxima!");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(AppTheme.TEXT_MUTED);

        JButton btnOK = criarBotaoModal("OK", AppTheme.ERROR_HIGHLIGHT);
        btnOK.addActionListener(e -> dispose());

        painel.add(titulo);
        painel.add(sub);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnOK);

        add(painel);
    }

    private JButton criarBotaoModal(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}