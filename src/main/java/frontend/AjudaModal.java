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

import frontend.theme.ThemeManager;
import frontend.util.AppTheme;

public class AjudaModal extends JDialog {

    private final GameplayTela telaJogo;
    private JButton btnDica;
    private JLabel dicaFalseLabel;
    private int tirarMetadeTentativas = 2;
    private int mostrarDicaTentativas = 2;

    public AjudaModal(GameplayTela pai, boolean dicaExiste) {
        super(pai, true);
        this.telaJogo = pai;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        initComponents();
        ThemeManager.applyTheme(this);

        pack();
        setLocationRelativeTo(pai);
    }

    private void initComponents() {

        JPanel painel = new JPanel();
        painel.setBackground(AppTheme.SURFACE);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER, 2, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        JLabel titulo = new JLabel("Ajuda");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(AppTheme.TEXT);

        JLabel sub = new JLabel("Escolha sua ajuda!");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setForeground(AppTheme.TEXT_MUTED);

        JButton btn50 = criarBotaoModal("Tirar metade das alternativas", AppTheme.ERROR_HIGHLIGHT);
        btn50.addActionListener(e -> {
            telaJogo.tirarDuasAlternativas();
            tirarMetadeTentativas--;
            if (tirarMetadeTentativas == 0){
                btn50.setEnabled(false);
            }
        });

        btnDica = criarBotaoModal("Dica do professor", AppTheme.RED);
        btnDica.addActionListener(e -> {
            telaJogo.mostrarDica();
            telaJogo.ajudaButton.setEnabled(false);
            mostrarDicaTentativas--;
            if(mostrarDicaTentativas==0){
                btnDica.setEnabled(false);
            }
            dispose();  
        });

        dicaFalseLabel = new JLabel("Essa questão não tem dica");
        dicaFalseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dicaFalseLabel.setAlignmentX(CENTER_ALIGNMENT);
        dicaFalseLabel.setForeground(AppTheme.TEXT_MUTED);
        
        JButton btnFechar = criarBotaoModal("Voltar ao Jogo", AppTheme.SECONDARY_DARK);
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
        
        // Se a dica existe, mas as tentativas acabaram, o botão continua desabilitado
        if (existe && mostrarDicaTentativas <= 0) {
            btnDica.setEnabled(false);
        } else if (existe) {
            btnDica.setEnabled(true);
        }
        
        this.pack(); // Ajusta o tamanho do modal ao novo conteúdo
    }
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
