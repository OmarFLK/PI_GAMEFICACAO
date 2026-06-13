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

import frontend.theme.ThemeAware;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.Navegador;

public class SelecaoNivelModal extends JDialog implements ThemeAware {
    private final String nivelSelecionado = null;
    private final JFrame pai;
    private final String tipoUsuario;
    private Component glassPaneOriginal;
    private ComponentAdapter rastreadorMovimento; // NOVO: Rastreador de movimento da tela

    // Componentes isolados para podermos trocar a cor no modo escuro
    private JPanel painelPrincipal;
    private JLabel titulo;
    private JLabel sub;
    private JButton btnCancelar;

    public SelecaoNivelModal(JFrame pai, String tipoUsuario) {
        super(pai, true);
        this.pai = pai;
        this.tipoUsuario = tipoUsuario;
        
        setUndecorated(true);

        initComponents();
        
        // Aplica o tema na inicialização
        ThemeManager.applyTheme(this);
        
        pack();
        setLocationRelativeTo(pai);
    }

    // --- LÓGICA DO FUNDO ESCURO E RASTREAMENTO DA JANELA ---
    @Override
    public void setVisible(boolean b) {
        if (b) {
            // 1. Configura a máscara escura
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

            // 2. Cria e adiciona o rastreador para o modal seguir a tela principal
            rastreadorMovimento = new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    setLocationRelativeTo(pai); // Centraliza de novo se a tela andar
                }
                @Override
                public void componentResized(ComponentEvent e) {
                    setLocationRelativeTo(pai); // Centraliza de novo se a tela mudar de tamanho
                }
            };
            pai.addComponentListener(rastreadorMovimento);
        }
        
        // A execução do Java pausa aqui até o usuário fechar o modal
        super.setVisible(b); 
        
        // Quando o modal fecha (dispose), a execução retoma aqui:
        if (b) {
            // 1. Remove o rastreador para evitar vazamento de memória
            if (rastreadorMovimento != null) {
                pai.removeComponentListener(rastreadorMovimento);
            }

            // 2. Restaura a tela original
            if (glassPaneOriginal != null) {
                pai.getGlassPane().setVisible(false);
                pai.setGlassPane(glassPaneOriginal);
            }
        }
    }
    // --------------------------------------------------------

    private void initComponents() {
        painelPrincipal = new JPanel();
        painelPrincipal.setOpaque(true); 
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        titulo = new JLabel("Dificuldade");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        sub = new JLabel("Escolha o nível do desafio:");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        // --- GRADIENTE DE VERMELHOS (DO CLARO AO MAIS ESCURO) ---
        JButton btnFacil = criarBotaoModal("FÁCIL", new Color(255, 102, 102));           // Vermelho Claro
        JButton btnMedio = criarBotaoModal("MÉDIO", new Color(204, 0, 0));               // Vermelho Vivo
        JButton btnDificil = criarBotaoModal("DIFÍCIL", new Color(128, 0, 0));           // Vermelho Escuro
        JButton btnProgressivo = criarBotaoModal("PROGRESSIVO", new Color(50, 0, 0));    // Vermelho muito escuro (Quase Preto)
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(CENTER_ALIGNMENT);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnFacil.addActionListener(e -> iniciarJogo("FACIL", btnFacil));
        btnMedio.addActionListener(e -> iniciarJogo("MEDIO", btnMedio));
        btnDificil.addActionListener(e -> iniciarJogo("DIFICIL", btnDificil));
        btnProgressivo.addActionListener(e -> iniciarJogo("PROGRESSIVO", btnProgressivo));
        btnCancelar.addActionListener(e -> dispose());

        painelPrincipal.add(titulo);
        painelPrincipal.add(sub);
        painelPrincipal.add(Box.createVerticalStrut(25));
        painelPrincipal.add(btnFacil);
        painelPrincipal.add(Box.createVerticalStrut(12));
        painelPrincipal.add(btnMedio);
        painelPrincipal.add(Box.createVerticalStrut(12));
        painelPrincipal.add(btnDificil);
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(btnProgressivo);
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(btnCancelar);

        add(painelPrincipal);
    }

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
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro ao carregar partida!" + ex.getMessage());
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

    // --- INTEGRAÇÃO COM O MODO ESCURO ---
    @Override
    public void applyTheme(ThemePalette palette) {
        if (painelPrincipal != null) {
            painelPrincipal.setBackground(palette.surface());
            painelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(palette.border(), 2, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
            ));
        }
        if (titulo != null) titulo.setForeground(palette.textPrimary());
        if (sub != null) sub.setForeground(palette.textSecondary());
        if (btnCancelar != null) btnCancelar.setForeground(palette.textSecondary());
        
        repaint();
    }

    public String getNivel() {
        return nivelSelecionado;
    }
}