package frontend;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.perguntaDAO.*;
import backend.DAO.alternativasDAO.*;

public class GameplayTela extends TelaBase {

    private final String tipoUsuario;
    private final PerguntaDAO perguntaDAO = new PerguntaDAO();
    private final AlternativasDAO alternativasDAO = new AlternativasDAO();
    
    private List<Pergunta> perguntas;
    private List<Alternativa> alternativasAtuais;
    private int indicePergunta = 0;
    private int pontuacao = 0;
    private int acertos = 0;

    private JLabel dificuldadeLabel;
    private JLabel progressoLabel;
    private JLabel perguntaLabel;
    private JLabel imagemLabel;
    private JRadioButton[] alternativasRadioButtons;
    private ButtonGroup alternativasButtonGroup;
    private JButton ajudaButton;

    public GameplayTela(String tipoUsuario, String modoTela) {
        super("QuimLab - Gameplay");
        this.tipoUsuario = tipoUsuario;
        
        // Passo 1: Selecionar Dificuldade antes de iniciar
        String dificuldadeSelecionada = selecionarDificuldade();
        
        // Passo 2: Puxar do Banco Real
        this.perguntas = perguntaDAO.getPerguntasPorDificuldade(dificuldadeSelecionada);
        
        if (this.perguntas == null || this.perguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há perguntas para esta dificuldade no banco.");
            Navegador.abrirHome(null, tipoUsuario); // Retorna se estiver vazio
            return;
        }

        initComponents();
        carregarPergunta();
    }

    private String selecionarDificuldade() {
        Object[] opcoes = {"FACIL", "MEDIO", "DIFICIL"};
        Object selecao = JOptionPane.showInputDialog(null, 
                "Escolha o nível do desafio:", "QuimLab - Seleção",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        return (selecao != null) ? selecao.toString() : "FACIL";
    }

    private void carregarPergunta() {
        Pergunta perguntaAtual = perguntas.get(indicePergunta);
        
        // Busca as alternativas reais vinculadas ao ID da pergunta
        this.alternativasAtuais = alternativasDAO.getAlternativasPorPergunta(perguntaAtual.getId());

        dificuldadeLabel.setText("Dificuldade: " + formatarTexto(perguntaAtual.getDificuldade()));
        progressoLabel.setText("Pergunta " + (indicePergunta + 1) + " de " + perguntas.size() + " | Pontos " + pontuacao);
        perguntaLabel.setText("<html><div style='text-align:center; width:980px;'>" + perguntaAtual.getEnunciado() + "</div></html>");
        
        // Por enquanto, apenas texto no placeholder da imagem
        imagemLabel.setText("<html><div style='text-align:center; padding-top:60px;'>Ilustração: " + 
                            (perguntaAtual.getImagemURL() != null ? perguntaAtual.getImagemURL() : "Laboratório") + "</div></html>");

        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (i < alternativasAtuais.size()) {
                alternativasRadioButtons[i].setText(alternativasAtuais.get(i).getTexto());
                alternativasRadioButtons[i].setVisible(true);
                alternativasRadioButtons[i].setEnabled(true);
            } else {
                alternativasRadioButtons[i].setVisible(false);
            }
            alternativasRadioButtons[i].setBackground(new Color(241, 246, 250));
            alternativasRadioButtons[i].setForeground(COR_AZUL_ESCURO);
        }

        ajudaButton.setEnabled(true);
        alternativasButtonGroup.clearSelection();
    }

    private void avancarPergunta() {
        int indiceSelecionado = -1;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasRadioButtons[i].isSelected()) {
                indiceSelecionado = i;
                break;
            }
        }

        if (indiceSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Escolha uma alternativa!", "QuimLab", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lógica de verificação usando o campo 'correta' (1 ou 0) do seu banco
        if (alternativasAtuais.get(indiceSelecionado).getCorreta() == 1) {
            pontuacao += 100;
            acertos++;
            JOptionPane.showMessageDialog(this, "Acertou! +100 pontos.");
        } else {
            JOptionPane.showMessageDialog(this, "Errado!");
        }

        indicePergunta++;
        if (indicePergunta >= perguntas.size()) {
            Navegador.abrirTela(this, new ResultadoTela(tipoUsuario, pontuacao, acertos, perguntas.size()));
            return;
        }

        carregarPergunta();
    }

    // --- MÉTODOS DE LAYOUT (MANTIDOS IGUAIS AO SEU ORIGINAL) ---

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(28, 70, 28, 70));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(18, 20));
        conteudo.setOpaque(false);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        dificuldadeLabel = criarTexto("Dificuldade: ");
        dificuldadeLabel.setForeground(COR_VERDE.darker());
        dificuldadeLabel.setFont(dificuldadeLabel.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        progressoLabel = criarTextoSuave("");
        progressoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topo.add(dificuldadeLabel, BorderLayout.WEST);
        topo.add(progressoLabel, BorderLayout.EAST);

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));

        perguntaLabel = criarTituloHero("Carregando...");
        perguntaLabel.setFont(perguntaLabel.getFont().deriveFont(32f)); // Ajustei levemente o tamanho
        perguntaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imagemLabel = criarPlaceholder("Imagem");
        imagemLabel.setPreferredSize(new Dimension(320, 220));
        imagemLabel.setMaximumSize(new Dimension(320, 220));
        imagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel alternativasPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        alternativasPanel.setOpaque(false);
        alternativasButtonGroup = new ButtonGroup();
        alternativasRadioButtons = new JRadioButton[4];
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            alternativasRadioButtons[i] = criarOpcaoResposta();
            alternativasButtonGroup.add(alternativasRadioButtons[i]);
            alternativasPanel.add(alternativasRadioButtons[i]);
        }

        corpo.add(perguntaLabel);
        corpo.add(Box.createVerticalStrut(16));
        corpo.add(imagemLabel);
        corpo.add(Box.createVerticalStrut(28));
        corpo.add(alternativasPanel);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        ajudaButton = criarBotaoLink("Ajuda");
        ajudaButton.addActionListener(evt -> usarAjuda());

        JButton sairButton = criarBotaoLink("Sair");
        sairButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        JButton proximaButton = criarBotaoPrincipal("Confirmar Resposta");
        proximaButton.setPreferredSize(new Dimension(320, 82));
        proximaButton.addActionListener(evt -> avancarPergunta());

        rodape.add(ajudaButton, BorderLayout.WEST);
        rodape.add(proximaButton, BorderLayout.CENTER);
        rodape.add(sairButton, BorderLayout.EAST);

        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(corpo, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JRadioButton criarOpcaoResposta() {
        JRadioButton radio = new JRadioButton();
        radio.setOpaque(true);
        radio.setBackground(new Color(241, 246, 250));
        radio.setForeground(COR_AZUL_ESCURO);
        radio.setFocusPainted(false);
        radio.setHorizontalAlignment(SwingConstants.CENTER);
        radio.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        radio.setPreferredSize(new Dimension(0, 100));
        return radio;
    }

    private void usarAjuda() {
        int removidas = 0;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasAtuais.get(i).getCorreta() == 0 && removidas < 2) {
                alternativasRadioButtons[i].setEnabled(false);
                alternativasRadioButtons[i].setBackground(new Color(220, 226, 232));
                removidas++;
            }
        }
        ajudaButton.setEnabled(false);
    }

    private String formatarTexto(String texto) {
        if (texto == null) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}