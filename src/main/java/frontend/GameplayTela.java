package frontend;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
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
import javax.swing.JScrollPane;
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
    private boolean modoTextoAtual = false;

    private JLabel difficultyLabel;
    private final String dificuldadeSelecionada;
    private JLabel progressoLabel;
    private JLabel perguntaLabel;
    private JLabel dicaLabel;
    private JLabel imagemLabel;
    private JLabel erroLabel;
    private JPanel alternativasPanel;
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
        difficultyLabel = criarTexto("Dificuldade: ");
        difficultyLabel.setForeground(AppTheme.NEUTRAL_DARK);
        difficultyLabel.setFont(difficultyLabel.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        progressoLabel = criarTextoSuave("");
        progressoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topo.add(difficultyLabel, BorderLayout.WEST);
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

        alternativasPanel = new JPanel(new GridLayout(2, 2, 20, 20));
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

        // Envelopamento minimalista com scrollbar invisível
        JScrollPane scrollCorpo = new JScrollPane(corpo);
        scrollCorpo.setBorder(BorderFactory.createEmptyBorder());
        scrollCorpo.setOpaque(false);
        scrollCorpo.getViewport().setOpaque(false);
        scrollCorpo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCorpo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollCorpo.getVerticalScrollBar().setUnitIncrement(16);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // --- Ajuda: botão de verdade ---
        ajudaButton = criarBotaoLink("Ajuda");
        estilizarBotaoRodape(ajudaButton);
        ajudaButton.addActionListener(evt -> usarAjuda());

        // --- Sair: botão de verdade ---
        JButton sairButton = criarBotaoLink("Sair");
        estilizarBotaoRodape(sairButton);
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

        // Wrappers com FlowLayout para os botões laterais não esticarem
        JPanel wrapAjuda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapAjuda.setOpaque(false);
        wrapAjuda.add(ajudaButton);

        JPanel wrapSair = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        wrapSair.setOpaque(false);
        wrapSair.add(sairButton);

        rodape.add(wrapAjuda, BorderLayout.WEST);
        rodape.add(containerConfirmar, BorderLayout.CENTER);
        rodape.add(wrapSair, BorderLayout.EAST);

        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(scrollCorpo, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    /**
     * Transforma um botão-link em um botão visual de verdade, com borda,
     * fundo e padding, compatível com o ThemeManager.
     */
    private void estilizarBotaoRodape(JButton btn) {
        ThemePalette palette = ThemeManager.getCurrentPalette();
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBackground(palette.neutralButtonBackground());
        btn.setForeground(palette.textPrimary());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(palette.border(), 1),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 44));
    }

    /**
     * Cria uma borda para as alternativas que compensa o padding interno conforme
     * a espessura, evitando que o texto se mova ao selecionar (2px -> 4px).
     * No modo texto usa CompoundBorder; no modo imagem usa LineBorder simples.
     */
    private javax.swing.border.Border criarBordaAlternativa(Color cor, int espessura) {
        if (modoTextoAtual) {
            // Padding total fixo = 14px. Borda 2px -> inner 12px. Borda 4px -> inner 10px.
            int inner = 14 - espessura;
            return BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(cor, espessura),
                    BorderFactory.createEmptyBorder(inner, inner, inner, inner));
        }
        return BorderFactory.createLineBorder(cor, espessura);
    }

    private JRadioButton criarOpcaoResposta() {
        // Sobrescrita do paintComponent para renderizar imagem e sobrepor X nas descartadas
        JRadioButton radio = new JRadioButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                Image imgRaw = (Image) getClientProperty("IMAGEM_RAW");
                if (imgRaw != null) {
                    int btnW = getWidth();
                    int btnH = getHeight();
                    int imgW = imgRaw.getWidth(null);
                    int imgH = imgRaw.getHeight(null);

                    if (imgW > 0 && imgH > 0) {
                        // Padding para a imagem não colidir com as bordas
                        int padding = 16;
                        int maxW = btnW - (padding * 2);
                        int maxH = btnH - (padding * 2);

                        double scale = Math.min((double) maxW / imgW, (double) maxH / imgH);
                        int targetW = (int) (imgW * scale);
                        int targetH = (int) (imgH * scale);

                        int x = (btnW - targetW) / 2;
                        int y = (btnH - targetH) / 2;

                        g2.drawImage(imgRaw, x, y, targetW, targetH, null);
                    }
                }

                // Overlay X vermelho para alternativas com imagem descartadas
                Boolean descartada = (Boolean) getClientProperty("DESCARTADA");
                if (descartada != null && descartada && imgRaw != null) {
                    int w = getWidth();
                    int h = getHeight();
                    // Véu escuro semi-transparente
                    g2.setColor(new Color(0, 0, 0, 110));
                    g2.fillRect(0, 0, w, h);
                    // X vermelho em destaque
                    int margin = 18;
                    g2.setColor(new Color(255, 70, 70, 220));
                    g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(margin, margin, w - margin, h - margin);
                    g2.drawLine(w - margin, margin, margin, h - margin);
                }

                g2.dispose();
            }
        };
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
                // Só reseta visual das que não estão descartadas
                Boolean descartada = (Boolean) rb.getClientProperty("DESCARTADA");
                if (descartada == null || !descartada) {
                    rb.setBorder(criarBordaAlternativa(corBordaPadrao(), 2));
                    rb.setBackground(corFundoPadrao());
                }
            }
            if (radio.isSelected()) {
                radio.setBorder(criarBordaAlternativa(ThemeManager.getCurrentPalette().primaryRed(), 4));
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

        // Reseta estados e limpa imagens/flags antigas para evitar fantasmas visuais
        if (alternativasRadioButtons != null) {
            for (JRadioButton rb : alternativasRadioButtons) {
                if (rb != null) {
                    rb.putClientProperty("IMAGEM_RAW", null);
                    rb.putClientProperty("DESCARTADA", null);
                    rb.setText("");
                    rb.setForeground(null);
                }
            }
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Pergunta perguntaSendoCarregada;
            ImageIcon imgPergunta = null;
            Image[] imgsAlternativas = new Image[4]; // Image pura para guardar em alta resolução

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
                        imgsAlternativas[i] = baixarImagemOriginal(urlAlt);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                difficultyLabel.setText("Dificuldade: " + formatarTexto(perguntaSendoCarregada.getDificuldade()));
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
                if (ajudaModal != null) {
                    ajudaModal.atualizarVisibilidadeDica(dicaExiste);
                }
                dicaLabel.setVisible(false);
                dicaLabel.setForeground(ThemeManager.getCurrentPalette().primaryRed());
                dicaLabel.setText("<html><body style='width: 850px; text-align: center;'>" +
                        perguntaSendoCarregada.getAjuda() +
                        "</body></html>");

                // Detecta se é modo texto puro: nenhuma das alternativas tem imagem
                boolean modoTexto = true;
                for (int i = 0; i < alternativasAtuais.size() && i < 4; i++) {
                    if (imgsAlternativas[i] != null) {
                        modoTexto = false;
                        break;
                    }
                }

                // Atualiza flag global para criarBordaAlternativa() saber o modo atual
                modoTextoAtual = modoTexto;

                // GridLayout(4,1) empilha verticalmente ocupando toda a largura disponível
                // GridLayout(2,2) mantém o grid original para alternativas com imagem
                if (modoTexto) {
                    alternativasPanel.setLayout(new GridLayout(4, 1, 0, 8));
                } else {
                    alternativasPanel.setLayout(new GridLayout(2, 2, 20, 20));
                }

                for (int i = 0; i < alternativasRadioButtons.length; i++) {
                    if (i < alternativasAtuais.size()) {
                        JRadioButton rb = alternativasRadioButtons[i];
                        rb.setBackground(corFundoPadrao());
                        if (ThemeManager.isDarkMode()) {
                            rb.setForeground(Color.WHITE);
                        } else {
                            rb.setForeground(Color.BLACK);
                        }

                        if (imgsAlternativas[i] != null) {
                            // Modo imagem: centralizado, altura fixa, sem padding extra
                            rb.setText("");
                            rb.putClientProperty("IMAGEM_RAW", imgsAlternativas[i]);
                            rb.setHorizontalAlignment(SwingConstants.CENTER);
                            rb.setPreferredSize(new Dimension(0, 120));
                        } else {
                            rb.putClientProperty("IMAGEM_RAW", null);
                            rb.setText(alternativasAtuais.get(i).getTexto());
                            if (modoTexto) {
                                // Modo texto: alinhado à esquerda, altura automática
                                rb.setHorizontalAlignment(SwingConstants.LEFT);
                                rb.setFont(rb.getFont().deriveFont(Font.PLAIN, 15f));
                                rb.setPreferredSize(null);
                            } else {
                                rb.setHorizontalAlignment(SwingConstants.CENTER);
                                rb.setPreferredSize(new Dimension(0, 120));
                            }
                        }

                        // Borda aplicada por último, já com modoTextoAtual definido
                        rb.setBorder(criarBordaAlternativa(corBordaPadrao(), 2));
                        rb.setVisible(true);
                        rb.setEnabled(true);
                    } else {
                        alternativasRadioButtons[i].setVisible(false);
                    }
                }

                alternativasPanel.revalidate();
                alternativasPanel.repaint();
                alternativasButtonGroup.clearSelection();
                proximaButton.setEnabled(true);
                ajudaButton.setEnabled(true);
            }
        };
        worker.execute();
    }

    private Image baixarImagemOriginal(String urlStr) {
        try {
            if (urlStr.startsWith("data:image")) {
                String base64Image = urlStr.split(",")[1];
                byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
                return ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
            } else {
                URL url = java.net.URI.create(urlStr).toURL();
                return ImageIO.read(url);
            }
        } catch (Exception e) {
            System.err.println("Erro imagem: " + urlStr + " | Motivo: " + e.getMessage());
        }
        return null;
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
        } catch (Exception e) {
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
            alternativasRadioButtons[sel].setBorder(criarBordaAlternativa(palette.success(), 4));
        } else {
            alternativasRadioButtons[sel].setBackground(palette.dangerSurface());
            alternativasRadioButtons[sel].setBorder(criarBordaAlternativa(palette.dangerRed(), 4));

            for (int i = 0; i < alternativasAtuais.size(); i++) {
                if (alternativasAtuais.get(i).getCorreta() == 1) {
                    alternativasRadioButtons[i].setBorder(criarBordaAlternativa(palette.success(), 2));
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
        ThemePalette palette = ThemeManager.getCurrentPalette();
        for (int i = 0; i < alternativasRadioButtons.length; i++) {
            if (alternativasAtuais.get(i).getCorreta() == 0 && r < 2) {
                JRadioButton rb = alternativasRadioButtons[i];
                rb.setEnabled(false);
                rb.putClientProperty("DESCARTADA", true);
                rb.setBackground(palette.mutedSurface());

                if (modoTextoAtual) {
                    // Modo texto: texto tachado + cor apagada via HTML
                    String texto = alternativasAtuais.get(i).getTexto();
                    rb.setText("<html><s>" + texto + "</s></html>");
                    rb.setForeground(palette.textSecondary());
                    // Borda com padding compensado (espessura 2, inner 12)
                    rb.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(palette.border(), 2),
                            BorderFactory.createEmptyBorder(12, 12, 12, 12)));
                } else {
                    // Modo imagem: o X é desenhado pelo paintComponent via flag DESCARTADA
                    rb.repaint();
                }
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