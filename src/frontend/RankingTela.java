package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.RankingMock;
import frontend.util.Navegador;

public class RankingTela extends TelaBase {

    private final String tipoUsuario;

    public RankingTela(String tipoUsuario) {
        super("QuimLab - Ranking");
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(28, 86, 28, 86));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(18, 18));
        conteudo.setOpaque(false);

        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 58));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        conteudo.add(criarTopoComVoltar("Ranking Geral", voltarButton), BorderLayout.NORTH);

        JPanel filtros = criarLinhaBotoes(
            criarBotaoPrincipal("Geral"),
            criarBotaoNeutro("1 Ano"),
            criarBotaoNeutro("2 Ano"),
            criarBotaoNeutro("3 Ano")
        );

        JPanel centro = new JPanel(new BorderLayout(18, 0));
        centro.setOpaque(false);
        centro.add(criarListaRanking(), BorderLayout.WEST);
        centro.add(criarPodio(), BorderLayout.CENTER);

        conteudo.add(filtros, BorderLayout.CENTER);
        conteudo.add(centro, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
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
