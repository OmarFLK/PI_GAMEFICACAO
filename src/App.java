// Imports 
import java.util.List;
import javax.swing.SwingUtilities;
import frontend.LoginTela;

import backend.DAO.ajudaDAO.Ajuda;
import backend.DAO.ajudaDAO.AjudaDAO;
import backend.DAO.alternativasDAO.Alternativa;
import backend.DAO.alternativasDAO.AlternativasDAO;
import backend.DAO.partidaDAO.Partida;
import backend.DAO.partidaDAO.PartidaDAO;
import backend.DAO.perguntaDAO.Pergunta;
import backend.DAO.perguntaDAO.PerguntaDAO;
import backend.DAO.usuarioDAO.Usuario;
import backend.DAO.usuarioDAO.UsuarioDAO;

//classe main
public class App {
    public static void main(String[] args) {

        //pedi para o gemini criar um código para testar os metodos que tinha feito


        System.out.println("========== INICIANDO TESTES DO SISTEMA ETEC ==========\n");

        // 1. Teste de UsuarioDAO (Simulação de Login)
        System.out.println("--- Teste de Login ---");
        UsuarioDAO userDAO = new UsuarioDAO();
        // Testando com o e-mail da professora Maria do Socorro
        Usuario profa = userDAO.efetuarLogin("socorro@etec.sp.gov.br", "etec123");
        if (profa != null) {
            System.out.println("✅ Login bem-sucedido! Bem-vindo(a), " + profa.getNome());
        } else {
            System.out.println("❌ Falha no login: verifique as credenciais no banco.");
        }

        // 2. Teste de PerguntaDAO
        System.out.println("\n--- Teste de Pergunta ---");
        PerguntaDAO perguntaDAO = new PerguntaDAO();
        Pergunta p = perguntaDAO.getPergunta(1);
        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("❌ Pergunta não encontrada.");
        }

        // 3. Teste de AlternativasDAO
        System.out.println("\n--- Teste de Alternativas da Pergunta 1 ---");
        AlternativasDAO altDAO = new AlternativasDAO();
        List<Alternativa> listaAlt = altDAO.getAlternativasPorPergunta(1);
        if (!listaAlt.isEmpty()) {
            for (Alternativa a : listaAlt) {
                System.out.println(a);
            }
        } else {
            System.out.println("❌ Nenhuma alternativa encontrada para esta pergunta.");
        }

        // 4. Teste de AjudaDAO
        System.out.println("\n--- Teste de Ajuda ---");
        AjudaDAO ajudaDAO = new AjudaDAO();
        Ajuda aj = ajudaDAO.getAjuda(1);
        if (aj != null) {
            System.out.println("Ajuda ID 1: " + aj.getNome() + " - " + aj.getDescricao());
        } else {
            System.out.println("❌ Tipo de ajuda não encontrado.");
        }

        // 5. Teste de PartidaDAO
        System.out.println("\n--- Teste de Partida ---");
        PartidaDAO partidaDAO = new PartidaDAO();
        Partida part = partidaDAO.getPartida(1);
        if (part != null) {
            System.out.println("Partida ID 1 | Usuário: " + part.getIdUsuario() + " | Pontos: " + part.getPontuacao());
            System.out.println("Iniciada em: " + part.getDataInicio());
        } else {
            System.out.println("ℹ️ Nenhuma partida com ID 1 no banco.");
        }

        System.out.println("\n========== FIM DOS TESTES - ABRINDO INTERFACE ==========");

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