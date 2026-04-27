package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
        // Busca os dados reais da sessão no pacote Seguranca
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

        // TOPO
        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 58));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        String tituloHeader = Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Informações do Professor" : "Meu Perfil";
        conteudo.add(criarTopoComVoltar(tituloHeader, voltarButton), BorderLayout.NORTH);

        // ÁREA CENTRAL
        JPanel corpo = new JPanel(new BorderLayout(40, 0));
        corpo.setOpaque(false);

        // Lado Esquerdo: Card do Avatar
        JPanel avatarCard = criarCartaoSuave();
        avatarCard.setLayout(new BoxLayout(avatarCard, BoxLayout.Y_AXIS));
        avatarCard.setPreferredSize(new Dimension(320, 0));
        
        JLabel avatar = criarAvatarPlaceholder(" ");
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel lbNome = criarTextoCentral(nomeExibicao);
        lbNome.setFont(lbNome.getFont().deriveFont(22f));
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

        // Lado Direito: Grid de Informações (Ocupando o espaço central)
        JPanel infoDireita = new JPanel(new GridLayout(2, 2, 20, 20));
        infoDireita.setOpaque(false);
        
        infoDireita.add(criarCapsulaInfo("Nome Completo", nomeExibicao));
        infoDireita.add(criarCapsulaInfo("E-mail Cadastrado", emailExibicao));
        infoDireita.add(criarCapsulaInfo("Tipo de Conta", tipoUsuario));
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
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lbTitulo = new JLabel(titulo);
        lbTitulo.setForeground(COR_TEXTO_SUAVE);
        lbTitulo.setFont(lbTitulo.getFont().deriveFont(13f));

        JLabel lbValor = new JLabel(valor);
        lbValor.setFont(lbValor.getFont().deriveFont(18f));
        lbValor.setForeground(COR_PRETO);

        p.add(lbTitulo, BorderLayout.NORTH);
        p.add(lbValor, BorderLayout.CENTER);
        return p;
    }
}