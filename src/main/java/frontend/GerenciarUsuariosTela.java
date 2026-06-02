package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;
import frontend.base.TelaBase;
import frontend.util.Navegador;

public class GerenciarUsuariosTela extends TelaBase {

    private JTable tabela;
    private DefaultTableModel modelo;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private JButton btnNovo, btnEditar, btnExcluir;

    public GerenciarUsuariosTela() {
        super("QuimLab Pro - Gerenciar Usuarios");
        initComponents();
        atualizarTabela();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout(0, 20));
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titulo = new JLabel("Gestao de Alunos e Professores", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        titulo.setForeground(new Color(44, 62, 80));

        JButton btnVoltar = criarBotaoSecundario("<- Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVoltar.setPreferredSize(new Dimension(150, 40));
        btnVoltar.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        JPanel containerVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        containerVoltar.setOpaque(false);
        containerVoltar.add(btnVoltar);

        Dimension tamanhoBotao = btnVoltar.getPreferredSize();
        Component spacer = Box.createRigidArea(tamanhoBotao);

        topo.add(containerVoltar, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(spacer, BorderLayout.EAST);
        painelExterno.add(topo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "E-mail", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(35);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(new Color(255, 241, 243));
        tabela.setSelectionForeground(COR_PRETO);
        tabela.getTableHeader().setBackground(COR_PRETO);
        tabela.getTableHeader().setForeground(COR_BRANCO);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        painelExterno.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0));
        acoes.setOpaque(false);

        btnNovo = criarBotaoPrincipal("NOVO USUARIO");
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
        modelo.addRow(new Object[]{"...", "Buscando usuarios...", "...", "..."});

        SwingWorker<List<Usuario>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Usuario> doInBackground() {
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
            JOptionPane.showMessageDialog(this, "Selecione um usuario para editar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = valorTabela(linha, 1);
        String email = valorTabela(linha, 2);
        String tipo = valorTabela(linha, 3);

        Usuario selecionado = new Usuario(id, nome, email, tipo);
        Navegador.abrirTela(this, new UsuariosFormsTela(selecionado));
    }

    private void excluirUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = valorTabela(linha, 1);

        if (JOptionPane.showConfirmDialog(this, "Deseja excluir permanentemente " + nome + "?",
            "Aviso", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            setEstadoBotoes(false, "Excluindo...");
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override protected Void doInBackground() {
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
        if (!ativo) {
            btnEditar.setText(status);
        } else {
            btnEditar.setText("EDITAR DADOS");
        }
    }

    private String valorTabela(int linha, int coluna) {
        Object valor = modelo.getValueAt(linha, coluna);
        return valor == null ? "" : valor.toString();
    }
}
