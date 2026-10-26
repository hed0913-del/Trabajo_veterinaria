import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DB {
    private static final String URL = System.getenv().getOrDefault("VETU_DB_URL", "jdbc:mysql://localhost:3306/veterinaria_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String USER = System.getenv().getOrDefault("VETU_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("VETU_DB_PASSWORD", "");
    private DB() {}
    public static Connection connect() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new SQLException("No se encontró el conector MySQL. Revisá lib/mysql-connector-j-8.0.33.jar", e); }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
