package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.base.TelaBase;
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
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);

        JButton perfilButton = criarBotaoNeutro("Meu perfil");
        perfilButton.setPreferredSize(new Dimension(170, 58));
        perfilButton.addActionListener(evt -> Navegador.abrirTela(this, new PerfilTela(Navegador.TIPO_PROFESSOR)));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(criarBadge("PROFESSOR", new Color(235, 240, 255), new Color(50, 100, 200)), BorderLayout.WEST);
        topo.add(perfilButton, BorderLayout.EAST);

        JPanel centro = criarColunaCentral(740);
        centro.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel marcaPanel = new JPanel();
        marcaPanel.setOpaque(false);
        marcaPanel.setLayout(new BoxLayout(marcaPanel, BoxLayout.X_AXIS));
        marcaPanel.add(criarIconeLaboratorio());
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarTituloHero("QuimLab Pro"));
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarIconeLaboratorio());

        JLabel subtitulo = criarSubtituloHero("Gestão de conteúdos e análise de turmas");
        JLabel apoio = criarTextoCentral("Gerencie o banco de questões, usuários e acompanhe o desempenho das turmas.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        JButton gerenciarPerguntasButton = criarBotaoPrincipal("GERENCIAR PERGUNTAS");
        gerenciarPerguntasButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarPerguntasTela()));

        JButton gerenciarUsuariosButton = criarBotaoPrincipal("GERENCIAR USUÁRIOS");
        gerenciarUsuariosButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarUsuariosTela()));

        JButton jogarButton = criarBotaoPrincipal("MODO JOGADOR (TESTAR QUESTÕES)");
        jogarButton.setBackground(new Color(0, 153, 136));
        jogarButton.addActionListener(evt -> new SelecaoNivelModal(this, Navegador.TIPO_PROFESSOR).setVisible(true));

        JButton estatisticasButton = criarBotaoSecundario("Estatísticas das Turmas");
        estatisticasButton.addActionListener(evt -> Navegador.abrirTela(this, new TurmasEstatisticasTela()));

        centro.add(Box.createVerticalStrut(18));
        centro.add(marcaPanel);
        centro.add(Box.createVerticalStrut(8));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(8));
        centro.add(apoio);
        centro.add(Box.createVerticalStrut(30));

        centro.add(jogarButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(gerenciarPerguntasButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(gerenciarUsuariosButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(estatisticasButton);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(criarTextoSuave("Painel Administrativo | ETEC Química"), BorderLayout.WEST);

        JButton sairButton = criarBotaoLink("Encerrar Sessão");
        sairButton.addActionListener(evt -> {
            SairModal modal = new SairModal(this);
            modal.setVisible(true);

            if (modal.isConfirmarSair()) {
                Navegador.abrirTela(this, new LoginTela());
            }
        });
        rodape.add(sairButton, BorderLayout.EAST);

        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(centro, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }
}
