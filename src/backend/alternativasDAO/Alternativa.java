package backend.alternativasDAO;

public class Alternativa {
    private int idAlternativa;
    private int idPergunta;
    private String texto;
    private String imagemURL;
    private int correta;

    public Alternativa(int idAlternativa, int idPergunta, String texto, String imagemURL, int correta) {
        this.idAlternativa = idAlternativa;
        this.idPergunta = idPergunta;
        this.texto = texto;
        this.imagemURL = imagemURL;
        this.correta = correta;
    }

    @Override
    public String toString() {
        return String.format(
            "   [%s] %-20s %s", 
            (correta == 1 ? "V" : "F"), 
            texto, 
            (imagemURL != null ? "(📸 " + imagemURL + ")" : "")
        );
    }
    
}
