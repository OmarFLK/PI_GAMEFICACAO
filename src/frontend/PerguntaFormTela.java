package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import frontend.base.TelaBase;
import frontend.mock.DadosMockados.PerguntaCadastroMock;
import frontend.util.Navegador;

public class PerguntaFormTela extends TelaBase {

    private final PerguntaCadastroMock perguntaMock;
    private JTextArea enunciadoTextArea;
    private JTextField imagemTextField;
    private JComboBox<String> dificuldadeComboBox;
    private JTextField alternativa1TextField;
    private JTextField alternativa2TextField;
    private JTextField alternativa3TextField;
    private JTextField alternativa4TextField;

    public PerguntaFormTela(PerguntaCadastroMock perguntaMock) {
        super("QuimLab - Formulario de pergunta");
        this.perguntaMock = perguntaMock;
        initComponents();
        preencherDados();
    }

    private void initComponents() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelExterno = new JPanel(new BorderLayout());
        painelExterno.setOpaque(false);
        painelExterno.setBorder(BorderFactory.createEmptyBorder(26, 120, 26, 120));

        JPanel canvas = criarCanvasCentral();
        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);

        JButton voltarButton = criarBotaoNeutro("Voltar");
        voltarButton.setPreferredSize(new Dimension(160, 60));
        voltarButton.addActionListener(evt -> Navegador.abrirTela(this, new GerenciarPerguntasTela()));
        conteudo.add(criarTopoComVoltar("ADICIONAR/EDITAR PERGUNTAS", voltarButton), BorderLayout.NORTH);

        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        enunciadoTextArea = new JTextArea(4, 40);
        enunciadoTextArea.setLineWrap(true);
        enunciadoTextArea.setWrapStyleWord(true);
        enunciadoTextArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JScrollPane enunciadoScroll = new JScrollPane(enunciadoTextArea);
        enunciadoScroll.setBorder(new RoundedLineBorder(COR_PRETO, 2, 26, 18));

        imagemTextField = criarCampoTexto("Imagem / descricao visual");
        dificuldadeComboBox = new JComboBox<>(new String[] { "FACIL", "MEDIO", "DIFICIL" });
        alternativa1TextField = criarCampoTexto("Alternativa 1");
        alternativa2TextField = criarCampoTexto("Alternativa 2");
        alternativa3TextField = criarCampoTexto("Alternativa 3");
        alternativa4TextField = criarCampoTexto("Alternativa 4");

        JPanel alternativas = new JPanel(new GridLayout(2, 2, 16, 16));
        alternativas.setOpaque(false);
        alternativas.add(alternativa1TextField);
        alternativas.add(alternativa2TextField);
        alternativas.add(alternativa3TextField);
        alternativas.add(alternativa4TextField);

        JButton salvarButton = criarBotaoPrincipal("Salvar");
        salvarButton.addActionListener(evt -> salvarPergunta());

        formulario.add(enunciadoScroll);
        formulario.add(Box.createVerticalStrut(16));
        formulario.add(imagemTextField);
        formulario.add(Box.createVerticalStrut(16));
        formulario.add(dificuldadeComboBox);
        formulario.add(Box.createVerticalStrut(16));
        formulario.add(alternativas);
        formulario.add(Box.createVerticalStrut(24));
        formulario.add(salvarButton);

        conteudo.add(formulario, BorderLayout.CENTER);
        canvas.add(conteudo, BorderLayout.CENTER);
        painelExterno.add(canvas, BorderLayout.CENTER);
        painelPrincipal.add(painelExterno, BorderLayout.CENTER);
        setContentPane(painelPrincipal);
    }

    private void preencherDados() {
        if (perguntaMock == null) {
            return;
        }

        enunciadoTextArea.setText(perguntaMock.getEnunciado());
        imagemTextField.setText("Imagem do material - placeholder visual");
        dificuldadeComboBox.setSelectedItem(perguntaMock.getDificuldade());
    }

    private void salvarPergunta() {
        if (enunciadoTextArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o enunciado da pergunta.", "QuimLab", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Pergunta salva visualmente.", "QuimLab", JOptionPane.INFORMATION_MESSAGE);
        Navegador.abrirTela(this, new GerenciarPerguntasTela());
    }
}
