package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import frontend.base.TelaBase;
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
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);

        JButton perfilButton = criarBotaoNeutro("Meu perfil");
        perfilButton.setPreferredSize(new Dimension(170, 58));
        perfilButton.addActionListener(evt -> Navegador.abrirTela(this, new PerfilTela(Navegador.TIPO_ALUNO)));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(criarBadge("ALUNO", new java.awt.Color(228, 245, 239), COR_VERDE.darker()), BorderLayout.WEST);
        topo.add(perfilButton, BorderLayout.EAST);

        JPanel centro = criarColunaCentral(740);
        centro.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel marcaPanel = new JPanel();
        marcaPanel.setOpaque(false);
        marcaPanel.setLayout(new BoxLayout(marcaPanel, BoxLayout.X_AXIS));
        marcaPanel.add(criarIconeLaboratorio());
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarTituloHero("QuimLab"));
        marcaPanel.add(Box.createHorizontalStrut(16));
        marcaPanel.add(criarIconeLaboratorio());

        javax.swing.JLabel subtitulo = criarSubtituloHero("Treine vidrarias, conceitos e pratique com rapidez");
        javax.swing.JLabel apoio = criarTextoCentral("Escolha uma area principal e siga direto para a atividade.");
        apoio.setForeground(COR_TEXTO_SUAVE);

        JButton iniciarJogoButton = criarBotaoPrincipal("INICIAR JOGO");
        iniciarJogoButton.addActionListener(evt -> Navegador.abrirTela(this, new GameplayTela(Navegador.TIPO_ALUNO, "JOGO")));

        // BOTÃO AJUSTADO: Agora exibe aviso de desenvolvimento
        JButton estatisticasButton = criarBotaoSecundario("ESTATISTICAS DO ALUNO");
        estatisticasButton.addActionListener(evt -> {
            JOptionPane.showMessageDialog(
                this, 
                "<html><div style='text-align: center;'><b>Página em Desenvolvimento</b><br>" +
                "Estamos preparando gráficos incríveis para você acompanhar seu progresso!<br>" +
                "<font size='5'>🧪🏗️</font></div></html>", 
                "QuimLab - Em Breve", 
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        JButton rankingButton = criarBotaoNeutro("RANKING GERAL");
        rankingButton.setPreferredSize(new Dimension(0, 70));
        rankingButton.addActionListener(evt -> Navegador.abrirTela(this, new RankingTela(Navegador.TIPO_ALUNO)));

        centro.add(Box.createVerticalStrut(18));
        centro.add(marcaPanel);
        centro.add(Box.createVerticalStrut(8));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(8));
        centro.add(apoio);
        centro.add(Box.createVerticalStrut(38));
        centro.add(iniciarJogoButton);
        centro.add(Box.createVerticalStrut(18));
        centro.add(estatisticasButton);
        centro.add(Box.createVerticalStrut(18));
        centro.add(rankingButton);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(criarTextoSuave("ETEC | Ambiente academico gamificado"), BorderLayout.WEST);
        JButton sairButton = criarBotaoLink("Sair");
        sairButton.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this, "Saindo do perfil do aluno.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
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