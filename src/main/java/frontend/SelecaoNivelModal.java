package frontend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font; // Importante para abrir a tela

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.util.Navegador;

public class SelecaoNivelModal extends JDialog {
    private final String nivelSelecionado = null;
    private final JFrame pai;
    private final String tipoUsuario;

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
        JButton btnProgressivo = criarBotaoModal("PROGRESSIVO", new Color(255, 165, 0));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(CENTER_ALIGNMENT);

        // AÇÕES CORRIGIDAS: Agora elas chamam o método iniciarJogo
        btnFacil.addActionListener(e -> iniciarJogo("FACIL",btnFacil));
        btnMedio.addActionListener(e -> iniciarJogo("MEDIO",btnMedio));
        btnDificil.addActionListener(e -> iniciarJogo("DIFICIL",btnDificil));
        btnProgressivo.addActionListener(e -> iniciarJogo("PROGRESSIVO",btnProgressivo));
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
        painel.add(btnProgressivo);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnCancelar);

        add(painel);
    }

    // MÉTODO NOVO: É aqui que a mágica acontece
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
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro ao carregar partida!");
                });
            }
        }).start();
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