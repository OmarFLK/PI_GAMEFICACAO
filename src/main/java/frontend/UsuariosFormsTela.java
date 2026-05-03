package frontend;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;

public class UsuariosFormsTela extends TelaBase {

    private final Usuario usuarioExistente; 
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private JTextField txtNome, txtEmail;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo;
    private JButton btnSalvar, btnCancelar;

    public UsuariosFormsTela(Usuario u) {
        super(u == null ? "QuimLab Pro - Novo Usuário" : "QuimLab Pro - Editar Usuário");
        this.usuarioExistente = u;
        initComponents();
        if (u != null) carregarDadosParaEdicao();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(40, 160, 40, 160));

        JPanel canvas = criarCanvasCentral();
        JPanel centro = criarColunaCentral(520);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel titulo = criarTituloHero(usuarioExistente == null ? "Novo Usuário" : "Editar Usuário");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamanhoPadrao = new Dimension(380, 55);

        // --- CAMPOS ---
        txtNome = criarCampoTexto("Nome Completo");
        txtNome.setMaximumSize(tamanhoPadrao);
        
        txtEmail = criarCampoTexto("E-mail Institucional");
        txtEmail.setMaximumSize(tamanhoPadrao);

        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(tamanhoPadrao);
        txtSenha.setHorizontalAlignment(JTextField.CENTER);
        txtSenha.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtSenha.setBorder(BorderFactory.createTitledBorder(usuarioExistente == null ? "Defina uma senha" : "Nova senha (opcional)"));

        cbTipo = new JComboBox<>(new String[]{"ALUNO", "PROFESSOR"});
        cbTipo.setMaximumSize(tamanhoPadrao);
        cbTipo.setFont(new Font("SansSerif", Font.PLAIN, 18));

        // --- BOTÕES ---
        btnSalvar = criarBotaoPrincipal(usuarioExistente == null ? "CADASTRAR" : "ATUALIZAR");
        btnSalvar.addActionListener(e -> salvar());

        btnCancelar = criarBotaoNeutro("CANCELAR");
        btnCancelar.addActionListener(e -> Navegador.abrirTela(this, new GerenciarUsuariosTela()));

        // Atalho Enter
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) salvar();
            }
        };
        txtNome.addKeyListener(enterKey);
        txtEmail.addKeyListener(enterKey);
        txtSenha.addKeyListener(enterKey);

        // --- MONTAGEM ---
        centro.add(Box.createVerticalGlue());
        centro.add(titulo);
        centro.add(Box.createVerticalStrut(30));
        
        centro.add(new JLabel("Nome:")); centro.add(txtNome);
        centro.add(Box.createVerticalStrut(10));
        
        centro.add(new JLabel("E-mail:")); centro.add(txtEmail);
        centro.add(Box.createVerticalStrut(10));
        
        centro.add(new JLabel("Senha:")); centro.add(txtSenha);
        centro.add(Box.createVerticalStrut(10));
        
        centro.add(new JLabel("Tipo de Acesso:")); centro.add(cbTipo);
        centro.add(Box.createVerticalStrut(40));
        
        centro.add(btnSalvar);
        centro.add(Box.createVerticalStrut(15));
        centro.add(btnCancelar);
        centro.add(Box.createVerticalGlue());

        canvas.add(centro, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void carregarDadosParaEdicao() {
        txtNome.setText(usuarioExistente.getNome());
        txtEmail.setText(usuarioExistente.getEmail());
        cbTipo.setSelectedItem(usuarioExistente.getTipo());
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String tipo = (String) cbTipo.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e E-mail são obrigatórios.");
            return;
        }

        // Se for novo, a senha é obrigatória
        if (usuarioExistente == null && senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A senha é obrigatória para novos usuários.");
            return;
        }

        setEstadoInterface(false, "Processando...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (usuarioExistente == null) {
                    // MODO CADASTRO
                    usuarioDAO.cadastrarUsuario(nome, email, senha, tipo);
                } else {
                    // MODO EDIÇÃO
                    // Se a senha estiver vazia, enviamos null ou uma flag para o DAO não alterar o hash atual
                    String senhaParaEnviar = senha.isEmpty() ? null : senha;
                    usuarioDAO.atualizarUsuario(usuarioExistente.getId(), nome, email, senhaParaEnviar, tipo);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(UsuariosFormsTela.this, "Dados salvos com sucesso!");
                    Navegador.abrirTela(UsuariosFormsTela.this, new GerenciarUsuariosTela());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(UsuariosFormsTela.this, "Erro ao salvar: " + e.getMessage());
                    setEstadoInterface(true, usuarioExistente == null ? "CADASTRAR" : "ATUALIZAR");
                }
            }
        };
        worker.execute();
    }

    private void setEstadoInterface(boolean ativo, String textoBotao) {
        btnSalvar.setEnabled(ativo);
        btnCancelar.setEnabled(ativo);
        btnSalvar.setText(textoBotao);
        txtNome.setEnabled(ativo);
        txtEmail.setEnabled(ativo);
        txtSenha.setEnabled(ativo);
        cbTipo.setEnabled(ativo);
    }
}