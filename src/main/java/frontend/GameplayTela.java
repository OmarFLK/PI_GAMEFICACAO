package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import backend.DAO.alternativasDAO.Alternativa;
import backend.DAO.alternativasDAO.AlternativasDAO;
import backend.DAO.partidaDAO.PartidaDAO;
import backend.DAO.perguntaDAO.Pergunta;
import backend.DAO.perguntaDAO.PerguntaDAO;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemePalette;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class GameplayTela extends TelaBase {

    private final String tipoUsuario;
    private final PerguntaDAO perguntaDAO = new PerguntaDAO();
    private final AlternativasDAO alternativasDAO = new AlternativasDAO();

    private final List<Pergunta> perguntas;
    private List<Alternativa> alternativasAtuais;
    private int indicePergunta = 0;
    private int pontuacao = 0;
    private int acertos = 0;
    private boolean dicaExiste;

    private JLabel dificuldadeLabel;
    private final String dificuldadeSelecionada;
    private JLabel progressoLabel;
    private JLabel perguntaLabel;
    private JLabel dicaLabel;
    private JLabel imagemLabel;
    private JLabel erroLabel;
    private JRadioButton[] alternativasRadioButtons;
    private ButtonGroup alternativasButtonGroup;
    public JButton ajudaButton;
    private JButton proximaButton;
    private AjudaModal ajudaModal = new AjudaModal(this, dicaExiste);

    public GameplayTela(String tipoUsuario, String modoTela) {
        super("QuimLab - Gameplay");
        this.tipoUsuario = tipoUsuario;

        this.dificuldadeSelecionada = (modoTela != null) ? modoTela : "FACIL";

        if (dificuldadeSelecionada.equalsIgnoreCase("PROGRESSIVO")) {
            this.perguntas = perguntaDAO.getPerguntasPorDificuldade("FACIL", 3);
            this.perguntas.addAll(perguntaDAO.getPerguntasPorDificuldade("MEDIO", 3));
            this.perguntas.addAll(perguntaDAO.getPerguntasPorDificuldade("DIFICIL", 4));
        } else {
            this.perguntas = perguntaDAO.getPerguntasPorDificuldade(dificuldadeSelecionada, 10);
        }

        if (this.perguntas == null || this.perguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há perguntas para esta dificuldade no banco.");
            SwingUtilities.invokeLater(() -> {
                Navegador.abrirHome(this, tipoUsuario);
            });
            return;
        }

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
        dificuldadeLabel = criarTexto("Dificuldade: ");
        dificuldadeLabel.setForeground(AppTheme.NEUTRAL_DARK);
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

        dicaLabel = criarTextoCentral(" ");

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

        erroLabel = new JLabel("Por favor, selecione uma opção antes de confirmar.");
        erroLabel.setForeground(Color.RED);
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        erroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        erroLabel.setVisible(false);

        corpo.add(perguntaLabel);
        corpo.add(Box.createVerticalStrut(16));
        corpo.add(dicaLabel);
        corpo.add(Box.createVerticalStrut(16));
        corpo.add(imagemLabel);
        corpo.add(Box.createVerticalStrut(28));
        corpo.add(alternativasPanel);
        corpo.add(Box.createVerticalStrut(15));
        corpo.add(erroLabel);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        ajudaButton = criarBotaoLink("Ajuda");
        ajudaButton.addActionListener(evt -> usarAjuda());

        JButton sairButton = criarBotaoLink("Sair");
        sairButton.addActionListener(evt -> {
            SairModal modal = new SairModal(this);
            modal.setVisible(true);
            if (modal.isConfirmarSair()) {
                Navegador.abrirHome(this, tipoUsuario);
            }
        });

        JPanel containerConfirmar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerConfirmar.setOpaque(false);

        this.proximaButton = criarBotaoPrincipal("Confirmar Resposta");
        this.proximaButton.setPreferredSize(new Dimension(420, 70));
        this.proximaButton.addActionListener(evt -> avancarPergunta());

        containerConfirmar.add(this.proximaButton);

        rodape.add(ajudaButton, BorderLayout.WEST);
        rodape.add(containerConfirmar, BorderLayout.CENTER);
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
        radio.setBackground(corFundoPadrao());
        radio.setFocusPainted(false);
        radio.setHorizontalAlignment(SwingConstants.CENTER);
        radio.setBorder(BorderFactory.createLineBorder(corBordaPadrao(), 2));
        radio.setBorderPainted(true);
        radio.setPreferredSize(new Dimension(0, 120));

        radio.addActionListener(e -> {
            erroLabel.setVisible(false);
            for (JRadioButton rb : alternativasRadioButtons) {
                rb.setBorder(BorderFactory.createLineBorder(corBordaPadrao(), 2));
                rb.setBackground(corFundoPadrao());
            }
            if (radio.isSelected()) {
                radio.setBorder(BorderFactory.createLineBorder(ThemeManager.getCurrentPalette().primaryRed(), 4));
                radio.setBackground(corFundoSelecionado());
            }
        });
        return radio;
    }

    private void carregarPergunta() {
        proximaButton.setEnabled(false);
        ajudaButton.setEnabled(false);
        erroLabel.setVisible(false);
        perguntaLabel.setText(
                "<html><div style='text-align:center; width:850px;'>Baixando questão e imagens...</div></html>");
        imagemLabel.setVisible(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Pergunta perguntaSendoCarregada;
            ImageIcon imgPergunta = null;
            ImageIcon[] imgsAlternativas = new ImageIcon[4];
            @Override
            protected Void doInBackground() throws Exception {
                perguntaSendoCarregada = perguntas.get(indicePergunta);
                alternativasAtuais = alternativasDAO.getAlternativasPorPergunta(perguntaSendoCarregada.getId());

                if (perguntaSendoCarregada.getImagemURL() != null && !perguntaSendoCarregada.getImagemURL().isEmpty()) {
                    imgPergunta = baixarEscalarImagem(perguntaSendoCarregada.getImagemURL(), 400, 250);
                }

                for (int i = 0; i < alternativasAtuais.size() && i < 4; i++) {
                    String urlAlt = alternativasAtuais.get(i).getImagemURL();
                    if (urlAlt != null && !urlAlt.isEmpty()) {
                        imgsAlternativas[i] = baixarEscalarImagem(urlAlt, 110, 110);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                dificuldadeLabel.setText("Dificuldade: " + formatarTexto(perguntaSendoCarregada.getDificuldade()));
                progressoLabel.setText(
                        "Pergunta " + (indicePergunta + 1) + " de " + perguntas.size() + " | Pontos " + pontuacao);

                perguntaLabel.setText("<html><body style='width: 850px; text-align: center;'>" +
                        perguntaSendoCarregada.getEnunciado() +
                        "</body></html>");

                if (imgPergunta != null) {
                    imagemLabel.setIcon(imgPergunta);
                    imagemLabel.setVisible(true);
                }
                
                Pergunta perguntaAtual = perguntas.get(indicePergunta);
                dicaExiste = (perguntaAtual.getAjuda() != null && !perguntaAtual.getAjuda().trim().isEmpty());
                if (ajudaModal != null){
                    ajudaModal.atualizarVisibilidadeDica(dicaExiste);
                }
                dicaLabel.setVisible(false);
                dicaLabel.setForeground(ThemeManager.getCurrentPalette().primaryRed());
                dicaLabel.setText("<html><body style='width: 850px; text-align: center;'>" +
                            perguntaSendoCarregada.getAjuda() +
                            "</body></html>");
                for (int i = 0; i < alternativasRadioButtons.length; i++) {
                    if (i < alternativasAtuais.size()) {
                        JRadioButton rb = alternativasRadioButtons[i];
                        rb.setBorder(BorderFactory.createLineBorder(corBordaPadrao(), 2));
                        rb.setBackground(corFundoPadrao());
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

    private ImageIcon baixarEscalarImagem(String urlStr, int width, int height) {
        try {
            Image img;
            if (urlStr.startsWith("data:image")) {
                String base64Image = urlStr.split(",")[1];
                byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
                img = ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
            } else {
                URL url = java.net.URI.create(urlStr).toURL();
                img = ImageIO.read(url);
            }

            if (img != null) {
                return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            }
        } catch (IOException e) {
            System.err.println("Erro imagem: " + urlStr);
        }
        return null;
    }

    private void avancarPergunta() {
        int sel = -1;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasRadioButtons[i].isSelected()) {
                sel = i;
                break;
            }
        }

        if (sel == -1) {
            erroLabel.setVisible(true);
            this.revalidate();
            this.repaint();

            Timer timerErro = new Timer(3000, e -> {
                erroLabel.setVisible(false);
                this.revalidate();
                this.repaint();
            });
            timerErro.setRepeats(false);
            timerErro.start();
            return;
        }

        proximaButton.setEnabled(false);
        for (JRadioButton rb : alternativasRadioButtons) {
            rb.setEnabled(false);
        }

        boolean correta = alternativasAtuais.get(sel).getCorreta() == 1;
        ThemePalette palette = ThemeManager.getCurrentPalette();

        if (correta) {
            Pergunta pergunta = perguntas.get(indicePergunta);

            if (pergunta.getDificuldade().equalsIgnoreCase("DIFICIL")) {
                pontuacao += 200;
            } else if (pergunta.getDificuldade().equalsIgnoreCase("MEDIO")) {
                pontuacao += 100;
            } else {
                pontuacao += 50;
            }

            acertos++;
            alternativasRadioButtons[sel].setBackground(palette.successSurface());
            alternativasRadioButtons[sel].setBorder(BorderFactory.createLineBorder(palette.success(), 4));
        } else {
            alternativasRadioButtons[sel].setBackground(palette.dangerSurface());
            alternativasRadioButtons[sel].setBorder(BorderFactory.createLineBorder(palette.dangerRed(), 4));

            for (int i = 0; i < alternativasAtuais.size(); i++) {
                if (alternativasAtuais.get(i).getCorreta() == 1) {
                    alternativasRadioButtons[i]
                            .setBorder(BorderFactory.createLineBorder(palette.success(), 2));
                }
            }
            if (dificuldadeSelecionada.equalsIgnoreCase("PROGRESSIVO")) {
                new PerdeuModal(this).setVisible(true);
                Navegador.abrirHome(this, tipoUsuario);
            }
        }

        javax.swing.Timer timerFeedback = new javax.swing.Timer(800, e -> {
            indicePergunta++;
            if (indicePergunta >= perguntas.size()) {
                // LOGICA DE SALVAMENTO ATUALIZADA COM OS NOVOS PARAMETROS DO BANCO MySQL
                backend.DAO.usuarioDAO.Usuario u = SessaoUsuario.getInstancia().getUsuario();
                if (u != null && Navegador.TIPO_ALUNO.equals(u.getTipo())) {
                    int totalErros = perguntas.size() - acertos;
                    new PartidaDAO().salvarResultadoFinal(u.getId(), pontuacao, acertos, totalErros);
                }

                Navegador.abrirTela(this, new ResultadoTela(tipoUsuario, pontuacao, acertos, perguntas.size()));
            } else {
                carregarPergunta();
            }
        });
        timerFeedback.setRepeats(false);
        timerFeedback.start();
    }

    private void usarAjuda() {
        ajudaModal.setVisible(true);
    }

    public void tirarDuasAlternativas() {
        int r = 0;
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasAtuais.get(i).getCorreta() == 0 && r < 2) {
                alternativasRadioButtons[i].setEnabled(false);
                alternativasRadioButtons[i].setBackground(ThemeManager.getCurrentPalette().mutedSurface());
                r++;
            }
        }
        ajudaModal.setVisible(false);
        ajudaButton.setEnabled(false);
    }

    public void mostrarDica() {
        dicaLabel.setVisible(true);
    }
    
    public boolean isDicaExiste() {
        return dicaExiste;
    }

    private String formatarTexto(String t) {
        if (t == null)
            return "";
        return t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase();
    }

    private Color corBordaPadrao() {
        return ThemeManager.getCurrentPalette().border();
    }

    private Color corFundoPadrao() {
        return ThemeManager.getCurrentPalette().inputBackground();
    }

    private Color corFundoSelecionado() {
        return ThemeManager.getCurrentPalette().selectionBackground();
    }
}
