package frontend;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;

public class GerenciarUsuariosTela extends TelaBase {

    private JTable tabela;
    private DefaultTableModel modelo;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private JButton btnNovo, btnEditar, btnExcluir;

    public GerenciarUsuariosTela() {
        super("QuimLab Pro - Gerenciar Usuários");
        initComponents();
        atualizarTabela();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout(0, 20));
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // --- TOPO ---
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título centralizado e com destaque
        JLabel titulo = new JLabel("Gestão de Alunos e Professores", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        titulo.setForeground(new Color(44, 62, 80));
        
        // BOTÃO VOLTAR: Agora usando criarBotaoSecundario para seguir o estilo dos botões de baixo
        JButton btnVoltar = criarBotaoSecundario("← Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVoltar.setPreferredSize(new Dimension(150, 40)); // Tamanho menor para o topo
        btnVoltar.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        // Container para alinhar o botão à esquerda sem esticar
        JPanel containerVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        containerVoltar.setOpaque(false);
        containerVoltar.add(btnVoltar);

        // Placeholder à direita para garantir centralização perfeita do título
        Dimension tamanhoBotao = btnVoltar.getPreferredSize();
        Component spacer = Box.createRigidArea(tamanhoBotao);

        topo.add(containerVoltar, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(spacer, BorderLayout.EAST);

        painelExterno.add(topo, BorderLayout.NORTH);

        // --- TABELA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "E-mail", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(35);
        painelExterno.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- AÇÕES (ESTILO REFERÊNCIA) ---
        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0)); 
        acoes.setOpaque(false);

        btnNovo = criarBotaoPrincipal("NOVO USUÁRIO");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new UsuariosFormsTela(null)));

        btnEditar = criarBotaoSecundario("EDITAR DADOS");
        btnEditar.addActionListener(e -> prepararEdicao());

        btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(Color.RED);
        btnExcluir.addActionListener(e -> excluirUsuario());

        acoes.add(btnNovo);
        acoes.add(btnEditar);
        acoes.add(btnExcluir);

        painelExterno.add(acoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelExterno);
        setContentPane(painelPrincipal);
    }

    private void atualizarTabela() {
        setEstadoBotoes(false, "Carregando...");
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"...", "Buscando usuários...", "...", "..."});

        SwingWorker<List<Usuario>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Usuario> doInBackground() throws Exception {
                return usuarioDAO.listarTodos();
            }
            @Override
            protected void done() {
                try {
                    List<Usuario> lista = get();
                    modelo.setRowCount(0);
                    for (Usuario u : lista) {
                        modelo.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getTipo()});
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar.");
                } finally {
                    setEstadoBotoes(true, "");
                }
            }
        };
        worker.execute();
    }

    private void prepararEdicao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = (String) modelo.getValueAt(linha, 1);
        String email = (String) modelo.getValueAt(linha, 2);
        String tipo = (String) modelo.getValueAt(linha, 3);

        Usuario selecionado = new Usuario(id, nome, email, tipo);
        Navegador.abrirTela(this, new UsuariosFormsTela(selecionado));
    }

    private void excluirUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = (String) modelo.getValueAt(linha, 1);

        if (JOptionPane.showConfirmDialog(this, "Deseja excluir permanentemente " + nome + "?", 
            "Aviso", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            
            setEstadoBotoes(false, "Excluindo...");
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override protected Void doInBackground() throws Exception {
                    usuarioDAO.deletarUsuario(id);
                    return null;
                }
                @Override protected void done() { atualizarTabela(); }
            };
            worker.execute();
        }
    }

    private void setEstadoBotoes(boolean ativo, String status) {
        btnNovo.setEnabled(ativo);
        btnEditar.setEnabled(ativo);
        btnExcluir.setEnabled(ativo);
        if (!ativo) btnEditar.setText(status);
        else btnEditar.setText("EDITAR DADOS");
    }
}