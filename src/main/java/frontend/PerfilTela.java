package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backend.DAO.usuarioDAO.Usuario;
import backend.Seguranca.SessaoUsuario;
import frontend.base.TelaBase;
import frontend.util.Navegador;

public class PerfilTela extends TelaBase {

    protected final String tipoUsuario;

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

        JLabel avatar = criarAvatarPlaceholder("");
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        avatar.setMaximumSize(new Dimension(210, 210));

        JLabel lbNome = criarTextoCentral(nomeExibicao);
        lbNome.setFont(lbNome.getFont().deriveFont(Font.BOLD, 22f));
        lbNome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbTipo = criarTextoCentral(tipoExibicao.toLowerCase());
        lbTipo.setForeground(COR_TEXTO_SUAVE);
        lbTipo.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnFoto = criarBotaoNeutro("Alterar Foto");
        btnFoto.setAlignmentX(CENTER_ALIGNMENT);
        btnFoto.addActionListener(evt -> JOptionPane.showMessageDialog(
                this,
                "A foto de perfil poderá ser configurada em uma próxima etapa.",
                "QuimLab",
                JOptionPane.INFORMATION_MESSAGE));

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
