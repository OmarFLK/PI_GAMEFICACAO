package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.RankingMock;
import frontend.util.Navegador;

public class RankingTela extends TelaBase {

       public RankingTela(String tipoUsuario) {
        super("QuimLab - Ranking");
        this.tipoUsuario = tipoUsuario;
        initComponents();

    }

    private final String tipoUsuario;

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudoCentral = new JPanel(new BorderLayout(0, 25)); // Espaçamento entre topo e cards
        conteudoCentral.setOpaque(false);

        // --- CABEÇALHO COM TÍTULO CENTRALIZADO E BOTÕES NAS PONTAS ---
        JPanel painelCabecalho = new JPanel(new BorderLayout());
        painelCabecalho.setOpaque(false);

        // 1. Botão Geral (Lado Esquerdo - Subiu do meio da tela)
        JButton btnFiltroGeral = criarBotaoPrincipal("Geral");
        btnFiltroGeral.setPreferredSize(new Dimension(140, 45));
        
        JPanel containerEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        containerEsquerdo.setOpaque(false);
        containerEsquerdo.setPreferredSize(new Dimension(200, 55));
        containerEsquerdo.add(btnFiltroGeral);

        // 2. Título (Centro com destaque maior)
        JLabel tituloRanking = new JLabel("Ranking Geral", SwingConstants.CENTER);
        tituloRanking.setFont(new Font("Segoe UI", Font.BOLD, 42)); 
        tituloRanking.setForeground(COR_AZUL_ESCURO);

        // 3. Botão Voltar (Lado Direito)
        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        
        JPanel containerDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        containerDireito.setOpaque(false);
        containerDireito.setPreferredSize(new Dimension(200, 55));
        containerDireito.add(voltarButton);

        // Montagem do Cabeçalho (Garante o centro absoluto)
        painelCabecalho.add(containerEsquerdo, BorderLayout.WEST);
        painelCabecalho.add(tituloRanking, BorderLayout.CENTER);
        painelCabecalho.add(containerDireito, BorderLayout.EAST);

        // --- ÁREA DOS CARDS (Recuperando seus métodos originais) ---
        JPanel painelCards = new JPanel(new BorderLayout(24, 0));
        painelCards.setOpaque(false);
        
        // Chamando seus métodos que já existem no código antigo
        painelCards.add(criarListaRanking(), BorderLayout.WEST);
        painelCards.add(criarPodio(), BorderLayout.CENTER);

        // Montagem final no Conteúdo
        conteudoCentral.add(painelCabecalho, BorderLayout.NORTH);
        conteudoCentral.add(painelCards, BorderLayout.CENTER);

        canvas.add(conteudoCentral, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        
        setContentPane(painelPrincipal);
    }

    

    private JPanel criarListaRanking() {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 0));
        JLabel titulo = criarTituloBloco("Primeiro Ano");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(22));

        List<RankingMock> ranking = DadosMockados.getRankingGeral();
        for (int i = 0; i < ranking.size(); i++) {
            RankingMock item = ranking.get(i);
            JPanel linha = new JPanel(new BorderLayout());
            linha.setOpaque(false);
            JLabel nome = criarTexto((i + 1) + ". " + item.getNome());
            nome.setFont(nome.getFont().deriveFont(24f));
            JLabel pontos = criarTexto(item.getPontuacao() + " pts");
            pontos.setForeground(COR_TEXTO_SUAVE);
            linha.add(nome, BorderLayout.WEST);
            linha.add(pontos, BorderLayout.EAST);
            card.add(linha);
            card.add(Box.createVerticalStrut(14));
        }
        return card;
    }

    private JPanel criarPodio() {
        JPanel painel = new JPanel(new BorderLayout(0, 18));
        painel.setOpaque(false);
        JLabel titulo = criarTituloBloco("Top 3 do Primeiro Ano");
        painel.add(titulo, BorderLayout.NORTH);

        JPanel podio = new JPanel(new GridLayout(1, 3, 16, 0));
        podio.setOpaque(false);
        podio.add(criarBlocoPodio("Aluno Y", "2", 250, new Color(118, 148, 184)));
        podio.add(criarBlocoPodio("Aluno X", "1", 360, COR_AZUL_ESCURO));
        podio.add(criarBlocoPodio("Aluno Z", "3", 200, COR_VERDE));
        painel.add(podio, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarBlocoPodio(String nome, String posicao, int altura, Color cor) {
        JPanel bloco = new JPanel(new BorderLayout(0, 10));
        bloco.setOpaque(false);
        JLabel nomeLabel = criarTextoCentral(nome);
        nomeLabel.setFont(nomeLabel.getFont().deriveFont(22f));
        bloco.add(nomeLabel, BorderLayout.NORTH);

        JPanel base = criarCartaoSuave();
        base.setLayout(new BorderLayout());
        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBackground(cor);
        coluna.setPreferredSize(new Dimension(0, altura));
        JLabel medalha = criarTextoCentral(posicao);
        medalha.setForeground(COR_BRANCO);
        medalha.setFont(medalha.getFont().deriveFont(84f));
        coluna.add(medalha, BorderLayout.CENTER);
        base.add(coluna, BorderLayout.CENTER);

        bloco.add(base, BorderLayout.CENTER);
        return bloco;
    }
}
