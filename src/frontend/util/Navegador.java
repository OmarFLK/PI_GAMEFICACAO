package frontend.util;

import javax.swing.JFrame;

import frontend.HomeAlunoTela;
import frontend.HomeProfessorTela;
import frontend.LoginTela;

public class Navegador {

    public static final String TIPO_ALUNO = "ALUNO";
    public static final String TIPO_PROFESSOR = "PROFESSOR";

    public static void abrirTela(JFrame telaAtual, JFrame proximaTela) {
        proximaTela.setVisible(true);
        if (telaAtual != null) {
            telaAtual.dispose();
        }
    }

    public static void abrirLogin(JFrame telaAtual) {
        abrirTela(telaAtual, new LoginTela());
    }

    public static void abrirHome(JFrame telaAtual, String tipoUsuario) {
        if (TIPO_PROFESSOR.equals(tipoUsuario)) {
            abrirTela(telaAtual, new HomeProfessorTela());
            return;
        }
        abrirTela(telaAtual, new HomeAlunoTela());
    }
}
