package frontend;

import java.awt.*;
import javax.swing.*;
import frontend.base.TelaBase;
import frontend.util.Navegador;
import backend.DAO.usuarioDAO.Usuario;
import backend.Seguranca.SessaoUsuario;

public class PerfilTela extends TelaBase {

    protected final String tipoUsuario;

    public PerfilTela(String tipoUsuario) {
        super("QuimLab - Perfil");
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        Usuario usuario = SessaoUsuario.getInstancia().getUsuario();
        
        String nomeExibicao = (usuario != null) ? usuario.getNome() : "Utilizador";
        String emailExibicao = (usuario != null) ? usuario.getEmail() : "---";

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 30));
        conteudo.setOpaque(false);

        // --- TOPO CUSTOMIZADO (CENTRALIZADO E DESTACADO) ---
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setPreferredSize(new Dimension(0, 100)); // Altura fixa para o topo

        // Título centralizado e com fonte maior
        JLabel lbTituloHeader = new JLabel("Meu Perfil");
        if (Navegador.TIPO_PROFESSOR.equals(tipoUsuario)) {
            lbTituloHeader.setText("Informações do Professor");
        }
        lbTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 42)); // Fonte bem maior
        lbTituloHeader.setForeground(COR_AZUL_ESCURO);
        lbTituloHeader.setHorizontalAlignment(SwingConstants.CENTER);

        // Botão voltar posicionado à direita sem entortar o centro
        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 58));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        
        JPanel painelBotaoVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotaoVoltar.setOpaque(false);
        painelBotaoVoltar.add(voltarButton);

        // Placeholder à esquerda para equilibrar o BorderLayout e manter o título no centro exato
        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(160, 58));

        cabecalho.add(placeholder, BorderLayout.WEST);
        cabecalho.add(lbTituloHeader, BorderLayout.CENTER);
        cabecalho.add(painelBotaoVoltar, BorderLayout.EAST);

        conteudo.add(cabecalho, BorderLayout.NORTH);

        // --- ÁREA CENTRAL ---
        JPanel corpo = new JPanel(new BorderLayout(40, 0));
        corpo.setOpaque(false);

        // Lado Esquerdo: Card do Avatar
        JPanel avatarCard = criarCartaoSuave();
        avatarCard.setLayout(new BoxLayout(avatarCard, BoxLayout.Y_AXIS));
        avatarCard.setPreferredSize(new Dimension(320, 0));
        
        JLabel avatar = criarAvatarPlaceholder(" ");
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel lbNome = criarTextoCentral(nomeExibicao);
        lbNome.setFont(lbNome.getFont().deriveFont(Font.BOLD, 22f));
        lbNome.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnFoto = criarBotaoNeutro("Alterar Foto");
        btnFoto.setAlignmentX(CENTER_ALIGNMENT);
        btnFoto.addActionListener(evt -> JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento.", "QuimLab", JOptionPane.INFORMATION_MESSAGE));

        avatarCard.add(Box.createVerticalStrut(30));
        avatarCard.add(avatar);
        avatarCard.add(Box.createVerticalStrut(20));
        avatarCard.add(lbNome);
        avatarCard.add(Box.createVerticalStrut(25));
        avatarCard.add(btnFoto);
        avatarCard.add(Box.createVerticalGlue());

        // Lado Direito: Grid de Informações
        JPanel infoDireita = new JPanel(new GridLayout(2, 2, 20, 20));
        infoDireita.setOpaque(false);
        
        infoDireita.add(criarCapsulaInfo("Nome Completo", nomeExibicao));
        infoDireita.add(criarCapsulaInfo("E-mail Cadastrado", emailExibicao));
        infoDireita.add(criarCapsulaInfo("Tipo de Conta", tipoUsuario.toUpperCase()));
        infoDireita.add(criarCapsulaInfo("Status do Perfil", "Ativo"));

        corpo.add(avatarCard, BorderLayout.WEST);
        corpo.add(infoDireita, BorderLayout.CENTER);

        conteudo.add(corpo, BorderLayout.CENTER);
        
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarCapsulaInfo(String titulo, String valor) {
        JPanel p = criarCartaoSuave();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
            p.getBorder(), 
            BorderFactory.createEmptyBorder(20, 25, 20, 25) // Aumentei o respiro interno
        ));

        JLabel lbTitulo = new JLabel(titulo.toUpperCase());
        lbTitulo.setForeground(COR_TEXTO_SUAVE);
        lbTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lbValor = new JLabel(valor);
        lbValor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lbValor.setForeground(COR_AZUL_ESCURO);

        p.add(lbTitulo, BorderLayout.NORTH);
        p.add(lbValor, BorderLayout.CENTER);
        return p;
    }
}