import frontend.LoginTela;

import java.util.List;

import javax.swing.SwingUtilities;

import backend.alternativasDAO.Alternativa;
import backend.alternativasDAO.AlternativasDAO;
import backend.perguntaDAO.PerguntaDAO;

public class App {
    public static void main(String[] args) {
        // Inicia a interface de forma segura
        PerguntaDAO pergunta = new PerguntaDAO();
        System.out.println(pergunta.getPergunta(1));

        AlternativasDAO altDao = new AlternativasDAO();
        Alternativa a = altDao.getAlternativa(1); 

        if(a != null) {
            System.out.println(a);
        }

        List<Alternativa> alternativas = altDao.getAlternativasPorPergunta(1);
        for (Alternativa b : alternativas) {
            System.out.println(b); // Vai printar as 4 alternativas da ETEC uma embaixo da outra
        }

        SwingUtilities.invokeLater(() -> {
            new LoginTela().setVisible(true);
        });
    }
}