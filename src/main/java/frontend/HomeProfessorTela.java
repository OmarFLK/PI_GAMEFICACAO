package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.theme.ThemeToggleButton;
import frontend.util.AppTheme;
import frontend.util.Navegador;

public class HomeProfessorTela extends TelaBase {

    public HomeProfessorTela() {
        super("QuimLab - Painel do Professor");
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(28, 110, 28, 110));

        JPanel canvas = criarCanvasCentral();

        // Coluna central única, igual ao Login
        JPanel coluna = criarColunaCentral(740);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- PAINEL DIREITO (Perfil + Tema) ---
        JPanel painelDireita = new JPanel();
        painelDireita.setOpaque(false);
        painelDireita.setLayout(new BoxLayout(painelDireita, BoxLayout.Y_AXIS));

        JButton perfilButton = criarBotaoNeutro("Meu perfil");
        perfilButton.setPreferredSize(new Dimension(170, 58));
        perfilButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        perfilButton.addActionListener(evt -> Navegador.abrirTela(this, new PerfilTela(Navegador.TIPO_PROFESSOR)));

        ThemeToggleButton btnTema = new ThemeToggleButton();
        btnTema.setPreferredSize(new Dimension(170, 36));
        btnTema.setMaximumSize(new Dimension(170, 36));
        btnTema.setAlignmentX(Component.RIGHT_ALIGNMENT);

        painelDireita.add(perfilButton);
        painelDireita.add(Box.createVerticalStrut(8));
        painelDireita.add(btnTema);

        // --- TOPO ---
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JLabel professorBadge = criarBadge("PROFESSOR", AppTheme.RED_SOFT, AppTheme.PROFESSOR_HIGHLIGHT);
        professorBadge.setPreferredSize(new Dimension(150, 42));
        topo.add(professorBadge, BorderLayout.WEST);
        topo.add(painelDireita, BorderLayout.EAST);

        // --- CONTEÚDO CENTRAL ---
        JPanel marcaPanel = new JPanel();
        marcaPanel.setOpaque(false);
        marcaPanel.setLayout(new BoxLayout(marcaPanel, BoxLayout.X_AXIS));
        marcaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marcaPanel.add(criarIconeLaboratorio());
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarTituloHero("QuimLab Pro"));
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarIconeLaboratorio());

        JLabel subtitulo = criarSubtituloHero("Gestão de conteúdos e análise de turmas");
        JLabel apoio = criarTextoCentral("Gerencie o banco de questões, usuários e acompanhe o desempenho das turmas.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        JButton gerenciarPerguntasButton = criarBotaoPrincipal("GERENCIAR PERGUNTAS");
        configurarBotaoMenu(gerenciarPerguntasButton);
        gerenciarPerguntasButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarPerguntasTela()));

        JButton gerenciarUsuariosButton = criarBotaoPrincipal("GERENCIAR USUÁRIOS");
        configurarBotaoMenu(gerenciarUsuariosButton);
        gerenciarUsuariosButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarUsuariosTela()));

        JButton jogarButton = criarBotaoPrincipal("MODO JOGADOR (TESTAR QUESTÕES)");
        configurarBotaoMenu(jogarButton);
        jogarButton.addActionListener(evt -> new SelecaoNivelModal(this, Navegador.TIPO_PROFESSOR).setVisible(true));

        JButton estatisticasButton = criarBotaoSecundario("Estatísticas das Turmas");
        configurarBotaoMenu(estatisticasButton);
        estatisticasButton.addActionListener(evt -> Navegador.abrirTela(this, new TurmasEstatisticasTela()));

        // --- RODAPÉ ---
        JButton sairButton = criarBotaoLink("Encerrar Sessão");
        sairButton.addActionListener(evt -> {
            SairModal modal = new SairModal(this);
            modal.setVisible(true);
            if (modal.isConfirmarSair()) {
                Navegador.abrirTela(this, new LoginTela());
            }
        });

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rodape.add(criarTextoSuave("Painel Administrativo | ETEC Química"), BorderLayout.WEST);
        rodape.add(sairButton, BorderLayout.EAST);

        // --- MONTAGEM DA COLUNA (igual ao Login) ---
        coluna.add(topo);
        coluna.add(Box.createVerticalGlue());
        coluna.add(marcaPanel);
        coluna.add(Box.createVerticalStrut(8));
        coluna.add(criarLinhaDestaque());
        coluna.add(Box.createVerticalStrut(10));
        coluna.add(subtitulo);
        coluna.add(Box.createVerticalStrut(8));
        coluna.add(apoio);
        coluna.add(Box.createVerticalStrut(30));
        coluna.add(jogarButton);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(gerenciarPerguntasButton);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(gerenciarUsuariosButton);
        coluna.add(Box.createVerticalStrut(12));
        coluna.add(estatisticasButton);
        coluna.add(Box.createVerticalGlue());
        coluna.add(rodape);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(coluna, BorderLayout.CENTER);

        canvas.add(centro, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void configurarBotaoMenu(JButton botao) {
        botao.setPreferredSize(new Dimension(460, 64));
        botao.setMaximumSize(new Dimension(460, 64));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}