package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class RegistroTela extends TelaBase {

    private JTextField anoTextField;
    private JTextField turmaTextField;
    private JTextField nomeTextField;
    private JTextField emailTextField;
    private JPasswordField senhaPasswordField;
    private JPasswordField confirmarSenhaPasswordField;

    public RegistroTela() {
        super("QuimLab - Registro");
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 150, 24, 150));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);

        JPanel coluna = criarColunaCentral(560);
        JLabel titulo = criarTituloHero("Etec");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 70));
        JLabel subtitulo = criarSubtituloHero("Escola Tecnica Estadual");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 22));
        JLabel aviso = criarTextoCentral("Apenas alunos. Cadastro de professor e feito pelo administrador.");
        aviso.setForeground(new Color(110, 110, 110));

        anoTextField = criarCampoTexto("Ano");
        turmaTextField = criarCampoTexto("Turma");
        nomeTextField = criarCampoTexto("Nome e primeiro sobrenome");
        emailTextField = criarCampoTexto("Login");
        senhaPasswordField = criarCampoSenha("Senha");
        confirmarSenhaPasswordField = criarCampoSenha("Repita a senha");

        JButton ajudaButton = criarBotaoLink("Ajuda");
        ajudaButton.addActionListener(evt -> JOptionPane.showMessageDialog(
            this,
            "Cadastro visual para alunos conforme a proposta do sistema.",
            "QuimLab",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JButton cadastrarButton = criarBotaoPrincipal("Cadastrar");
        cadastrarButton.addActionListener(evt -> cadastrarAluno());

        JButton voltarButton = criarBotaoLink("Voltar ao login");
        voltarButton.addActionListener(evt -> Navegador.abrirLogin(this));

        coluna.add(titulo);
        coluna.add(Box.createVerticalStrut(6));
        coluna.add(subtitulo);
        coluna.add(Box.createVerticalStrut(14));
        coluna.add(aviso);
        coluna.add(Box.createVerticalStrut(28));
        coluna.add(anoTextField);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(turmaTextField);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(nomeTextField);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(emailTextField);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(senhaPasswordField);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(confirmarSenhaPasswordField);
        coluna.add(Box.createVerticalStrut(14));
        coluna.add(ajudaButton);
        coluna.add(Box.createVerticalStrut(14));
        coluna.add(cadastrarButton);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(voltarButton);

        conteudo.add(coluna, BorderLayout.CENTER);
        canvas.add(conteudo, BorderLayout.CENTER);
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

    private void cadastrarAluno() {
        if (isVazio(anoTextField, "Ano") || isVazio(turmaTextField, "Turma") || isVazio(nomeTextField, "Nome e primeiro sobrenome")
            || isVazio(emailTextField, "Login") || isSenhaVazia(senhaPasswordField, "Senha")
            || isSenhaVazia(confirmarSenhaPasswordField, "Repita a senha")) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos do registro.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String senha = new String(senhaPasswordField.getPassword()).trim();
        String confirmarSenha = new String(confirmarSenhaPasswordField.getPassword()).trim();
        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas precisam ser iguais.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Cadastro visual concluido.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
        Navegador.abrirLogin(this);
    }

    private boolean isVazio(JTextField campo, String placeholder) {
        String valor = campo.getText().trim();
        return valor.isEmpty() || placeholder.equalsIgnoreCase(valor);
    }

    private boolean isSenhaVazia(JPasswordField campo, String placeholder) {
        String valor = new String(campo.getPassword()).trim();
        return valor.isEmpty() || placeholder.equalsIgnoreCase(valor);
    }
}
