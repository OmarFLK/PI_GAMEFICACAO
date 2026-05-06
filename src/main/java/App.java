import javax.swing.SwingUtilities;

import backend.DAO.perguntaDAO.PerguntaDAO;
import frontend.LoginTela;

//classe main
public class App {
    public static void main(String[] args) {
        PerguntaDAO pergunta = new PerguntaDAO();
        pergunta.deletarPergunta(14);
        // Inicia a interface gráfica (Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginTela().setVisible(true);
            } catch (Exception e) {
                System.err.println("Erro ao abrir tela de login: " + e.getMessage());
            }
        });
    }
}
