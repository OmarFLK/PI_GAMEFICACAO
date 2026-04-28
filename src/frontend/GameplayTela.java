package frontend;

import java.awt.*;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

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
    private JButton proximaButton;

    // Cores para o feedback visual
    private final Color COR_BORDA_PADRAO = new Color(200, 210, 220);
    private final Color COR_FUNDO_PADRAO = new Color(241, 246, 250);
    private final Color COR_FUNDO_SELECIONADO = new Color(225, 235, 245);

    public GameplayTela(String tipoUsuario, String modoTela) {
        super("QuimLab - Gameplay");
        this.tipoUsuario = tipoUsuario;
        
        String dificuldadeSelecionada = selecionarDificuldade();
        
        this.perguntas = perguntaDAO.getPerguntasPorDificuldade(dificuldadeSelecionada);
        
        if (this.perguntas == null || this.perguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há perguntas para esta dificuldade no banco.");
            Navegador.abrirHome(null, tipoUsuario);
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

        perguntaLabel = criarTituloHero("Carregando pergunta...");
        perguntaLabel.setFont(perguntaLabel.getFont().deriveFont(28f));
        perguntaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imagemLabel = new JLabel();
        imagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagemLabel.setVisible(false);

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

        proximaButton = criarBotaoPrincipal("Confirmar Resposta");
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
        radio.setBackground(COR_FUNDO_PADRAO);
        radio.setFocusPainted(false);
        radio.setHorizontalAlignment(SwingConstants.CENTER);
        radio.setBorder(BorderFactory.createLineBorder(COR_BORDA_PADRAO, 2));
        radio.setBorderPainted(true);
        radio.setPreferredSize(new Dimension(0, 120));

        // Listener para feedback de clique
        radio.addActionListener(e -> {
            for (JRadioButton rb : alternativasRadioButtons) {
                rb.setBorder(BorderFactory.createLineBorder(COR_BORDA_PADRAO, 2));
                rb.setBackground(COR_FUNDO_PADRAO);
            }
            if (radio.isSelected()) {
                radio.setBorder(BorderFactory.createLineBorder(COR_AZUL_ESCURO, 4));
                radio.setBackground(COR_FUNDO_SELECIONADO);
            }
        });

        return radio;
    }

    private void carregarPergunta() {
        proximaButton.setEnabled(false);
        ajudaButton.setEnabled(false);
        perguntaLabel.setText("<html><div style='text-align:center; width:980px;'>Baixando questão e imagens...</div></html>");
        imagemLabel.setVisible(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Pergunta perguntaAtual;
            ImageIcon imgPergunta = null;
            ImageIcon[] imgsAlternativas = new ImageIcon[4];

            @Override
            protected Void doInBackground() throws Exception {
                perguntaAtual = perguntas.get(indicePergunta);
                alternativasAtuais = alternativasDAO.getAlternativasPorPergunta(perguntaAtual.getId());

                if (perguntaAtual.getImagemURL() != null && !perguntaAtual.getImagemURL().isEmpty()) {
                    imgPergunta = baixarEScalarImagem(perguntaAtual.getImagemURL(), 400, 250);
                }

                for (int i = 0; i < alternativasAtuais.size() && i < 4; i++) {
                    String urlAlt = alternativasAtuais.get(i).getImagemURL();
                    if (urlAlt != null && !urlAlt.isEmpty()) {
                        imgsAlternativas[i] = baixarEScalarImagem(urlAlt, 110, 110);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                dificuldadeLabel.setText("Dificuldade: " + formatarTexto(perguntaAtual.getDificuldade()));
                progressoLabel.setText("Pergunta " + (indicePergunta + 1) + " de " + perguntas.size() + " | Pontos " + pontuacao);
                perguntaLabel.setText("<html><div style='text-align:center; width:980px;'>" + perguntaAtual.getEnunciado() + "</div></html>");

                if (imgPergunta != null) {
                    imagemLabel.setIcon(imgPergunta);
                    imagemLabel.setVisible(true);
                }

                for (int i = 0; i < alternativasRadioButtons.length; i++) {
                    if (i < alternativasAtuais.size()) {
                        JRadioButton rb = alternativasRadioButtons[i];
                        rb.setBorder(BorderFactory.createLineBorder(COR_BORDA_PADRAO, 2));
                        rb.setBackground(COR_FUNDO_PADRAO);

                        if (imgsAlternativas[i] != null) {
                            rb.setText("");
                            rb.setIcon(imgsAlternativas[i]);
                        } else {
                            rb.setText(alternativasAtuais.get(i).getTexto());
                            rb.setIcon(null);
                        }
                        rb.setVisible(true);
                        rb.setEnabled(true);
                    } else {
                        alternativasRadioButtons[i].setVisible(false);
                    }
                }
                alternativasButtonGroup.clearSelection();
                proximaButton.setEnabled(true);
                ajudaButton.setEnabled(true);
            }
        };
        worker.execute();
    }

    private ImageIcon baixarEScalarImagem(String urlStr, int width, int height) {
        try {
            URL url = new URL(urlStr);
            Image img = ImageIO.read(url);
            if (img != null) {
                return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) { System.err.println("Erro imagem: " + urlStr); }
        return null;
    }

    private void avancarPergunta() {
        int sel = -1;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasRadioButtons[i].isSelected()) { sel = i; break; }
        }

        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma opção!");
            return;
        }

        if (alternativasAtuais.get(sel).getCorreta() == 1) {
            pontuacao += 100; acertos++;
            JOptionPane.showMessageDialog(this, "Correto! ✨");
        } else {
            JOptionPane.showMessageDialog(this, "Errado! ❌");
        }

        indicePergunta++;
        if (indicePergunta >= perguntas.size()) {
            Navegador.abrirTela(this, new ResultadoTela(tipoUsuario, pontuacao, acertos, perguntas.size()));
        } else {
            carregarPergunta();
        }
    }

    private void usarAjuda() {
        int r = 0;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasAtuais.get(i).getCorreta() == 0 && r < 2) {
                alternativasRadioButtons[i].setEnabled(false);
                alternativasRadioButtons[i].setBackground(new Color(220, 226, 232));
                r++;
            }
        }
        ajudaButton.setEnabled(false);
    }

    private String formatarTexto(String t) {
        if (t == null) return "";
        return t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase();
    }
}