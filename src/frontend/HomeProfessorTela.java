package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

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

        // Botão de Perfil do Professor
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
        JLabel apoio = criarTextoCentral("Gerencie o banco de questões, usuários ou teste o simulador.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        // --- BOTÕES DE GERENCIAMENTO ---
        
        JButton gerenciarPerguntasButton = criarBotaoPrincipal("GERENCIAR PERGUNTAS");
        gerenciarPerguntasButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarPerguntasTela()));

        JButton gerenciarUsuariosButton = criarBotaoPrincipal("GERENCIAR USUÁRIOS");
        gerenciarUsuariosButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarUsuariosTela()));

        // --- NOVO BOTÃO: MODO JOGADOR ---
        // Usei o criarBotaoSecundario ou um estilo customizado para destacar que é uma ação de "play"
        JButton jogarButton = criarBotaoPrincipal("MODO JOGADOR (TESTAR QUESTÕES)");
        jogarButton.setBackground(new Color(0, 150, 136)); // Um tom de verde água/teal para diferenciar
        jogarButton.addActionListener(evt -> {
            // Abre a gameplay passando o tipo PROFESSOR para que ele volte para esta tela ao sair
            Navegador.abrirTela(this, new GameplayTela(Navegador.TIPO_PROFESSOR, "MODO_TESTE"));
        });

        JButton estatisticasButton = criarBotaoSecundario("ESTATISTICAS DAS TURMAS");
        estatisticasButton.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this, "Painel de Análise em Desenvolvimento...", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton rankingButton = criarBotaoNeutro("RANKING GLOBAL DE ALUNOS");
        rankingButton.setPreferredSize(new Dimension(0, 70));
        rankingButton.addActionListener(evt -> Navegador.abrirTela(this, new RankingTela(Navegador.TIPO_PROFESSOR)));

        // Organização na coluna central
        centro.add(Box.createVerticalStrut(18));
        centro.add(marcaPanel);
        centro.add(Box.createVerticalStrut(8));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(8));
        centro.add(apoio);
        centro.add(Box.createVerticalStrut(30));
        
        centro.add(jogarButton); // Botão de jogar no topo das ações
        centro.add(Box.createVerticalStrut(12));
        centro.add(gerenciarPerguntasButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(gerenciarUsuariosButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(estatisticasButton);
        centro.add(Box.createVerticalStrut(12));
        centro.add(rankingButton);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(criarTextoSuave("Painel Administrativo | ETEC Química"), BorderLayout.WEST);
        
        JButton sairButton = criarBotaoLink("Encerrar Sessão");
        sairButton.addActionListener(evt -> {
            backend.Seguranca.SessaoUsuario.getInstancia().encerrarSessao();
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