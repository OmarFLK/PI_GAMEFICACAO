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

    private final String tipoUsuario;
    private String filtroAtual;
    private JPanel painelRanking;

    public RankingTela(String tipoUsuario) {
        super("QuimLab - Ranking");
        this.tipoUsuario = tipoUsuario;
        this.filtroAtual = Navegador.TIPO_ALUNO.equals(tipoUsuario) ? "Minha Turma" : "Geral";
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(22, 72, 22, 72));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(criarTopo());
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(criarFiltros());

        painelRanking = new JPanel(new BorderLayout());
        painelRanking.setOpaque(false);
        atualizarRanking();

        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(painelRanking, BorderLayout.CENTER);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setPreferredSize(new Dimension(0, 58));

        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(130, 46));

        JLabel titulo = new JLabel(getTituloTela(), SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titulo.setForeground(COR_AZUL_ESCURO);

        JButton voltarButton = criarBotaoLink("Voltar");
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelVoltar.setOpaque(false);
        painelVoltar.setPreferredSize(new Dimension(130, 46));
        painelVoltar.add(voltarButton);

        topo.add(placeholder, BorderLayout.WEST);
        topo.add(titulo, BorderLayout.CENTER);
        topo.add(painelVoltar, BorderLayout.EAST);
        return topo;
    }

    private JPanel criarFiltros() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        filtros.setOpaque(false);
        if (Navegador.TIPO_ALUNO.equals(tipoUsuario)) {
            filtros.add(criarBadge("MINHA TURMA", new Color(228, 245, 239), COR_VERDE.darker()));
            return filtros;
        }

        filtros.add(criarBotaoFiltro("Geral"));
        filtros.add(criarBotaoFiltro("1\u00ba Ano"));
        filtros.add(criarBotaoFiltro("2\u00ba Ano"));
        filtros.add(criarBotaoFiltro("3\u00ba Ano"));
        return filtros;
    }

    private JButton criarBotaoFiltro(String filtro) {
        JButton botao = filtro.equals(filtroAtual) ? criarBotaoPrincipal(filtro) : criarBotaoNeutro(filtro);
        botao.setPreferredSize(new Dimension(126, 44));
        botao.addActionListener(evt -> atualizarFiltro(filtro));
        return botao;
    }

    private void atualizarFiltro(String filtro) {
        filtroAtual = filtro;
        atualizarRanking();
    }

    private void atualizarRanking() {
        painelRanking.removeAll();
        List<RankingMock> ranking = DadosMockados.getRankingPorFiltro(filtroAtual);

        JPanel corpo = new JPanel(new GridLayout(1, 2, 26, 0));
        corpo.setOpaque(false);
        corpo.add(criarListaRanking(ranking));
        corpo.add(criarTopTres(ranking));

        painelRanking.add(corpo, BorderLayout.CENTER);
        painelRanking.revalidate();
        painelRanking.repaint();
    }

    private JPanel criarListaRanking(List<RankingMock> ranking) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titulo = criarTituloBloco(getTituloLista(filtroAtual));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(18));

        for (int i = 0; i < ranking.size(); i++) {
            RankingMock item = ranking.get(i);
            JPanel linha = new JPanel(new BorderLayout());
            linha.setOpaque(false);
            linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

            JLabel nome = criarTexto((i + 1) + ". " + item.getNome());
            nome.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            JLabel pontos = criarTexto(item.getPontuacao() + " pts");
            pontos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            pontos.setForeground(COR_TEXTO_SUAVE);

            linha.add(nome, BorderLayout.WEST);
            linha.add(pontos, BorderLayout.EAST);
            card.add(linha);
            card.add(Box.createVerticalStrut(18));
        }
        return card;
    }

    private JPanel criarTopTres(List<RankingMock> ranking) {
        JPanel painel = new JPanel(new BorderLayout(0, 18));
        painel.setOpaque(false);

        JLabel titulo = criarTituloBloco("Top 3 de " + getTituloPodio(filtroAtual));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);

        if (ranking.size() >= 3) {
            cards.add(criarCardTop(ranking.get(0), "1\u00ba", COR_VERMELHO_ETEC));
            cards.add(criarCardTop(ranking.get(1), "2\u00ba", new Color(118, 148, 184)));
            cards.add(criarCardTop(ranking.get(2), "3\u00ba", COR_VERDE.darker()));
        }

        painel.add(cards, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarCardTop(RankingMock item, String posicao, Color cor) {
        JPanel card = criarCartaoSuave();
        card.setLayout(new BorderLayout(0, 10));
        card.setPreferredSize(new Dimension(0, 250));

        JLabel posicaoLabel = new JLabel(posicao, SwingConstants.CENTER);
        posicaoLabel.setFont(new Font("Segoe UI", Font.BOLD, 54));
        posicaoLabel.setForeground(cor);

        JLabel nomeLabel = new JLabel("<html><div style='text-align:center;'>" + item.getNome() + "</div></html>", SwingConstants.CENTER);
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nomeLabel.setForeground(COR_AZUL_ESCURO);

        JLabel pontosLabel = new JLabel(item.getPontuacao() + " pts", SwingConstants.CENTER);
        pontosLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pontosLabel.setForeground(COR_TEXTO);

        card.add(posicaoLabel, BorderLayout.NORTH);
        card.add(nomeLabel, BorderLayout.CENTER);
        card.add(pontosLabel, BorderLayout.SOUTH);
        return card;
    }

    private String getTituloTela() {
        if (Navegador.TIPO_ALUNO.equals(tipoUsuario)) {
            return "Ranking da Minha Turma";
        }
        return "Ranking Geral de Alunos";
    }

    private String getTituloLista(String filtro) {
        if ("Minha Turma".equals(filtro)) {
            return "Minha Turma";
        }
        if ("1\u00ba Ano".equals(filtro)) {
            return "Primeiro Ano";
        }
        if ("2\u00ba Ano".equals(filtro)) {
            return "Segundo Ano";
        }
        if ("3\u00ba Ano".equals(filtro)) {
            return "Terceiro Ano";
        }
        return "Ranking Geral";
    }

    private String getTituloPodio(String filtro) {
        if ("Minha Turma".equals(filtro)) {
            return "Minha Turma";
        }
        if ("1\u00ba Ano".equals(filtro)) {
            return "Primeiro Ano";
        }
        if ("2\u00ba Ano".equals(filtro)) {
            return "Segundo Ano";
        }
        if ("3\u00ba Ano".equals(filtro)) {
            return "Terceiro Ano";
        }
        return "Geral";
    }
}
