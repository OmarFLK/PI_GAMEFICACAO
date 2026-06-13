package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.theme.ThemeManager;
import frontend.theme.ThemeToggleButton;
import frontend.util.Navegador;

public class HomeAlunoTela extends TelaBase {

    public HomeAlunoTela() {
        super("QuimLab - Aluno");
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

        // --- TOPO: badge (esquerda) + perfil/tema (direita) ---
        JPanel painelDireita = new JPanel();
        painelDireita.setOpaque(false);
        painelDireita.setLayout(new BoxLayout(painelDireita, BoxLayout.Y_AXIS));

        JButton perfilButton = criarBotaoNeutro("Meu perfil");
        perfilButton.setPreferredSize(new Dimension(170, 58));
        perfilButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        perfilButton.addActionListener(evt -> Navegador.abrirTela(this, new PerfilTela(Navegador.TIPO_ALUNO)));

        ThemeToggleButton btnTema = new ThemeToggleButton();
        btnTema.setPreferredSize(new Dimension(170, 36));
        btnTema.setMaximumSize(new Dimension(170, 36));
        btnTema.setAlignmentX(Component.RIGHT_ALIGNMENT);

        painelDireita.add(perfilButton);
        painelDireita.add(Box.createVerticalStrut(8));
        painelDireita.add(btnTema);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        topo.add(criarBadge(
                "ALUNO",
                ThemeManager.getCurrentPalette().successSurface(),
                ThemeManager.getCurrentPalette().success()), BorderLayout.WEST);
        topo.add(painelDireita, BorderLayout.EAST);

        // --- CONTEÚDO CENTRAL ---
        JPanel marcaPanel = new JPanel();
        marcaPanel.setOpaque(false);
        marcaPanel.setLayout(new BoxLayout(marcaPanel, BoxLayout.X_AXIS));
        marcaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marcaPanel.add(criarIconeLaboratorio());
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarTituloHero("QuimLab"));
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarIconeLaboratorio());

        javax.swing.JLabel subtitulo = criarSubtituloHero("Treine vidrarias, conceitos e pratique com rapidez");
        javax.swing.JLabel apoio = criarTextoCentral("Escolha uma área principal e siga direto para a atividade.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        JButton iniciarJogoButton = criarBotaoPrincipal("INICIAR JOGO");
        iniciarJogoButton.setMaximumSize(new Dimension(428, 70));
        iniciarJogoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        iniciarJogoButton.addActionListener(evt -> new SelecaoNivelModal(this, Navegador.TIPO_ALUNO).setVisible(true));

        JButton estatisticasButton = criarBotaoSecundario("Estatísticas do Aluno");
        estatisticasButton.setMaximumSize(new Dimension(420, 70));
        estatisticasButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        estatisticasButton.addActionListener(evt -> Navegador.abrirTela(this, new EstatisticasTela(Navegador.TIPO_ALUNO)));

        JButton rankingButton = criarBotaoNeutro("Ranking Geral");
        rankingButton.setMaximumSize(new Dimension(420, 70));
        rankingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rankingButton.addActionListener(evt -> Navegador.abrirTela(this, new RankingTela(Navegador.TIPO_ALUNO)));

        // --- RODAPÉ ---
        JButton sairButton = criarBotaoLink("Sair");
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
        rodape.add(criarTextoSuave("ETEC | Ambiente acadêmico gamificado"), BorderLayout.WEST);
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
        coluna.add(Box.createVerticalStrut(38));
        coluna.add(iniciarJogoButton);
        coluna.add(Box.createVerticalStrut(18));
        coluna.add(estatisticasButton);
        coluna.add(Box.createVerticalStrut(18));
        coluna.add(rankingButton);
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
}