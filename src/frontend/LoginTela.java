package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;
import backend.Seguranca.SessaoUsuario;

public class LoginTela extends TelaBase {

    private JTextField loginTextField;
    private JPasswordField senhaPasswordField;
    private JButton entrarButton;
    private JLabel erroLabel; // Nova variável para a linha de erro

    public LoginTela() {
        super("QuimLab - Login");
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(40, 160, 40, 160));

        JPanel canvas = criarCanvasCentral();
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);

        JPanel coluna = criarColunaCentral(520);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = criarTituloHero("Etec");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 74));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = criarSubtituloHero("Escola Técnica Estadual");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamanhoPadrao = new Dimension(380, 55);

        JLabel labelLogin = new JLabel("Login (E-mail)");
        labelLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginTextField = criarCampoTexto("");
        loginTextField.setMaximumSize(tamanhoPadrao);
        loginTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelSenha = new JLabel("Senha");
        labelSenha.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelSenha.setAlignmentX(Component.CENTER_ALIGNMENT);

        senhaPasswordField = criarCampoSenha("");
        senhaPasswordField.setMaximumSize(tamanhoPadrao);
        senhaPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- LINHA DE ERRO (Invisível por padrão) ---
        erroLabel = new JLabel("Usuário ou senha inválidos.");
        erroLabel.setForeground(Color.RED);
        erroLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        erroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        erroLabel.setVisible(false);

        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    entrarSistema();
                }
            }
        };
        loginTextField.addKeyListener(enterKeyAdapter);
        senhaPasswordField.addKeyListener(enterKeyAdapter);

        entrarButton = criarBotaoPrincipal("ENTRAR");
        entrarButton.setMaximumSize(tamanhoPadrao);
        entrarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        entrarButton.addActionListener(evt -> entrarSistema());

        JButton esquecerSenha = criarBotaoLink("Esqueci a senha");
        esquecerSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        esquecerSenha.addActionListener(evt -> JOptionPane.showMessageDialog(
            this,
            "Fluxo de recuperação ainda mockado.",
            "QuimLab",
            JOptionPane.INFORMATION_MESSAGE
        ));

        coluna.add(Box.createVerticalGlue());
        coluna.add(titulo);
        coluna.add(Box.createVerticalStrut(5));
        coluna.add(subtitulo);
        coluna.add(Box.createVerticalStrut(40));
        
        coluna.add(labelLogin);
        coluna.add(Box.createVerticalStrut(8));
        coluna.add(loginTextField);
        
        coluna.add(Box.createVerticalStrut(15));
        
        coluna.add(labelSenha);
        coluna.add(Box.createVerticalStrut(8));
        coluna.add(senhaPasswordField);
        
        // Adiciona a linha de erro logo abaixo da senha
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(erroLabel);
        
        coluna.add(Box.createVerticalStrut(20));
        coluna.add(entrarButton);
        coluna.add(Box.createVerticalStrut(15));
        coluna.add(esquecerSenha);
        coluna.add(Box.createVerticalGlue());

        centro.add(coluna, BorderLayout.CENTER);
        canvas.add(centro, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPasswordField criarCampoSenha(String texto) {
        JPasswordField campo = new JPasswordField(texto);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        campo.setForeground(COR_PRETO);
        campo.setBackground(COR_BRANCO);
        campo.setBorder(new RoundedLineBorder(COR_PRETO, 2, 30, 18));
        return campo;
    }

    private void entrarSistema() {
        String email = loginTextField.getText().trim();
        String senha = new String(senhaPasswordField.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            erroLabel.setText("Preencha todos os campos.");
            mostrarErroTemporario();
            return;
        }

        setEstadoInterface(false, "AUTENTICANDO...");

        SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                return usuarioDAO.efetuarLogin(email, senha);
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get();
                    if (usuario != null) {
                        SessaoUsuario.getInstancia().setUsuario(usuario);
                        Navegador.abrirHome(LoginTela.this, usuario.getTipo());
                    } else {
                        // AQUI: Em vez do JOptionPane, limpa senha e mostra texto
                        senhaPasswordField.setText("");
                        erroLabel.setText("Usuário ou senha inválidos.");
                        mostrarErroTemporario();
                        setEstadoInterface(true, "ENTRAR");
                    }
                } catch (Exception e) {
                    erroLabel.setText("Erro de conexão.");
                    mostrarErroTemporario();
                    setEstadoInterface(true, "ENTRAR");
                }
            }
        };
        worker.execute();
    }

    // Método para controlar a exibição da linha de erro
    private void mostrarErroTemporario() {
        erroLabel.setVisible(true);
        this.revalidate();
        this.repaint();

        Timer timer = new Timer(3000, e -> {
            erroLabel.setVisible(false);
            this.revalidate();
            this.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void setEstadoInterface(boolean ativo, String textoBotao) {
        entrarButton.setEnabled(ativo);
        entrarButton.setText(textoBotao);
        loginTextField.setEnabled(ativo);
        senhaPasswordField.setEnabled(ativo);
    }
}