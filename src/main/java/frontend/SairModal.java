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
import javax.swing.border.EmptyBorder;

import frontend.theme.ThemeManager;
import frontend.util.AppTheme;

public class SairModal extends JDialog {
    private boolean confirmarSair = false;
    private final JFrame pai;
    private Component glassPaneOriginal;
    private ComponentAdapter rastreadorMovimento;

    public SairModal(JFrame parent) {
        super(parent, "Sair", true);
        this.pai = parent;
        setUndecorated(true); 
        // Bug do Wayland corrigido: removido o setBackground transparente

        initComponents();
        ThemeManager.applyTheme(this);
        pack();
        setLocationRelativeTo(parent);
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
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(AppTheme.SURFACE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 2, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel titulo = new JLabel("Deseja sair?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel mensagem = new JLabel("Você voltará para a tela inicial.");
        mensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        mensagem.setForeground(AppTheme.TEXT_MUTED);

        JButton btnSair = new JButton("SAIR");
        btnSair.setBackground(AppTheme.ERROR_HIGHLIGHT);
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSair.setMaximumSize(new Dimension(250, 50));
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> {
            confirmarSair = true;
            dispose();
        });

        JButton btnVoltar = new JButton("Continuar jogando");
        btnVoltar.setContentAreaFilled(false);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setForeground(AppTheme.TEXT_MUTED);
        btnVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> dispose());

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));
        painel.add(mensagem);
        painel.add(Box.createVerticalStrut(30));
        painel.add(btnSair);
        painel.add(Box.createVerticalStrut(10));
        painel.add(btnVoltar);

        add(painel);
    }

    public boolean isConfirmarSair() {
        return confirmarSair;
    }
}