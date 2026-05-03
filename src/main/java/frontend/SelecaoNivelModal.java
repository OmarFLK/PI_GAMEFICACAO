package frontend;

import java.awt.*;
import javax.swing.*;
import frontend.base.TelaBase;
import frontend.util.Navegador; // Importante para abrir a tela

public class SelecaoNivelModal extends JDialog {
    private String nivelSelecionado = null;
    private JFrame pai;
    private String tipoUsuario;

    // Atualizei o construtor para aceitar o tipo de usuário (ALUNO ou PROFESSOR)
    public SelecaoNivelModal(JFrame pai, String tipoUsuario) {
        super(pai, true);
        this.pai = pai;
        this.tipoUsuario = tipoUsuario;
        
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        initComponents();
        
        pack();
        setLocationRelativeTo(pai);
    }

    private void initComponents() {
        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 37, 41), 2, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel titulo = new JLabel("Dificuldade");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(new Color(33, 37, 41));

        JLabel sub = new JLabel("Escolha o nível do desafio:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setForeground(Color.GRAY);

        JButton btnFacil = criarBotaoModal("FÁCIL", new Color(40, 167, 69));
        JButton btnMedio = criarBotaoModal("MÉDIO", new Color(255, 193, 7));
        JButton btnDificil = criarBotaoModal("DIFÍCIL", new Color(220, 53, 69));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(CENTER_ALIGNMENT);

        // AÇÕES CORRIGIDAS: Agora elas chamam o método iniciarJogo
        btnFacil.addActionListener(e -> iniciarJogo("FACIL"));
        btnMedio.addActionListener(e -> iniciarJogo("MEDIO"));
        btnDificil.addActionListener(e -> iniciarJogo("DIFICIL"));
        btnCancelar.addActionListener(e -> dispose());

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

    // MÉTODO NOVO: É aqui que a mágica acontece
    private void iniciarJogo(String nivel) {
        this.nivelSelecionado = nivel;
        this.dispose(); // Fecha o modal
        
        // Abre a tela de Gameplay passando a dificuldade E o tipo de usuário
        // Isso garante que o banco de dados receba o filtro e as questões apareçam
        Navegador.abrirTela(pai, new GameplayTela(tipoUsuario, nivel));
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

    public String getNivel() {
        return nivelSelecionado;
    }
}