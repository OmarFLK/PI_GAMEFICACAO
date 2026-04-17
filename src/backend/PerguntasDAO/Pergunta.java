package backend.PerguntasDAO;

public class Pergunta {
    private int id;
    private String enunciado;
    private String imagemURL;
    private String dificuldade;
    private int criadoPor;

    public Pergunta(String enunciado, String imagemURL, String dificuldade,int criadoPor) {
        this.enunciado = enunciado;
        this.imagemURL = imagemURL;
        this.dificuldade = dificuldade;
        this.criadoPor = criadoPor;
    }  

    public String getEnunciado() {
        return enunciado;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public int getCriadoPor() {
        return criadoPor;
    }
    
}


