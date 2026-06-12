import javax.swing.SwingUtilities;

import frontend.LoginTela;
import frontend.theme.ThemeManager;

//classe main
public class App {
    public static void main(String[] args) {
        // Inicia a interface gráfica (Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                ThemeManager.initialize();
                new LoginTela().setVisible(true);
            } catch (Exception e) {
                System.err.println("Erro ao abrir tela de login: " + e.getMessage());
            }
        });
    }
}
