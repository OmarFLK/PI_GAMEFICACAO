package frontend;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.util.Navegador;

public class HomeProfessorTela extends TelaBase {

    public HomeProfessorTela() {
        super("QuimLab - Professor");
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
        perfilButton.setPreferredSize(new java.awt.Dimension(170, 58));
        perfilButton.addActionListener(evt -> Navegador.abrirTela(this, new PerfilTela(Navegador.TIPO_PROFESSOR)));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(criarBadge("PROFESSOR", new java.awt.Color(235, 243, 252), COR_AZUL_ESCURO), BorderLayout.WEST);
        topo.add(perfilButton, BorderLayout.EAST);

        JPanel centro = criarColunaCentral(780);
        centro.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel marcaPanel = new JPanel();
        marcaPanel.setOpaque(false);
        marcaPanel.setLayout(new BoxLayout(marcaPanel, BoxLayout.X_AXIS));
        marcaPanel.add(criarIconeLaboratorio());
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarTituloHero("QuimLab"));
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarIconeLaboratorio());

        javax.swing.JLabel subtitulo = criarSubtituloHero("Gerencie perguntas, acompanhe turmas e valide atividades");
        javax.swing.JLabel apoio = criarTextoCentral("Acesso rapido para as acoes principais do professor.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        JButton testarQuestoesButton = criarBotaoPrincipal("TESTAR QUESTOES");
        testarQuestoesButton.addActionListener(evt -> Navegador.abrirTela(this, new GameplayTela(Navegador.TIPO_PROFESSOR, "TESTE")));

        JButton perguntasButton = criarBotaoSecundario("ADICIONAR E EDITAR PERGUNTAS");
        perguntasButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarPerguntasTela()));

        JButton estatisticasButton = criarBotaoSecundario("ESTATISTICAS DOS ALUNOS");
        estatisticasButton.addActionListener(evt -> Navegador.abrirTela(this, new EstatisticasTela(Navegador.TIPO_PROFESSOR)));

        JButton rankingButton = criarBotaoNeutro("RANKING GERAL");
        rankingButton.setPreferredSize(new java.awt.Dimension(0, 70));
        rankingButton.addActionListener(evt -> Navegador.abrirTela(this, new RankingTela(Navegador.TIPO_PROFESSOR)));

        centro.add(Box.createVerticalStrut(18));
        centro.add(marcaPanel);
        centro.add(Box.createVerticalStrut(8));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(8));
        centro.add(apoio);
        centro.add(Box.createVerticalStrut(34));
        centro.add(testarQuestoesButton);
        centro.add(Box.createVerticalStrut(16));
        centro.add(perguntasButton);
        centro.add(Box.createVerticalStrut(16));
        centro.add(estatisticasButton);
        centro.add(Box.createVerticalStrut(16));
        centro.add(rankingButton);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(criarTextoSuave("ETEC | Painel academico do professor"), BorderLayout.WEST);
        JButton sairButton = criarBotaoLink("Sair");
        sairButton.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this, "Saindo do perfil do professor.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
            Navegador.abrirLogin(this);
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
