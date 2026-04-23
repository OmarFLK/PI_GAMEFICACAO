package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.util.Navegador;

public class ResultadoTela extends TelaBase {

    private final String tipoUsuario;
    private final int pontuacao;
    private final int acertos;
    private final int totalPerguntas;

    public ResultadoTela(String tipoUsuario, int pontuacao, int acertos, int totalPerguntas) {
        super("QuimLab - Resultado");
        this.tipoUsuario = tipoUsuario;
        this.pontuacao = pontuacao;
        this.acertos = acertos;
        this.totalPerguntas = totalPerguntas;
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(34, 150, 34, 150));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        conteudo.add(criarBadge("PARTIDA ENCERRADA", new java.awt.Color(228, 240, 248), COR_AZUL_ESCURO));
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(criarCabecalhoMarca("Resultado", "Resumo rapido do seu desempenho"));
        conteudo.add(Box.createVerticalStrut(24));

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 16));
        cards.setOpaque(false);
        cards.add(criarResumo("Pontuacao final", String.valueOf(pontuacao)));
        cards.add(criarResumo("Acertos", acertos + " / " + totalPerguntas));
        cards.add(criarResumo("Modo", Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Professor" : "Aluno"));
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(26));

        JButton voltarHomeButton = criarBotaoPrincipal("Voltar para a home");
        voltarHomeButton.setPreferredSize(new Dimension(420, 82));
        voltarHomeButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));
        conteudo.add(voltarHomeButton);

        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarResumo(String titulo, String valor) {
        JPanel painel = criarCartaoSuave();
        painel.setLayout(new BorderLayout(0, 10));
        javax.swing.JLabel tituloLabel = criarTextoCentral(titulo);
        tituloLabel.setForeground(COR_TEXTO_SUAVE);
        painel.add(tituloLabel, BorderLayout.NORTH);
        javax.swing.JLabel valorLabel = criarTextoCentral(valor);
        valorLabel.setFont(valorLabel.getFont().deriveFont(30f));
        valorLabel.setForeground(COR_AZUL_ESCURO);
        painel.add(valorLabel, BorderLayout.CENTER);
        return painel;
    }
}
