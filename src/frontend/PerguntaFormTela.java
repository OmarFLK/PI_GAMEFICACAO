package frontend;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.perguntaDAO.*;
import backend.DAO.alternativasDAO.*;
import backend.Seguranca.SessaoUsuario;

public class PerguntaFormTela extends TelaBase {

    private final Pergunta perguntaExistente; 
    private final PerguntaDAO perguntaDAO = new PerguntaDAO();
    private final AlternativasDAO alternativasDAO = new AlternativasDAO();

    private JTextArea txtEnunciado;
    private JComboBox<String> cbDificuldade;
    private JTextField txtImagemPergunta;
    private JTextField[] txtAlternativas;
    private JTextField[] txtImagensAlternativas;
    private JRadioButton[] rbCorretas;
    private JButton btnSalvar;
    private JButton btnCancelar; // Adicionado explicitamente

    public PerguntaFormTela(Pergunta p) {
        super(p == null ? "QuimLab Pro - Nova Pergunta" : "QuimLab Pro - Editar Pergunta");
        this.perguntaExistente = p;
        initComponents();
        if (p != null) carregarDadosParaEdicao();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout(0, 20));
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JPanel canvas = criarCanvasCentral();
        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));

        // --- ENUNCIADO ---
        corpo.add(criarTexto("Enunciado da Questão:"));
        txtEnunciado = new JTextArea(3, 20);
        txtEnunciado.setLineWrap(true);
        txtEnunciado.setWrapStyleWord(true);
        JScrollPane scrollEnunciado = new JScrollPane(txtEnunciado);
        scrollEnunciado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        corpo.add(scrollEnunciado);

        corpo.add(Box.createVerticalStrut(10));

        // --- DIFICULDADE E IMAGEM ---
        JPanel painelMeio = new JPanel(new GridLayout(1, 2, 20, 0));
        painelMeio.setOpaque(false);

        JPanel pDificuldade = new JPanel(new BorderLayout());
        pDificuldade.setOpaque(false);
        pDificuldade.add(criarTexto("Dificuldade:"), BorderLayout.NORTH);
        cbDificuldade = new JComboBox<>(new String[]{"FACIL", "MEDIO", "DIFICIL"});
        pDificuldade.add(cbDificuldade, BorderLayout.CENTER);

        JPanel pImagem = new JPanel(new BorderLayout());
        pImagem.setOpaque(false);
        pImagem.add(criarTexto("URL da Imagem (Pergunta):"), BorderLayout.NORTH);
        txtImagemPergunta = new JTextField();
        pImagem.add(txtImagemPergunta, BorderLayout.CENTER);

        painelMeio.add(pDificuldade);
        painelMeio.add(pImagem);
        corpo.add(painelMeio);

        corpo.add(Box.createVerticalStrut(20));

        // --- ALTERNATIVAS ---
        corpo.add(criarTexto("Alternativas (Texto | URL da Imagem opcional):"));
        JPanel painelAlts = new JPanel(new GridLayout(4, 1, 0, 10));
        painelAlts.setOpaque(false);

        txtAlternativas = new JTextField[4];
        txtImagensAlternativas = new JTextField[4];
        rbCorretas = new JRadioButton[4];
        ButtonGroup grupoCorretas = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            JPanel linha = new JPanel(new BorderLayout(10, 0));
            linha.setOpaque(false);
            
            rbCorretas[i] = new JRadioButton();
            rbCorretas[i].setOpaque(false);
            grupoCorretas.add(rbCorretas[i]);
            
            txtAlternativas[i] = new JTextField();
            txtImagensAlternativas[i] = new JTextField();
            txtImagensAlternativas[i].setPreferredSize(new Dimension(200, 40));
            
            linha.add(rbCorretas[i], BorderLayout.WEST);
            linha.add(txtAlternativas[i], BorderLayout.CENTER);
            linha.add(txtImagensAlternativas[i], BorderLayout.EAST);
            painelAlts.add(linha);
        }
        corpo.add(painelAlts);

        // --- RODAPÉ: BOTÕES ---
        JPanel rodape = new JPanel(new BorderLayout()); // Mudei para BorderLayout para separar os botões
        rodape.setOpaque(false);

        btnCancelar = criarBotaoNeutro("CANCELAR E VOLTAR");
        btnCancelar.setPreferredSize(new Dimension(220, 50));
        btnCancelar.addActionListener(e -> acaoCancelar());

        btnSalvar = criarBotaoPrincipal("SALVAR PERGUNTA");
        btnSalvar.setPreferredSize(new Dimension(250, 60));
        btnSalvar.addActionListener(e -> salvar());

        rodape.add(btnCancelar, BorderLayout.WEST); // Cancelar na esquerda
        rodape.add(btnSalvar, BorderLayout.EAST);   // Salvar na direita

        canvas.add(corpo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelExterno.add(rodape, BorderLayout.SOUTH);
        painelPrincipal.add(painelExterno);
        setContentPane(painelPrincipal);
    }

    private void carregarDadosParaEdicao() {
        txtEnunciado.setText(perguntaExistente.getEnunciado());
        cbDificuldade.setSelectedItem(perguntaExistente.getDificuldade());
        txtImagemPergunta.setText(perguntaExistente.getImagemURL());
        
        List<Alternativa> alts = alternativasDAO.getAlternativasPorPergunta(perguntaExistente.getId());
        for (int i = 0; i < alts.size() && i < 4; i++) {
            txtAlternativas[i].setText(alts.get(i).getTexto());
            txtImagensAlternativas[i].setText(alts.get(i).getImagemURL());
            if (alts.get(i).getCorreta() == 1) rbCorretas[i].setSelected(true);
        }
    }

    private void acaoCancelar() {
        int op = JOptionPane.showConfirmDialog(this, "Deseja descartar as alterações e voltar?", "QuimLab", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            Navegador.abrirTela(this, new GerenciarPerguntasTela());
        }
    }

    private void salvar() {
        String enunciado = txtEnunciado.getText().trim();
        String dificuldade = cbDificuldade.getSelectedItem().toString();
        String imgPergunta = tratarNull(txtImagemPergunta.getText());
        int professorId = SessaoUsuario.getInstancia().getUsuario().getId();

        if (enunciado.isEmpty() || !validarSelecaoCorreta()) {
            JOptionPane.showMessageDialog(this, "Erro: Preencha o enunciado e marque a alternativa correta.");
            return;
        }

        // Feedback de carregamento
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false); // Desativa o cancelar também para não dar conflito
        btnSalvar.setText("Processando...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (perguntaExistente == null) {
                    perguntaDAO.criarPergunta(enunciado, imgPergunta, dificuldade, professorId);
                    int idNova = buscarUltimoIdCriado(professorId);
                    for (int i = 0; i < 4; i++) {
                        alternativasDAO.criarAlternativa(idNova, txtAlternativas[i].getText().trim(), tratarNull(txtImagensAlternativas[i].getText()), rbCorretas[i].isSelected() ? 1 : 0);
                    }
                } else {
                    perguntaDAO.atualizarPergunta(perguntaExistente.getId(), enunciado, imgPergunta, dificuldade, 1);
                    List<Alternativa> antigas = alternativasDAO.getAlternativasPorPergunta(perguntaExistente.getId());
                    for (Alternativa a : antigas) alternativasDAO.deletarAlternativa(a.getIdAlternativa());
                    for (int i = 0; i < 4; i++) {
                        alternativasDAO.criarAlternativa(perguntaExistente.getId(), txtAlternativas[i].getText().trim(), tratarNull(txtImagensAlternativas[i].getText()), rbCorretas[i].isSelected() ? 1 : 0);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(PerguntaFormTela.this, "Pergunta salva com sucesso!");
                    Navegador.abrirTela(PerguntaFormTela.this, new GerenciarPerguntasTela());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PerguntaFormTela.this, "Erro ao conectar com o servidor: " + e.getMessage());
                    btnSalvar.setEnabled(true);
                    btnCancelar.setEnabled(true);
                    btnSalvar.setText("SALVAR PERGUNTA");
                }
            }
        };
        worker.execute();
    }

    private String tratarNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private boolean validarSelecaoCorreta() {
        for (JRadioButton rb : rbCorretas) if (rb.isSelected()) return true;
        return false;
    }

    private int buscarUltimoIdCriado(int profId) {
        return perguntaDAO.getTodasPerguntas().stream()
                .filter(p -> p.getCriadoPor() == profId)
                .mapToInt(Pergunta::getId).max().orElse(0);
    }
}