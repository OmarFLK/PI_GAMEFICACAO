package frontend;

import java.awt.*;
import javax.swing.*;
import frontend.base.TelaBase;

public class SelecaoNivelModal extends JDialog {
    private String nivelSelecionado = null;

    public SelecaoNivelModal(JFrame pai) {
        super(pai, true); // true para ser modal (travar a tela de trás)
        setUndecorated(true); // Remove a barra de título feia do Windows/Linux
        setBackground(new Color(0, 0, 0, 0)); // Fundo transparente para o arredondamento funcionar

        initComponents();
        
        pack();
        setLocationRelativeTo(pai); // Centraliza no meio do programa
    }

    private void initComponents() {
        // Usamos o método criarCartaoSuave da TelaBase (precisamos instanciar ou simular aqui)
        // Como o JDialog não estende TelaBase, vamos replicar o estilo visual:
        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 37, 41), 2, true), // Borda Azul Escuro
            BorderFactory.createEmptyBorder(30, 40, 30, 40) // Respiro interno
        ));

        JLabel titulo = new JLabel("Dificuldade");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(new Color(33, 37, 41));

        JLabel sub = new JLabel("Escolha o nível do desafio:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setForeground(Color.GRAY);

        // Botões de Nível (Estilizados como os seus botões principais)
        JButton btnFacil = criarBotaoModal("FÁCIL", new Color(40, 167, 69)); // Verde
        JButton btnMedio = criarBotaoModal("MÉDIO", new Color(255, 193, 7)); // Amarelo/Laranja
        JButton btnDificil = criarBotaoModal("DIFÍCIL", new Color(220, 53, 69)); // Vermelho
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(CENTER_ALIGNMENT);

        // Ações
        btnFacil.addActionListener(e -> selecionar("FACIL"));
        btnMedio.addActionListener(e -> selecionar("MEDIO"));
        btnDificil.addActionListener(e -> selecionar("DIFICIL"));
        btnCancelar.addActionListener(e -> dispose());

        // Montagem
        painel.add(titulo);
        painel.add(sub);
        painel.add(Box.createVerticalStrut(25));
        painel.add(btnFacil);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnMedio);
        painel.add(Box.createVerticalStrut(12));
        painel.add(btnDificil);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnCancelar);

        add(painel);
    }

    private JButton criarBotaoModal(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void selecionar(String nivel) {
        this.nivelSelecionado = nivel;
        dispose();
    }

    public String getNivel() {
        return nivelSelecionado;
    }
}

