package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
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
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(24, 60, 24, 60));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 20));
        conteudo.setOpaque(false);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 50));

        JLabel titulo = new JLabel("Gestão de Alunos e Professores", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(AppTheme.TEXT);

        JButton btnVoltar = criarBotaoLink("Voltar");
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        JPanel containerVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        containerVoltar.setOpaque(false);
        containerVoltar.add(btnVoltar);

        topo.add(titulo, BorderLayout.CENTER);
        topo.add(containerVoltar, BorderLayout.EAST);
        conteudo.add(topo, BorderLayout.NORTH);

        // --- INÍCIO DA TABELA MINIMALISTA DINÂMICA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "E-mail", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(modelo) {
            // Garante que a cor da grade se atualize na troca de temas
            @Override
            public void paint(java.awt.Graphics g) {
                setGridColor(ThemeManager.getCurrentPalette().border());
                super.paint(g);
            }
        };
        
        tabela.setRowHeight(46);
        tabela.setShowGrid(false);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tabela.setFocusable(false);
        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // Header customizado (Sem relevo, uppercase, alinhado à esquerda)
        JTableHeader header = tabela.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new java.awt.Dimension(0, 42));
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                ThemePalette palette = ThemeManager.getCurrentPalette();
                
                setText(value == null ? "" : value.toString().toUpperCase());
                
                // Cor do fundo do Header sutilmente diferente do fundo normal para destacar
                java.awt.Color headerBg = ThemeManager.isDarkMode() ? new java.awt.Color(14, 14, 20) : new java.awt.Color(245, 245, 250);
                setBackground(headerBg);
                setForeground(palette.textSecondary());
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                
                // Borda inferior separando o header do conteúdo
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, palette.border()),
                    BorderFactory.createEmptyBorder(0, 18, 0, 18)
                ));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

        // Renderer das células (Zebra striping inteligente + Cores dinâmicas)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                ThemePalette palette = ThemeManager.getCurrentPalette();
                
                setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
                setHorizontalAlignment(col == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                
                if (isSelected) {
                    setBackground(palette.selectionBackground());
                    setForeground(ThemeManager.isDarkMode() ? java.awt.Color.WHITE : palette.textPrimary());
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                } else {
                    // Efeito Zebra Sutil
                    java.awt.Color zebraColor = ThemeManager.isDarkMode() 
                        ? new java.awt.Color(32, 32, 44) 
                        : new java.awt.Color(250, 250, 253);
                        
                    setBackground(row % 2 == 0 ? palette.surface() : zebraColor);
                    
                    // Coluna de ID apagadinha, demais normais
                    setForeground(col == 0 ? palette.textSecondary() : palette.textPrimary());
                    setFont(col == 0 ? new Font("Segoe UI", Font.BOLD, 12) : new Font("Segoe UI", Font.PLAIN, 14));
                }
                return this;
            }
        };
        tabela.setDefaultRenderer(Object.class, cellRenderer);

        // Largura customizada para a coluna ID
        tabela.getColumnModel().getColumn(0).setMaxWidth(64);
        tabela.getColumnModel().getColumn(0).setMinWidth(48);
        // --- FIM DA TABELA MINIMALISTA DINÂMICA ---

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());
        
        // Deixar o scroll transparente para não quebrar os cantos arredondados do cartão
        scrollTabela.setOpaque(false);
        scrollTabela.getViewport().setOpaque(false);
        
        // Esconder as barras de rolagem
        scrollTabela.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollTabela.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel cardTabela = criarCartaoSuave();
        cardTabela.setLayout(new BorderLayout());
        cardTabela.add(scrollTabela, BorderLayout.CENTER);
        conteudo.add(cardTabela, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0));
        acoes.setOpaque(false);

        btnNovo = criarBotaoPrincipal("NOVO USUARIO");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new UsuariosFormsTela(null)));

        btnEditar = criarBotaoSecundario("EDITAR DADOS");
        btnEditar.addActionListener(e -> prepararEdicao());

        btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(AppTheme.ERROR_HIGHLIGHT);
        btnExcluir.addActionListener(e -> excluirUsuario());

        acoes.add(btnNovo);
        acoes.add(btnEditar);
        acoes.add(btnExcluir);

        conteudo.add(acoes, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
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