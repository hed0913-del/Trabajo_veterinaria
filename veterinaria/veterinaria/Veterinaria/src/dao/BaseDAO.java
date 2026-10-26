package dao;


import java.sql.*;
import java.util.*;

public abstract class BaseDAO {
    private final String table;
    private final String idColumn;

    protected BaseDAO(String table, String idColumn) {
        this.table = table;
        this.idColumn = idColumn;
    }

    protected Connection connect() throws SQLException {
        String url = System.getenv().getOrDefault("VETU_DB_URL", "jdbc:mysql://localhost:3306/veterinaria_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        String user = System.getenv().getOrDefault("VETU_DB_USER", "root");
        String password = System.getenv().getOrDefault("VETU_DB_PASSWORD", "");
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e) { throw new SQLException("No se encontró el conector MySQL en lib/", e); }
        return DriverManager.getConnection(url, user, password);
    }

    public List<Map<String, Object>> listarTodos() throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM `" + table + "` ORDER BY `" + idColumn + "` DESC";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                rows.add(row);
            }
        }
        return rows;
    }

    public Optional<Map<String, Object>> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM `" + table + "` WHERE `" + idColumn + "` = ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                ResultSetMetaData md = rs.getMetaData();
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                return Optional.of(row);
            }
        }
    }

    public int insertar(Map<String, Object> datos) throws SQLException {
        if (datos == null || datos.isEmpty()) throw new IllegalArgumentException("No hay datos para insertar");
        StringJoiner names = new StringJoiner(", ");
        StringJoiner marks = new StringJoiner(", ");
        for (String key : datos.keySet()) { validarIdentificador(key); names.add("`" + key + "`"); marks.add("?"); }
        String sql = "INSERT INTO `" + table + "` (" + names + ") VALUES (" + marks + ")";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1; for (Object value : datos.values()) ps.setObject(i++, value);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { return keys.next() ? keys.getInt(1) : -1; }
        }
    }

    public boolean actualizar(int id, Map<String, Object> datos) throws SQLException {
        if (datos == null || datos.isEmpty()) throw new IllegalArgumentException("No hay datos para actualizar");
        StringJoiner sets = new StringJoiner(", ");
        for (String key : datos.keySet()) { validarIdentificador(key); if (!key.equalsIgnoreCase(idColumn)) sets.add("`" + key + "` = ?"); }
        if (sets.length() == 0) throw new IllegalArgumentException("No hay campos actualizables");
        String sql = "UPDATE `" + table + "` SET " + sets + " WHERE `" + idColumn + "` = ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1; for (Map.Entry<String, Object> e : datos.entrySet()) if (!e.getKey().equalsIgnoreCase(idColumn)) ps.setObject(i++, e.getValue());
            ps.setInt(i, id); return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM `" + table + "` WHERE `" + idColumn + "` = ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1, id); return ps.executeUpdate() > 0; }
    }

    private static void validarIdentificador(String name) {
        if (name == null || !name.matches("[A-Za-z_][A-Za-z0-9_]*")) throw new IllegalArgumentException("Nombre de campo no válido");
    }
}
