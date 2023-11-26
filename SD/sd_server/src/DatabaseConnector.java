import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:postgresql://159.203.83.87:5432/projeto_sd";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "5cd998b6cb3636!";

    // M�todo para obter uma conex�o com o banco de dados
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}