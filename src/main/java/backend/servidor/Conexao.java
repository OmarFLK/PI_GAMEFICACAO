package backend.servidor;
//biblotecas
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Carrega o arquivo .env
    private static Dotenv dotenv = Dotenv.load();

    // Busca os valores pelas chaves definidas no arquivo
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    //conecta com o banco
    public static Connection conectar() throws SQLException {
        try {
            //cunversa com o .jar do lib para ler sql
            Class.forName("com.mysql.cj.jdbc.Driver");
            //retorna um objeto da Classe Connection  que recebe comandos em sql
            return DriverManager.getConnection(URL,USER,PASS);
         //trata se der erro (faz o L)
        }catch(ClassNotFoundException e){
            throw new SQLException("Driver nao encontrado", e);
        }
    }
}