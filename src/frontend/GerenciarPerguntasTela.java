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

        // --- CABEÇALHO COM BOTÃO VOLTAR ---
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JLabel titulo = new JLabel("Gerenciamento de Perguntas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(44, 62, 80));
        
        JButton btnVoltarHome = criarBotaoLink("← Voltar para o Painel");
        btnVoltarHome.addActionListener(e -> Navegador.abrirHome(this, Navegador.TIPO_PROFESSOR));

        topo.add(titulo, BorderLayout.CENTER);
        topo.add(btnVoltarHome, BorderLayout.WEST); // Botão de voltar no canto superior esquerdo
        
        painelExterno.add(topo, BorderLayout.NORTH);

        // --- TABELA ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Enunciado", "Dificuldade", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(35);
        JScrollPane scroll = new JScrollPane(tabela);
        
        painelExterno.add(scroll, BorderLayout.CENTER);

        // --- BARRA DE AÇÕES ---
        JPanel acoes = new JPanel(new GridLayout(1, 3, 15, 0));
        acoes.setOpaque(false);

        JButton btnNovo = criarBotaoPrincipal("NOVA PERGUNTA");
        btnNovo.addActionListener(e -> Navegador.abrirTela(this, new PerguntaFormTela(null)));

        JButton btnEditar = criarBotaoSecundario("EDITAR SELECIONADA");
        btnEditar.addActionListener(e -> prepararEdicao());

        JButton btnExcluir = criarBotaoNeutro("EXCLUIR");
        btnExcluir.setForeground(Color.RED);
        btnExcluir.addActionListener(e -> confirmarExclusao());

        acoes.add(btnNovo);
        acoes.add(btnEditar);
        acoes.add(btnExcluir);

        painelExterno.add(acoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelExterno);
        setContentPane(painelPrincipal);
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        List<Pergunta> lista = perguntaDAO.getTodasPerguntas(); 
        for (Pergunta p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), 
                p.getEnunciado(), 
                p.getDificuldade(), 
                p.getAtiva() == 1 ? "Ativa" : "Inativa"
            });
        }
    }

    private void prepararEdicao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pergunta primeiro.");
            return;
        }
        int id = (int) modelo.getValueAt(linha, 0);
        Pergunta p = perguntaDAO.getPergunta(id);
        Navegador.abrirTela(this, new PerguntaFormTela(p));
    }

    private void confirmarExclusao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        
        int id = (int) modelo.getValueAt(linha, 0);
        int resp = JOptionPane.showConfirmDialog(this, "Excluir permanentemente a pergunta " + id + "?", "Aviso", JOptionPane.YES_NO_OPTION);
        
        if (resp == JOptionPane.YES_OPTION) {
            perguntaDAO.deletarPergunta(id);
            atualizarTabela();
        }
    }
}