package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.util.Navegador;

public class PerfilTela extends TelaBase {

    protected final String tipoUsuario;
    private JLabel avatar; 

    public PerfilTela(String tipoUsuario) {
        super("QuimLab - Perfil");
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        Usuario usuario = SessaoUsuario.getInstancia().getUsuario();

        String nomeExibicao = (usuario != null && usuario.getNome() != null && !usuario.getNome().isBlank())
                ? usuario.getNome()
                : "Usuário";
        String emailExibicao = (usuario != null && usuario.getEmail() != null && !usuario.getEmail().isBlank())
                ? usuario.getEmail()
                : "---";

        String tituloTela = Navegador.TIPO_PROFESSOR.equals(tipoUsuario)
                ? "Informações do Professor"
                : "Perfil do Aluno";
        String tipoExibicao = Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Professor" : "Aluno";

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 30));
        conteudo.setOpaque(false);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setPreferredSize(new Dimension(0, 100));

        JLabel lbTituloHeader = new JLabel(tituloTela);
        int tamanhoTitulo = Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? 34 : 42;
        lbTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, tamanhoTitulo));
        lbTituloHeader.setForeground(COR_AZUL_ESCURO);
        lbTituloHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 58));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        JPanel painelBotaoVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotaoVoltar.setOpaque(false);
        painelBotaoVoltar.add(voltarButton);

        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(160, 58));

        cabecalho.add(placeholder, BorderLayout.WEST);
        cabecalho.add(lbTituloHeader, BorderLayout.CENTER);
        cabecalho.add(painelBotaoVoltar, BorderLayout.EAST);

        conteudo.add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(36, 0));
        corpo.setOpaque(false);

        JPanel avatarCard = criarCartaoSuave();
        avatarCard.setLayout(new BoxLayout(avatarCard, BoxLayout.Y_AXIS));
        avatarCard.setPreferredSize(new Dimension(340, 0));
        avatarCard.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));

        // --- NOVO AVATAR CUSTOMIZADO (CÍRCULO PERFEITO) ---
        avatar = new JLabel() {
            private Image imagemRedonda = null;

            @Override
            public void setIcon(javax.swing.Icon icon) {
                super.setIcon(null); 
                if (icon instanceof ImageIcon) {
                    this.imagemRedonda = ((ImageIcon) icon).getImage();
                } else {
                    this.imagemRedonda = null;
                }
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                // Máscara de recorte circular
                java.awt.geom.Ellipse2D.Double circle = new java.awt.geom.Ellipse2D.Double(x, y, size, size);
                g2.setClip(circle);

                if (imagemRedonda != null) {
                    // Preenche o círculo com a foto do banco
                    g2.drawImage(imagemRedonda, x, y, size, size, null);
                } else {
                    // Fundo do placeholder
                    g2.setColor(ThemeManager.getCurrentPalette().inputBackground());
                    g2.fill(circle);
                    
                    // Desenho do boneco minimalista
                    g2.setColor(ThemeManager.getCurrentPalette().textSecondary());
                    int cabeca = (int)(size * 0.35);
                    g2.fillOval(x + (size - cabeca)/2, y + (int)(size * 0.2), cabeca, cabeca);
                    g2.fillRoundRect(x + (int)(size * 0.2), y + (int)(size * 0.6), (int)(size * 0.6), (int)(size * 0.6), size/4, size/4);
                }

                // Remove o recorte para desenhar a borda
                g2.setClip(null);
                g2.setColor(ThemeManager.getCurrentPalette().primaryRed());
                g2.setStroke(new java.awt.BasicStroke(4f));
                g2.drawOval(x, y, size, size);

                // Texto de "Carregando..."
                if (getText() != null && !getText().isEmpty()) {
                    g2.setColor(ThemeManager.getCurrentPalette().textPrimary());
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    int txtX = x + (size - g2.getFontMetrics().stringWidth(getText())) / 2;
                    int txtY = y + size / 2 + 5;
                    g2.drawString(getText(), txtX, txtY);
                }

                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(210, 210));
        avatar.setMaximumSize(new Dimension(210, 210));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dispara o carregamento da foto se existir no banco
        if (usuario != null && usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
            carregarFotoAsync(usuario.getFotoPerfil());
        }

        JLabel lbNome = criarTextoCentral(nomeExibicao);
        lbNome.setFont(lbNome.getFont().deriveFont(Font.BOLD, 22f));
        lbNome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbTipo = criarTextoCentral(tipoExibicao.toLowerCase());
        lbTipo.setForeground(COR_TEXTO_SUAVE);
        lbTipo.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnFoto = criarBotaoNeutro("Alterar Foto");
        btnFoto.setAlignmentX(CENTER_ALIGNMENT);
        btnFoto.addActionListener(evt -> alterarFotoPerfil(usuario));

        avatarCard.add(Box.createVerticalStrut(26));
        avatarCard.add(avatar);
        avatarCard.add(Box.createVerticalStrut(18));
        avatarCard.add(lbNome);
        avatarCard.add(Box.createVerticalStrut(8));
        avatarCard.add(lbTipo);
        avatarCard.add(Box.createVerticalStrut(24));
        avatarCard.add(btnFoto);
        avatarCard.add(Box.createVerticalGlue());

        JPanel infoDireita = new JPanel(new GridLayout(2, 2, 18, 18));
        infoDireita.setOpaque(false);

        infoDireita.add(criarCapsulaInfo("Nome Completo", nomeExibicao));
        infoDireita.add(criarCapsulaInfo("E-mail Cadastrado", emailExibicao));
        infoDireita.add(criarCapsulaInfo("Tipo de Conta", Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "PROFESSOR" : "ALUNO"));
        infoDireita.add(criarCapsulaInfo("Status do Perfil", "Ativo"));

        corpo.add(avatarCard, BorderLayout.WEST);
        corpo.add(infoDireita, BorderLayout.CENTER);

        conteudo.add(corpo, BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void alterarFotoPerfil(Usuario usuario) {
        if (usuario == null) return;

        String novaUrl = JOptionPane.showInputDialog(this, 
            "Cole a URL ou código Base64 da nova foto de perfil:", 
            "Atualizar Avatar", 
            JOptionPane.PLAIN_MESSAGE);

        if (novaUrl != null && !novaUrl.trim().isEmpty()) {
            avatar.setIcon(null);
            avatar.setText("Carregando...");

            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    new UsuarioDAO().atualizarFoto(usuario.getId(), novaUrl);
                    usuario.setFotoPerfil(novaUrl); 
                    return true;
                }

                @Override
                protected void done() {
                    carregarFotoAsync(novaUrl);
                }
            };
            worker.execute();
        }
    }

    private void carregarFotoAsync(String urlStr) {
        if (urlStr == null || urlStr.trim().isEmpty()) return;

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
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
                        return new ImageIcon(img.getScaledInstance(210, 210, Image.SCALE_SMOOTH));
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar avatar: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    avatar.setText("");
                    if (icon != null) {
                        avatar.setIcon(icon);
                    } else {
                        avatar.setText("Erro de imagem");
                    }
                } catch (Exception e) {
                    avatar.setText("Erro de imagem");
                }
            }
        };
        worker.execute();
    }

    private JPanel criarCapsulaInfo(String titulo, String valor) {
        JPanel p = criarCartaoSuave();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                p.getBorder(),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel lbTitulo = new JLabel(titulo.toUpperCase());
        lbTitulo.setForeground(COR_TEXTO_SUAVE);
        lbTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lbValor = new JLabel(valor);
        lbValor.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lbValor.setForeground(COR_AZUL_ESCURO);
        lbValor.setToolTipText(valor);

        p.add(lbTitulo, BorderLayout.NORTH);
        p.add(lbValor, BorderLayout.CENTER);
        return p;
    }
}