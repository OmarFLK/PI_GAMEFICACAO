package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import frontend.base.TelaBase;
import frontend.util.Navegador;

public class LoginTela extends TelaBase {

    private JTextField loginTextField;
    private JPasswordField senhaPasswordField;

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

        JLabel labelLogin = new JLabel("Login");
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

        JButton entrarButton = criarBotaoPrincipal("ENTRAR");
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
        
        coluna.add(Box.createVerticalStrut(30));
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
            JOptionPane.showMessageDialog(this, "Preencha os campos para continuar.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // INTEGRAÇÃO REAL
        backend.DAO.usuarioDAO.UsuarioDAO usuarioDAO = new backend.DAO.usuarioDAO.UsuarioDAO();
        backend.DAO.usuarioDAO.Usuario usuarioLogado = usuarioDAO.efetuarLogin(email, senha);

        if (usuarioLogado != null) {
            // SALVA NA SESSÃO PARA USAR EM QUALQUER TELA
            backend.util.SessaoUsuario.setUsuario(usuarioLogado);
            // Redireciona baseado no tipo vindo do banco
            if ("PROFESSOR".equals(usuarioLogado.getTipo())) {
                Navegador.abrirTela(this, new HomeProfessorTela());
            } else {
                Navegador.abrirTela(this, new HomeAlunoTela());
            }
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}