package frontend;

import frontend.util.Navegador;

public class EstatisticasTela extends PerfilTela {

    public EstatisticasTela(String tipoUsuario) {
        super(tipoUsuario);
        setTitle(Navegador.TIPO_PROFESSOR.equals(tipoUsuario) ? "QuimLab - Estatisticas Professor" : "QuimLab - Estatisticas Aluno");
    }
}
