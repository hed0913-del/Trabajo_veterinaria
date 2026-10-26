package modelo;

public class Mascota {
    private int id;
    private int duenoId;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private String fechaNacimiento;
    private String color;
    private Double pesoKg;
    private String observaciones;

    public Mascota() {}

    public Mascota(int id, int duenoId, String nombre, String especie, String raza, String sexo, String fechaNacimiento, String color, Double pesoKg, String observaciones) {
        this.id = id;
        this.duenoId = duenoId;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.color = color;
        this.pesoKg = pesoKg;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDuenoId() { return duenoId; }
    public void setDuenoId(int duenoId) { this.duenoId = duenoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
