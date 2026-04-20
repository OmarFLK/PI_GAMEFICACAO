// Imports 
import backend.DAO.usuarioDAO.*;
import javax.swing.SwingUtilities;
import frontend.LoginTela;

//classe main
public class App {
    public static void main(String[] args) {
        
        // Inicia a interface gráfica (Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginTela().setVisible(true);
            } catch (Exception e) {
                System.err.println("Erro ao abrir tela de login: " + e.getMessage());
            }
        });

        // teste senhas criptografadas
        System.out.println("\n=== Teste de Login no Banco ===");
        UsuarioDAO dao = new UsuarioDAO();

        // Tenta o login
        Usuario logado = dao.efetuarLogin("linux@teste.com", "senha123");

        if (logado != null) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + logado.getNome());
        } else {
            System.out.println("Falha no login. Verifique se o hash está correto no banco.");
        }
    }
}