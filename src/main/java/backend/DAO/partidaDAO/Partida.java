//package e imports
package backend.DAO.partidaDAO;

import java.time.LocalDateTime;

//classes
public class Partida {

    //atributos
    private final int id;
    private final int idUsuario;
    private final int pontuacao;
    private final LocalDateTime dataInicio;

    //construtor
    public Partida(int id, int idUsuario, int pontuacao, LocalDateTime dataInicio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.pontuacao = pontuacao;
        this.dataInicio = dataInicio;
    }

    //getters
    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }
}
