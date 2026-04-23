package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.PerguntaCadastroMock;
import frontend.util.Navegador;

public class GerenciarPerguntasTela extends TelaBase {

    private JTable perguntasTable;
    private final List<PerguntaCadastroMock> perguntas;

    public GerenciarPerguntasTela() {
        super("QuimLab - Gerenciar perguntas");
        this.perguntas = DadosMockados.getPerguntasGerenciar();
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(26, 90, 26, 90));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);

        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 60));
        voltarButton.addActionListener(evt -> Navegador.abrirTela(this, new HomeProfessorTela()));
        conteudo.add(criarTopoComVoltar("ADICIONAR/EDITAR PERGUNTAS", voltarButton), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new Object[] { "Pergunta", "Dificuldade", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (PerguntaCadastroMock pergunta : perguntas) {
            model.addRow(new Object[] { pergunta.getEnunciado(), pergunta.getDificuldade(), pergunta.getStatus() });
        }

        perguntasTable = new JTable(model);
        perguntasTable.setRowHeight(36);

        JPanel acoes = criarLinhaBotoes(
            criarBotaoPrincipal("Nova Pergunta"),
            criarBotaoNeutro("Editar Selecionada")
        );

        JButton novaPerguntaButton = (JButton) acoes.getComponent(0);
        JButton editarButton = (JButton) acoes.getComponent(1);
        novaPerguntaButton.addActionListener(evt -> Navegador.abrirTela(this, new PerguntaFormTela(null)));
        editarButton.addActionListener(evt -> editarPerguntaSelecionada());

        conteudo.add(criarScroll(perguntasTable), BorderLayout.CENTER);
        conteudo.add(acoes, BorderLayout.SOUTH);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void editarPerguntaSelecionada() {
        int linhaSelecionada = perguntasTable.getSelectedRow();
        if (linhaSelecionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecione uma pergunta da lista para editar.", "QuimLab", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        Navegador.abrirTela(this, new PerguntaFormTela(perguntas.get(linhaSelecionada)));
    }
}
