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
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.theme.ThemeManager;
import frontend.util.AppTheme;

public class AjudaModal extends JDialog {

    private final GameplayTela telaJogo;
    private JButton btnDica;
    private JLabel dicaFalseLabel;
    private int tirarMetadeTentativas = 2;
    private int mostrarDicaTentativas = 2;

    private Component glassPaneOriginal;
    private ComponentAdapter rastreadorMovimento;

    public AjudaModal(GameplayTela pai, boolean dicaExiste) {
        super(pai, true);
        this.telaJogo = pai;
        setUndecorated(true);

        initComponents();
        ThemeManager.applyTheme(this);

        pack();
        setLocationRelativeTo(pai);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            glassPaneOriginal = telaJogo.getGlassPane();
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
            telaJogo.setGlassPane(mascaraEscura);
            mascaraEscura.setVisible(true);

            rastreadorMovimento = new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) { setLocationRelativeTo(telaJogo); }
                @Override
                public void componentResized(ComponentEvent e) { setLocationRelativeTo(telaJogo); }
            };
            telaJogo.addComponentListener(rastreadorMovimento);
        }
        super.setVisible(b);
        if (b) {
            if (rastreadorMovimento != null) telaJogo.removeComponentListener(rastreadorMovimento);
            if (glassPaneOriginal != null) {
                telaJogo.getGlassPane().setVisible(false);
                telaJogo.setGlassPane(glassPaneOriginal);
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

        JLabel titulo = new JLabel("Ajuda do Jogo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel sub = new JLabel("Escolha um benefício:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(AppTheme.TEXT_MUTED);

        JButton btn50 = criarBotaoModal("Tirar duas alternativas", new Color(100, 30, 255));
        btn50.addActionListener(e -> {
            if (tirarMetadeTentativas > 0) {
                telaJogo.tirarDuasAlternativas();
                tirarMetadeTentativas--;
            }
            if (tirarMetadeTentativas <= 0) {
                btn50.setEnabled(false);
            }
            dispose();
        });

        btnDica = criarBotaoModal("Mostrar Dica", AppTheme.STUDENT_HIGHLIGHT);
        btnDica.addActionListener(e -> {
            if (mostrarDicaTentativas > 0) {
                telaJogo.mostrarDica();
                mostrarDicaTentativas--;
            }
            if (mostrarDicaTentativas <= 0) {
                btnDica.setEnabled(false);
            }
            dispose();
        });

        dicaFalseLabel = new JLabel("Dica não disponível para esta questão.");
        dicaFalseLabel.setForeground(Color.RED);
        dicaFalseLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dicaFalseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicaFalseLabel.setVisible(false);

        JButton btnFechar = new JButton("Cancelar");
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFechar.addActionListener(e -> dispose());

        painel.add(titulo);
        painel.add(sub);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btn50);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnDica);
        painel.add(Box.createVerticalStrut(15));
        painel.add(dicaFalseLabel);   
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnFechar);
        painel.add(Box.createVerticalStrut(15));

        add(painel);
    }

    public void atualizarVisibilidadeDica(boolean existe) {
        if (btnDica != null && dicaFalseLabel != null) {
            btnDica.setVisible(existe);
            dicaFalseLabel.setVisible(!existe);
            
            if (existe && mostrarDicaTentativas <= 0) {
                btnDica.setEnabled(false);
            } else if (existe) {
                btnDica.setEnabled(true);
            }
            this.pack();
        }
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