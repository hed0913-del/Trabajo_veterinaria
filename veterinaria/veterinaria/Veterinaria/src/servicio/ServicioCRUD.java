package servicio;

import dao.BaseDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServicioCRUD {
    private final BaseDAO dao;
    public ServicioCRUD(BaseDAO dao) {
        if (dao == null) throw new IllegalArgumentException("El DAO es obligatorio");
        this.dao = dao;
    }
    public List<Map<String,Object>> listar() throws SQLException { return dao.listarTodos(); }
    public Optional<Map<String,Object>> buscar(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        return dao.buscarPorId(id);
    }
    public int crear(Map<String,Object> datos) throws SQLException {
        if (datos == null || datos.isEmpty()) throw new IllegalArgumentException("Ingresá los datos del registro");
        return dao.insertar(datos);
    }
    public boolean modificar(int id, Map<String,Object> datos) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        if (datos == null || datos.isEmpty()) throw new IllegalArgumentException("No hay datos para modificar");
        return dao.actualizar(id, datos);
    }
    public boolean eliminar(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        return dao.eliminar(id);
    }
}
