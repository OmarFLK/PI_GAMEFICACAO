package frontend;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.perguntaDAO.*;

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
        JPanel painelExterno = new JPanel(new BorderLayout(0, 20));
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // --- TOPO ---
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título centralizado com 32px
        JLabel titulo = new JLabel("Gerenciamento de Banco de Dados", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        titulo.setForeground(new Color(44, 62, 80));

        // BOTÃO VOLTAR (Estilo Meu Perfil - Corrigido)
        JButton btnVoltarHome = new JButton("← Voltar");
        btnVoltarHome.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVoltarHome.setForeground(new Color(44, 62, 80));
        btnVoltarHome.setBackground(Color.WHITE);
        btnVoltarHome.setOpaque(true);
        btnVoltarHome.setFocusPainted(false);
        btnVoltarHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Usando uma borda arredondada padrão do Swing para evitar erros de compilação
        btnVoltarHome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true), 
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnVoltarHome.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        // Container para alinhar à esquerda
        JPanel containerVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        containerVoltar.setOpaque(false);
        containerVoltar.add(btnVoltarHome);

        // Spacer para manter o título no centro exato
        Dimension tamanhoBotao = btnVoltarHome.getPreferredSize();
        Component spacer = Box.createRigidArea(tamanhoBotao);

        topo.add(containerVoltar, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(spacer, BorderLayout.EAST);
        painelExterno.add(topo, BorderLayout.NORTH);

        // --- TABELA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Enunciado", "Dificuldade", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(35);
        painelExterno.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- AÇÕES ---
        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0));
        acoes.setOpaque(false);
        btnNovo = criarBotaoPrincipal("NOVA PERGUNTA");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new PerguntaFormTela(null)));
        
        btnEditar = criarBotaoSecundario("EDITAR SELECIONADA");
        btnEditar.addActionListener(e -> prepararEdicao());
        
        btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(Color.RED);
        btnExcluir.addActionListener(e -> confirmarExclusao());
        
        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir);

        painelExterno.add(acoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelExterno);
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
                } catch (Exception e) { JOptionPane.showMessageDialog(null, "Erro ao carregar."); }
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
                } catch (Exception e) { 
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