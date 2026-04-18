import frontend.LoginTela;

import java.util.List;

import javax.swing.SwingUtilities;

import backend.DAO.alternativasDAO.Alternativa;
import backend.DAO.alternativasDAO.AlternativasDAO;
import backend.DAO.perguntaDAO.PerguntaDAO;

public class App {
    public static void main(String[] args) {
        // teste metodo para pegar pergunta por id
        PerguntaDAO pergunta = new PerguntaDAO();
        System.out.println(pergunta.getPergunta(1));

        //testa pegar alternativas por pergunta por id
        AlternativasDAO altDao = new AlternativasDAO();
        List<Alternativa> alternativas = altDao.getAlternativasPorPergunta(1);
        for (Alternativa a : alternativas) {
            System.out.println(a);
        }

        // Inicia a interface gráfica tela login 
        SwingUtilities.invokeLater(() -> {
            new LoginTela().setVisible(true);
        });
    }
}