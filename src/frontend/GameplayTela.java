package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.PerguntaMock;
import frontend.util.Navegador;

public class GameplayTela extends TelaBase {

    private final String tipoUsuario;
    private final List<PerguntaMock> perguntas;
    private int indicePergunta;
    private int pontuacao;
    private int acertos;
    private boolean ajudaUsada;

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
        this.perguntas = DadosMockados.getPerguntasGameplay();
        initComponents();
        carregarPergunta();
    }

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
        dificuldadeLabel = criarTexto("Dificuldade: Facil");
        dificuldadeLabel.setForeground(COR_VERDE.darker());
        dificuldadeLabel.setFont(dificuldadeLabel.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        progressoLabel = criarTextoSuave("Pergunta 1 de 3");
        progressoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topo.add(dificuldadeLabel, BorderLayout.WEST);
        topo.add(progressoLabel, BorderLayout.EAST);

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));

        perguntaLabel = criarTituloHero("Pergunta");
        perguntaLabel.setFont(perguntaLabel.getFont().deriveFont(40f));
        perguntaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imagemLabel = criarPlaceholder("Imagem do material de laboratorio");
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

        JButton proximaButton = criarBotaoPrincipal("Responder");
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
        radio.setBorder(new RoundedLineBorder(new Color(198, 210, 219), 1, 30, 18));
        radio.setFocusPainted(false);
        radio.setHorizontalAlignment(SwingConstants.CENTER);
        radio.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
        radio.setPreferredSize(new Dimension(0, 128));
        return radio;
    }

    private void carregarPergunta() {
        PerguntaMock perguntaAtual = perguntas.get(indicePergunta);
        dificuldadeLabel.setText("Dificuldade: " + formatarTexto(perguntaAtual.getDificuldade()));
        progressoLabel.setText("Pergunta " + (indicePergunta + 1) + " de " + perguntas.size() + " | Pontos " + pontuacao);
        perguntaLabel.setText("<html><div style='text-align:center; width:980px;'>" + perguntaAtual.getEnunciado() + "</div></html>");
        imagemLabel.setText("<html><div style='text-align:center; padding-top:60px;'>" + perguntaAtual.getImagemDescricao() + "</div></html>");

        List<String> alternativas = perguntaAtual.getAlternativas();
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            alternativasRadioButtons[i].setText(alternativas.get(i));
            alternativasRadioButtons[i].setEnabled(true);
            alternativasRadioButtons[i].setBackground(new Color(241, 246, 250));
            alternativasRadioButtons[i].setForeground(COR_AZUL_ESCURO);
        }

        ajudaUsada = false;
        ajudaButton.setEnabled(true);
        alternativasButtonGroup.clearSelection();
    }

    private void usarAjuda() {
        if (ajudaUsada) {
            return;
        }

        PerguntaMock perguntaAtual = perguntas.get(indicePergunta);
        int removidas = 0;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (i != perguntaAtual.getCorreta() && removidas < 2) {
                alternativasRadioButtons[i].setEnabled(false);
                alternativasRadioButtons[i].setBackground(new Color(220, 226, 232));
                alternativasRadioButtons[i].setForeground(COR_TEXTO_SUAVE);
                removidas++;
            }
        }

        ajudaUsada = true;
        ajudaButton.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Ajuda usada: duas alternativas incorretas foram desativadas.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Escolha uma alternativa para continuar.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PerguntaMock perguntaAtual = perguntas.get(indicePergunta);
        if (indiceSelecionado == perguntaAtual.getCorreta()) {
            pontuacao += 100;
            acertos++;
        }

        indicePergunta++;
        if (indicePergunta >= perguntas.size()) {
            Navegador.abrirTela(this, new ResultadoTela(tipoUsuario, pontuacao, acertos, perguntas.size()));
            return;
        }

        carregarPergunta();
    }

    private String formatarTexto(String texto) {
        String minusculo = texto.toLowerCase();
        return Character.toUpperCase(minusculo.charAt(0)) + minusculo.substring(1);
    }
}
