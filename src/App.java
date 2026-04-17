import frontend.LoginTela;
import javax.swing.SwingUtilities;
import backend.PerguntasDAO.PerguntaDAO;

public class App {
    public static void main(String[] args) {
        // Inicia a interface de forma segura
        PerguntaDAO pergunta = new PerguntaDAO();
        System.out.println(pergunta.getPergunta(1));

        SwingUtilities.invokeLater(() -> {
            new LoginTela().setVisible(true);
        });
    }
}