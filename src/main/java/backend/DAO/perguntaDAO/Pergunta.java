//imports e package
package backend.DAO.perguntaDAO;

//classe
public class Pergunta {

    // atributos
    private final int id;
    private final String enunciado;
    private final String imagemURL;
    private final String dificuldade;
    private final int criadoPor;
    private final int ativa;

    // construtor
    public Pergunta(int id, String enunciado, String imagemURL, String dificuldade, int criadoPor, int ativa) {
        this.id = id;
        this.enunciado = enunciado;
        this.imagemURL = imagemURL;
        this.dificuldade = dificuldade;
        this.criadoPor = criadoPor;
        this.ativa = ativa;
    }

    // to string para printar no terminal (temporario para teste)
    @Override
    public String toString() {
        return """
                ------------------------------------
                ID: %d
                Pergunta: %s
                Dificuldade: %s
                Imagem: %s
                Ativa: %s
                ------------------------------------
                """.formatted(
                id,
                enunciado,
                dificuldade,
                (imagemURL != null ? imagemURL : "Sem imagem"),
                (ativa == 1 ? "Sim" : "Não"));
    }

    // metodos getters
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
