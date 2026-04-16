import frontend.LoginTela;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Inicia a interface de forma segura
        SwingUtilities.invokeLater(() -> {
            new LoginTela().setVisible(true);
        });
    }
}