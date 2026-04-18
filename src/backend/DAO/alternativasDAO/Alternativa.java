//imports e package
package backend.DAO.alternativasDAO;

//classe
public class Alternativa {
    
    //atributos
    private int idAlternativa;
    private int idPergunta;
    private String texto;
    private String imagemURL;
    private int correta;

    //construtor
    public Alternativa(int idAlternativa, int idPergunta, String texto, String imagemURL, int correta) {
        this.idAlternativa = idAlternativa;
        this.idPergunta = idPergunta;
        this.texto = texto;
        this.imagemURL = imagemURL;
        this.correta = correta;
    }

    //to string para printar no terminal (temporario para teste)
    @Override
    public String toString() {
        return String.format(
            "   [%s] %-20s %s", 
            (correta == 1 ? "V" : "F"), 
            texto, 
            (imagemURL != null ? "(📸 " + imagemURL + ")" : "")
        );
    }

    //metodos getters
    public int getIdAlternativa() {
        return idAlternativa;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public String getTexto() {
        return texto;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public int getCorreta() {
        return correta;
    }
    
}
