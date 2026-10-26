package modelo;

public class Consulta {
    private int id;
    private Integer citaId;
    private int mascotaId;
    private int veterinarioId;
    private String fechaHora;
    private String diagnostico;
    private String tratamiento;
    private Double pesoKg;
    private Double costo;
    private String observaciones;

    public Consulta() {}

    public Consulta(int id, Integer citaId, int mascotaId, int veterinarioId, String fechaHora, String diagnostico, String tratamiento, Double pesoKg, Double costo, String observaciones) {
        this.id = id;
        this.citaId = citaId;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.fechaHora = fechaHora;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.pesoKg = pesoKg;
        this.costo = costo;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getCitaId() { return citaId; }
    public void setCitaId(Integer citaId) { this.citaId = citaId; }
    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }
    public int getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(int veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }
    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
