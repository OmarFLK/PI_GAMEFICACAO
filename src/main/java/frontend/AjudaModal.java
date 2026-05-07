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
        btn50.addActionListener(e -> telaJogo.tirarDuasAlternativas());

        JButton btnDica = criarBotaoModal("Dica do professor", new Color(120, 53, 69));
        btnDica.addActionListener(e -> {
            JLabel labelDica = new JLabel("<html><body style='width: 200px;'>" +
                    "Sua dica aqui!" + "</body></html>");
            labelDica.setAlignmentX(CENTER_ALIGNMENT);
            labelDica.setForeground(new Color(120, 53, 69));

            painel.add(Box.createVerticalStrut(10)); // Espaçamento
            painel.add(labelDica);

            btnDica.setEnabled(false);

            painel.revalidate(); 
            painel.repaint();
            this.pack(); 
        });

        painel.add(titulo);
        painel.add(sub);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btn50);
        painel.add(Box.createVerticalStrut(15));
        painel.add(btnDica);

        add(painel);
    }

    private JLabel mostrarDica() {

        JLabel dica = new JLabel("hamudi habibi");
        dica.setFont(new Font("Segoe UI", Font.BOLD, 28));
        dica.setAlignmentX(CENTER_ALIGNMENT);
        dica.setForeground(new Color(33, 37, 41));

        return dica;
        // dispose();

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
