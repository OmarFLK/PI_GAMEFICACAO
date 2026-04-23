package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import frontend.base.TelaBase;
import frontend.util.Navegador;

public class LoginTela extends TelaBase {

    private JTextField loginTextField;
    private JPasswordField senhaPasswordField;
    private JRadioButton alunoRadioButton;
    private JRadioButton professorRadioButton;

    public LoginTela() {
        super("QuimLab - Login");
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 160, 24, 160));

        JPanel canvas = criarCanvasCentral();
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);

        JPanel coluna = criarColunaCentral(520);
        coluna.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = criarTituloHero("Etec");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 74));
        JLabel subtitulo = criarSubtituloHero("Escola Tecnica Estadual");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 22));

        loginTextField = criarCampoTexto("Login");
        senhaPasswordField = criarCampoSenha("Senha");

        alunoRadioButton = criarOpcaoPerfil("Aluno");
        professorRadioButton = criarOpcaoPerfil("Professor");
        alunoRadioButton.setSelected(true);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(alunoRadioButton);
        grupo.add(professorRadioButton);

        JPanel perfilPanel = new JPanel();
        perfilPanel.setOpaque(false);
        perfilPanel.setLayout(new BoxLayout(perfilPanel, BoxLayout.X_AXIS));
        perfilPanel.add(alunoRadioButton);
        perfilPanel.add(Box.createHorizontalStrut(14));
        perfilPanel.add(professorRadioButton);

        JButton esquecerSenha = criarBotaoLink("Esqueci a senha");
        esquecerSenha.addActionListener(evt -> JOptionPane.showMessageDialog(
            this,
            "Fluxo de recuperacao ainda mockado.",
            "QuimLab",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JButton entrarButton = criarBotaoPrincipal("ENTRAR");
        entrarButton.addActionListener(evt -> entrarSistema());

        JButton registroButton = criarBotaoLink("Cadastrar aluno");
        registroButton.addActionListener(evt -> Navegador.abrirTela(this, new RegistroTela()));

        coluna.add(Box.createVerticalStrut(28));
        coluna.add(titulo);
        coluna.add(Box.createVerticalStrut(6));
        coluna.add(subtitulo);
        coluna.add(Box.createVerticalStrut(42));
        coluna.add(loginTextField);
        coluna.add(Box.createVerticalStrut(20));
        coluna.add(senhaPasswordField);
        coluna.add(Box.createVerticalStrut(20));
        coluna.add(perfilPanel);
        coluna.add(Box.createVerticalStrut(20));
        coluna.add(esquecerSenha);
        coluna.add(Box.createVerticalStrut(20));
        coluna.add(entrarButton);
        coluna.add(Box.createVerticalStrut(18));
        coluna.add(registroButton);

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
        campo.setForeground(COR_CINZA);
        campo.setBackground(COR_BRANCO);
        campo.setBorder(new RoundedLineBorder(COR_PRETO, 2, 30, 18));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        campo.setPreferredSize(new Dimension(0, 68));
        return campo;
    }

    private JRadioButton criarOpcaoPerfil(String texto) {
        JRadioButton radioButton = new JRadioButton(texto);
        radioButton.setOpaque(false);
        radioButton.setFocusPainted(false);
        radioButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        radioButton.setForeground(COR_PRETO);
        return radioButton;
    }

    private void entrarSistema() {
        String login = loginTextField.getText().trim();
        String senha = new String(senhaPasswordField.getPassword()).trim();

        if (login.isEmpty() || "Login".equalsIgnoreCase(login) || senha.isEmpty() || "Senha".equalsIgnoreCase(senha)) {
            JOptionPane.showMessageDialog(this, "Preencha login e senha para continuar.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (professorRadioButton.isSelected()) {
            Navegador.abrirTela(this, new HomeProfessorTela());
            return;
        }

        Navegador.abrirTela(this, new HomeAlunoTela());
    }
}
