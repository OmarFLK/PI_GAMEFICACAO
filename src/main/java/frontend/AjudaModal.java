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
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AjudaModal extends JDialog {
    private GameplayTela telaJogo;
    private int tirarMetadeTentativas = 2;
    private int mostrarDicaTentativas = 2;

    public AjudaModal(GameplayTela pai) {
        super(pai, true);
        this.telaJogo = pai;
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
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        JLabel titulo = new JLabel("Ajuda");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(new Color(33, 37, 41));

        JLabel sub = new JLabel("Escolha sua ajuda!");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setForeground(Color.GRAY);

        JButton btn50 = criarBotaoModal("Tirar metade das alternativas", new Color(220, 53, 69));
        btn50.addActionListener(e -> {
            telaJogo.tirarDuasAlternativas();
            tirarMetadeTentativas--;
            if (tirarMetadeTentativas == 0){
                btn50.setEnabled(false);
            }
        });

        JButton btnDica = criarBotaoModal("Dica do professor", new Color(120, 53, 69));
        btnDica.addActionListener(e -> {
            telaJogo.mostrarDica();
            telaJogo.ajudaButton.setEnabled(false);
            mostrarDicaTentativas--;
            if(mostrarDicaTentativas==0){
                btnDica.setEnabled(false);
            }
            
        });
        JButton btnFechar = criarBotaoModal("Voltar ao Jogo", new Color(108, 117, 125));
        btnFechar.addActionListener(e -> dispose());

        painel.add(titulo);
        painel.add(sub);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btn50);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnDica);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnFechar); 

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
}
