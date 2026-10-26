import java.sql.Connection;
public class PruebaConexion {
 public static void main(String[] args) {
  try (Connection c = DB.connect()) {
   System.out.println("OK: conexión establecida con MySQL.");
   System.out.println("Base de datos: " + c.getCatalog());
  } catch (Exception e) {
   System.out.println("ERROR: " + e.getMessage());
   System.out.println("Verificá que MySQL esté iniciado en XAMPP y que hayas importado sql/veterinaria_db.sql.");
   System.exit(1);
  }
 }
}
