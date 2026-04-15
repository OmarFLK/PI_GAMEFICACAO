import backend.Usuario;
import backend.UsuarioDAO;

public class App {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        
        // Exemplo de teste (substitua por Scanner se quiser ler do teclado)
        String emailDigitado = "teste@email.com";
        String senhaDigitada = "123456";

        Usuario logado = dao.efetuarLogin(emailDigitado, senhaDigitada);

        if (logado != null) {
            System.out.println("Login bem-sucedido!");
            System.out.println("Bem-vindo, " + logado.getNome() + " (" + logado.getTipo() + ")");
        } else {
            System.out.println("E-mail ou senha incorretos.");
        }
    }
}