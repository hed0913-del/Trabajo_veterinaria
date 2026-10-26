package modelo;

public class Vacuna {
    private int id;
    private String nombre;
    private String descripcion;
    private Integer intervaloDias;

    public Vacuna() {}

    public Vacuna(int id, String nombre, String descripcion, Integer intervaloDias) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.intervaloDias = intervaloDias;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getIntervaloDias() { return intervaloDias; }
    public void setIntervaloDias(Integer intervaloDias) { this.intervaloDias = intervaloDias; }
}
