package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
        // 1. Estrutura Base
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(34, 150, 34, 150));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        // 2. Cabeçalho e Badges (Mantendo o que já funcionava)
        conteudo.add(criarBadge("PARTIDA ENCERRADA", new java.awt.Color(228, 240, 248), COR_AZUL_ESCURO));
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(criarCabecalhoMarca("Resultado", "Resumo rápido do seu desempenho"));
        conteudo.add(Box.createVerticalStrut(24));

        // 3. Cards de Resumo (Pontuação, Acertos, Modo)
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 16));
        cards.setOpaque(false);
        cards.add(criarResumo("Pontuacao final", String.valueOf(pontuacao)));
        cards.add(criarResumo("Acertos", acertos + " / " + totalPerguntas));
        cards.add(criarResumo("Modo", Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Professor" : "Aluno"));
        
        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(26));

        // 4. O Botão "Voltar para a Home" (Ajustado para o padrão 420x70)
        JPanel containerBotaoHome = new JPanel(new FlowLayout(FlowLayout.CENTER));
        containerBotaoHome.setOpaque(false);

        JButton voltarButton = criarBotaoPrincipal("Voltar para a home");
        voltarButton.setPreferredSize(new Dimension(420, 70));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        containerBotaoHome.add(voltarButton);

        // 5. Montagem Final da Hierarquia (Sem duplicidade)
        canvas.add(conteudo, BorderLayout.CENTER);
        
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelExterno.add(containerBotaoHome, BorderLayout.SOUTH);

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
