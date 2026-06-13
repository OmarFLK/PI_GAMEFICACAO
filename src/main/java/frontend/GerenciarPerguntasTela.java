package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import backend.DAO.perguntaDAO.Pergunta;
import backend.DAO.perguntaDAO.PerguntaDAO;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class GerenciarPerguntasTela extends TelaBase {

    private JTable tabela;
    private DefaultTableModel modelo;
    private final PerguntaDAO perguntaDAO = new PerguntaDAO();
    private JButton btnNovo, btnEditar, btnExcluir, btnTestar;

    public GerenciarPerguntasTela() {
        super("QuimLab Pro - Gerenciar Questões");
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

        JLabel titulo = new JLabel("Gerenciamento de Perguntas", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(AppTheme.TEXT);

        JButton btnVoltarHome = criarBotaoLink("Voltar");
        btnVoltarHome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltarHome.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        JPanel containerVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        containerVoltar.setOpaque(false);
        containerVoltar.add(btnVoltarHome);

        topo.add(titulo, BorderLayout.CENTER);
        topo.add(containerVoltar, BorderLayout.EAST);
        conteudo.add(topo, BorderLayout.NORTH);

        // --- INÍCIO DA TABELA MINIMALISTA DINÂMICA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Enunciado", "Dificuldade", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modelo) {
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

        JTableHeader header = tabela.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new java.awt.Dimension(0, 42));
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                ThemePalette palette = ThemeManager.getCurrentPalette();
                
                setText(value == null ? "" : value.toString().toUpperCase());
                
                java.awt.Color headerBg = ThemeManager.isDarkMode() ? new java.awt.Color(14, 14, 20) : new java.awt.Color(245, 245, 250);
                setBackground(headerBg);
                setForeground(palette.textSecondary());
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, palette.border()),
                    BorderFactory.createEmptyBorder(0, 18, 0, 18)
                ));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

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
                    java.awt.Color zebraColor = ThemeManager.isDarkMode() 
                        ? new java.awt.Color(32, 32, 44) 
                        : new java.awt.Color(250, 250, 253);
                        
                    setBackground(row % 2 == 0 ? palette.surface() : zebraColor);
                    
                    setForeground(col == 0 ? palette.textSecondary() : palette.textPrimary());
                    setFont(col == 0 ? new Font("Segoe UI", Font.BOLD, 12) : new Font("Segoe UI", Font.PLAIN, 14));
                }
                return this;
            }
        };
        tabela.setDefaultRenderer(Object.class, cellRenderer);
        
        // Ajusta a largura do ID e do Status
        tabela.getColumnModel().getColumn(0).setMaxWidth(64); 
        tabela.getColumnModel().getColumn(0).setMinWidth(48);
        tabela.getColumnModel().getColumn(3).setMaxWidth(100); 
        // --- FIM DA TABELA MINIMALISTA DINÂMICA ---

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());
        
        // Deixar o scroll transparente
        scrollTabela.setOpaque(false);
        scrollTabela.getViewport().setOpaque(false);

        // Esconder as barras de rolagem
        scrollTabela.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollTabela.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel cardTabela = criarCartaoSuave();
        cardTabela.setLayout(new BorderLayout());
        cardTabela.add(scrollTabela, BorderLayout.CENTER);
        conteudo.add(cardTabela, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(1, 4, 15, 0));
        acoes.setOpaque(false);
        btnNovo = criarBotaoPrincipal("NOVA PERGUNTA");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new PerguntaFormTela(null)));

        btnEditar = criarBotaoSecundario("EDITAR SELECIONADA");
        btnEditar.addActionListener(e -> prepararEdicao());

        btnTestar = criarBotaoSecundario("TESTAR PERGUNTA");
        btnTestar.addActionListener(e -> testarPerguntaSelecionada());

        btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(AppTheme.ERROR_HIGHLIGHT);
        btnExcluir.addActionListener(e -> confirmarExclusao());

        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnTestar); acoes.add(btnExcluir);

        conteudo.add(acoes, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void atualizarTabela() {
        setEstadoBotoes(false, "Carregando...");
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"...", "Buscando dados...", "...", "..."});

        SwingWorker<List<Pergunta>, Void> worker = new SwingWorker<>() {
            @Override protected List<Pergunta> doInBackground() throws Exception {
                return perguntaDAO.getTodasPerguntas();
            }
            @Override protected void done() {
                try {
                    List<Pergunta> lista = get();
                    modelo.setRowCount(0);
                    lista.forEach(p -> modelo.addRow(new Object[]{p.getId(), p.getEnunciado(), p.getDificuldade(), p.getAtiva() == 1 ? "Ativa" : "Inativa"}));
                } catch (InterruptedException | ExecutionException e) { JOptionPane.showMessageDialog(null, "Erro ao carregar."); }
                finally { setEstadoBotoes(true, ""); }
            }
        };
        worker.execute();
    }

    private void prepararEdicao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione uma pergunta."); return; }
        
        setEstadoBotoes(false, "Abrindo...");
        int id = (int) modelo.getValueAt(linha, 0);
        
        SwingWorker<Pergunta, Void> worker = new SwingWorker<>() {
            @Override protected Pergunta doInBackground() throws Exception {
                return perguntaDAO.getPergunta(id);
            }
            @Override protected void done() {
                try {
                    Navegador.abrirTela(GerenciarPerguntasTela.this, new PerguntaFormTela(get()));
                } catch (InterruptedException | ExecutionException e) { 
                    JOptionPane.showMessageDialog(null, "Erro ao abrir."); 
                    setEstadoBotoes(true, "");
                }
            }
        };
        worker.execute();
    }

    private void confirmarExclusao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pergunta para excluir.");
            return;
        }
        int id = (int) modelo.getValueAt(linha, 0);
        
        if (JOptionPane.showConfirmDialog(this, "Excluir pergunta " + id + "?", "Aviso", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            setEstadoBotoes(false, "Excluindo...");
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override protected Void doInBackground() throws Exception {
                    perguntaDAO.deletarPergunta(id);
                    return null;
                }
                @Override protected void done() {
                    atualizarTabela();
                }
            };
            worker.execute();
        }
    }

    private void testarPerguntaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pergunta para testar.");
            return;
        }

        setEstadoBotoes(false, "Carregando...");
        int id = (int) modelo.getValueAt(linha, 0);

        SwingWorker<backend.DAO.perguntaDAO.Pergunta, Void> worker = new SwingWorker<>() {
            @Override
            protected backend.DAO.perguntaDAO.Pergunta doInBackground() throws Exception {
                return perguntaDAO.getPergunta(id);
            }

            @Override
            protected void done() {
                try {
                    backend.DAO.perguntaDAO.Pergunta pergunta = get();
                    if (pergunta != null) {
                        setEstadoBotoes(true, "");
                        Navegador.abrirTela(GerenciarPerguntasTela.this,
                                new GameplayTela(pergunta, GerenciarPerguntasTela.this));
                    } else {
                        JOptionPane.showMessageDialog(GerenciarPerguntasTela.this, "Pergunta não encontrada.");
                        setEstadoBotoes(true, "");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(GerenciarPerguntasTela.this, "Erro ao carregar pergunta.");
                    setEstadoBotoes(true, "");
                }
            }
        };
        worker.execute();
    }

    private void setEstadoBotoes(boolean ativo, String textoStatus) {
        btnNovo.setEnabled(ativo);
        btnEditar.setEnabled(ativo);
        btnTestar.setEnabled(ativo);
        btnExcluir.setEnabled(ativo);

        if (!ativo) {
            btnTestar.setText(textoStatus);
        } else {
            btnNovo.setText("NOVA PERGUNTA");
            btnEditar.setText("EDITAR SELECIONADA");
            btnTestar.setText("TESTAR PERGUNTA");
            btnExcluir.setText("EXCLUIR");
        }
    }
}