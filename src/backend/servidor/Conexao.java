package backend.servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Note o final da URL com /mydb
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root"; 
    private static final String PASS = "SuaNovaSenha123";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}