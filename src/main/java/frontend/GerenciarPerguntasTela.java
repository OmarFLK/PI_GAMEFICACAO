package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import backend.DAO.perguntaDAO.Pergunta;
import backend.DAO.perguntaDAO.PerguntaDAO;
import frontend.base.TelaBase;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class GerenciarPerguntasTela extends TelaBase {

    private JTable tabela;
    private DefaultTableModel modelo;
    private final PerguntaDAO perguntaDAO = new PerguntaDAO();
    private JButton btnNovo, btnEditar, btnExcluir;

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

        modelo = new DefaultTableModel(new Object[]{"ID", "Enunciado", "Dificuldade", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(35);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(new Color(255, 241, 243));
        tabela.setSelectionForeground(COR_PRETO);
        tabela.getTableHeader().setBackground(AppTheme.NEUTRAL_DARK);
        tabela.getTableHeader().setForeground(COR_BRANCO);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        JPanel cardTabela = criarCartaoSuave();
        cardTabela.setLayout(new BorderLayout());
        cardTabela.add(scrollTabela, BorderLayout.CENTER);
        conteudo.add(cardTabela, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0));
        acoes.setOpaque(false);
        btnNovo = criarBotaoPrincipal("NOVA PERGUNTA");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new PerguntaFormTela(null)));
        
        btnEditar = criarBotaoSecundario("EDITAR SELECIONADA");
        btnEditar.addActionListener(e -> prepararEdicao());
        
        btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(AppTheme.ERROR_HIGHLIGHT);
        btnExcluir.addActionListener(e -> confirmarExclusao());
        
        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir);

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
        if (linha == -1) return;
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

    private void setEstadoBotoes(boolean ativo, String textoStatus) {
        btnNovo.setEnabled(ativo);
        btnEditar.setEnabled(ativo);
        btnExcluir.setEnabled(ativo);
        
        if (!ativo) {
            btnEditar.setText(textoStatus);
        } else {
            btnNovo.setText("NOVA PERGUNTA");
            btnEditar.setText("EDITAR SELECIONADA");
            btnExcluir.setText("EXCLUIR");
        }
    }
}
