package frontend;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import frontend.theme.ThemeManager;
import frontend.util.AppTheme;

public class SairModal extends JDialog {
    private boolean confirmarSair = false;

    public SairModal(Frame parent) {
        super(parent, "Sair", true);
        setUndecorated(true); // Tira as bordas padrão de janela
        initComponents();
        ThemeManager.applyTheme(this);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        // Painel Principal com borda arredondada (simulada por margem)
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(AppTheme.SURFACE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel titulo = new JLabel("Deseja sair?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel mensagem = new JLabel("Você retornará à tela de login.");
        mensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        mensagem.setForeground(AppTheme.TEXT_MUTED);

        // Botão de Confirmar (Estilo Vermelho)
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

        // Botão de Cancelar (Link/Neutro)
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

        getContentPane().add(painel);
    }

    public boolean isConfirmarSair() {
        return confirmarSair;
    }
}
