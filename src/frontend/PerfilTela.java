package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados;
import frontend.mock.DadosMockados.UsuarioMock;
import frontend.util.Navegador;

public class PerfilTela extends TelaBase {

    private final String tipoUsuario;

    public PerfilTela(String tipoUsuario) {
        super("QuimLab - Perfil");
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        UsuarioMock usuario = Navegador.TIPO_PROFESSOR.equals(tipoUsuario)
            ? DadosMockados.getProfessorMockado()
            : DadosMockados.getAlunoMockado();

        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(26, 86, 26, 86));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(26, 26));
        conteudo.setOpaque(false);

        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 58));
        voltarButton.addActionListener(evt -> Navegador.abrirHome(this, tipoUsuario));

        String titulo = Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Painel do Professor" : "Perfil e Estatisticas";
        conteudo.add(criarTopoComVoltar(titulo, voltarButton), BorderLayout.NORTH);

        JPanel topoDados = new JPanel(new BorderLayout(22, 0));
        topoDados.setOpaque(false);

        JPanel avatarPanel = criarCartaoSuave();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setPreferredSize(new Dimension(280, 0));
        JLabel avatar = criarAvatarPlaceholder(" ");
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        avatarPanel.add(avatar);
        avatarPanel.add(Box.createVerticalStrut(8));
        JLabel nomePequeno = criarTextoCentral(usuario.getNome());
        nomePequeno.setFont(nomePequeno.getFont().deriveFont(18f));
        avatarPanel.add(nomePequeno);
        avatarPanel.add(Box.createVerticalStrut(14));
        JButton editarFotoButton = criarBotaoNeutro("Editar Foto");
        editarFotoButton.addActionListener(evt -> avisarAcao("Editar foto"));
        avatarPanel.add(editarFotoButton);

        JPanel blocoDireito = new JPanel(new BorderLayout(0, 16));
        blocoDireito.setOpaque(false);

        JPanel infoPanel = criarGrid(2, 2, 16);
        infoPanel.add(criarCapsulaInformativa(usuario.getNome()));
        infoPanel.add(criarCapsulaInformativa(Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Materia: Quimica" : "Pontuacao atual: 1280"));
        infoPanel.add(criarCapsulaInformativa(usuario.getEmail()));
        infoPanel.add(criarCapsulaInformativa(Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "Turmas: 3" : "Ranking atual: 2 lugar"));

        JPanel acoes = criarGrid(1, 3, 14);
        JButton mudarEmailButton = criarBotaoNeutro("Mudar Email");
        mudarEmailButton.addActionListener(evt -> avisarAcao("Mudar email"));
        JButton trocarSenhaButton = criarBotaoNeutro("Trocar Senha");
        trocarSenhaButton.addActionListener(evt -> avisarAcao("Trocar senha"));
        JButton relatarProblemaButton = criarBotaoNeutro("Relatar Problema");
        relatarProblemaButton.addActionListener(evt -> avisarAcao("Relatar problema"));
        acoes.add(mudarEmailButton);
        acoes.add(trocarSenhaButton);
        acoes.add(relatarProblemaButton);

        blocoDireito.add(infoPanel, BorderLayout.CENTER);
        blocoDireito.add(acoes, BorderLayout.SOUTH);

        topoDados.add(avatarPanel, BorderLayout.WEST);
        topoDados.add(blocoDireito, BorderLayout.CENTER);

        JPanel graficos = criarGrid(1, 3, 18);
        if (Navegador.TIPO_PROFESSOR.equals(tipoUsuario)) {
            graficos.add(criarGraficoBarras("Comparacao de pontos por turmas", new int[] { 12, 7, 5, 3 }, new Color[] { COR_AZUL_ESCURO, COR_AZUL, COR_VERDE, new Color(183, 198, 212) }));
            graficos.add(criarGraficoPizza("Desempenho geral das turmas", new String[] { "Semana 1 | 10", "Semana 2 | 30", "Semana 3 | 45", "Semana 4 | 65" }));
            graficos.add(criarGraficoDuplo("Acertos e erros mensais"));
        } else {
            graficos.add(criarGraficoBarras("Posicao mensal", new int[] { 12, 8, 5, 2 }, new Color[] { COR_AZUL_ESCURO, COR_AZUL, COR_VERDE, new Color(183, 198, 212) }));
            graficos.add(criarGraficoPizza("Pontuacao mensal", new String[] { "Semana 1 | 10", "Semana 2 | 30", "Semana 3 | 45", "Semana 4 | 65" }));
            graficos.add(criarGraficoDuplo("Acertos e erros mensais"));
        }

        conteudo.add(topoDados, BorderLayout.CENTER);
        conteudo.add(graficos, BorderLayout.SOUTH);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private JPanel criarCapsulaInformativa(String texto) {
        JPanel painel = criarCartaoSuave();
        painel.setPreferredSize(new Dimension(0, 92));
        JLabel label = criarTextoCentral("<html><div style='text-align:center;'>" + texto + "</div></html>");
        label.setFont(label.getFont().deriveFont(17f));
        painel.add(label, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarGraficoBarras(String titulo, int[] valores, Color[] cores) {
        JPanel painel = criarCartaoSuave();
        painel.setLayout(new BorderLayout(0, 14));
        JLabel tituloLabel = criarTextoCentral("<html><div style='text-align:center; font-weight:bold;'>" + titulo + "</div></html>");
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(15f));
        painel.add(tituloLabel, BorderLayout.NORTH);

        JPanel barras = new JPanel(new GridLayout(1, valores.length, 12, 0));
        barras.setOpaque(false);
        int max = 1;
        for (int valor : valores) {
            max = Math.max(max, valor);
        }

        for (int i = 0; i < valores.length; i++) {
            JPanel coluna = new JPanel(new BorderLayout(0, 10));
            coluna.setOpaque(false);
            JPanel alinhador = new JPanel(new BorderLayout());
            alinhador.setOpaque(false);
            JPanel barra = new JPanel();
            barra.setBackground(cores[i]);
            barra.setPreferredSize(new Dimension(32, 26 + (150 * valores[i] / max)));
            alinhador.add(barra, BorderLayout.SOUTH);
            coluna.add(alinhador, BorderLayout.CENTER);
            coluna.add(criarTextoCentral("S" + (i + 1)), BorderLayout.SOUTH);
            barras.add(coluna);
        }

        painel.add(barras, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarGraficoPizza(String titulo, String[] legendas) {
        JPanel painel = criarCartaoSuave();
        painel.setLayout(new BorderLayout(0, 12));
        JLabel tituloLabel = criarTextoCentral("<html><div style='text-align:center; font-weight:bold;'>" + titulo + "</div></html>");
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(15f));
        painel.add(tituloLabel, BorderLayout.NORTH);
        painel.add(criarPlaceholder("Grafico de distribuicao"), BorderLayout.CENTER);

        JPanel legenda = new JPanel(new GridLayout(2, 2, 8, 8));
        legenda.setOpaque(false);
        for (String legendaTexto : legendas) {
            legenda.add(criarTextoCentral(legendaTexto));
        }
        painel.add(legenda, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarGraficoDuplo(String titulo) {
        JPanel painel = criarCartaoSuave();
        painel.setLayout(new BorderLayout(0, 12));
        JLabel tituloLabel = criarTextoCentral("<html><div style='text-align:center; font-weight:bold;'>" + titulo + "</div></html>");
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(15f));
        painel.add(tituloLabel, BorderLayout.NORTH);

        JPanel barras = new JPanel(new GridLayout(1, 3, 12, 0));
        barras.setOpaque(false);
        barras.add(criarParBarras(22, 18, "S1"));
        barras.add(criarParBarras(10, 8, "S2"));
        barras.add(criarParBarras(30, 40, "S3"));
        painel.add(barras, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarParBarras(int esquerda, int direita, String texto) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        JPanel colunas = new JPanel(new GridLayout(1, 2, 6, 0));
        colunas.setOpaque(false);
        colunas.add(criarBarraVertical(esquerda, COR_AZUL_ESCURO));
        colunas.add(criarBarraVertical(direita, new Color(152, 170, 186)));
        painel.add(colunas, BorderLayout.CENTER);
        painel.add(criarTextoCentral(texto), BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarBarraVertical(int altura, Color cor) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        JPanel barra = new JPanel();
        barra.setBackground(cor);
        barra.setPreferredSize(new Dimension(28, altura * 4));
        painel.add(barra, BorderLayout.SOUTH);
        return painel;
    }

    private void avisarAcao(String acao) {
        JOptionPane.showMessageDialog(this, acao + " e uma acao visual mockada nesta etapa.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
    }
}
