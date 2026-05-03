//imports e package
package backend.DAO.perguntaDAO;

//classe
public class Pergunta {

    //atributos
    private int id;
    private String enunciado;
    private String imagemURL;
    private String dificuldade;
    private int criadoPor;
    private int ativa;

    //construtor
    public Pergunta(int id, String enunciado, String imagemURL, String dificuldade, int criadoPor, int ativa) {
        this.id = id;
        this.enunciado = enunciado;
        this.imagemURL = imagemURL;
        this.dificuldade = dificuldade;
        this.criadoPor = criadoPor;
        this.ativa = ativa;
    }

    //to string para printar no terminal (temporario para teste)
    @Override
    public String toString() {
        return "------------------------------------------\n" +
            "ID: " + id + "\n" +
            "Pergunta: " + enunciado + "\n" +
            "Dificuldade: " + dificuldade + "\n" +
            "Imagem: " + (imagemURL != null ? imagemURL : "Sem imagem") + "\n" +
            "Ativa: " + (ativa == 1 ? "Sim" : "Não") + "\n" +
            "------------------------------------------";
    }
    
    //metodos getters
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

    public int getId() {
        return id;
    }

    public int getAtiva() {
        return ativa;
    }

    
}


