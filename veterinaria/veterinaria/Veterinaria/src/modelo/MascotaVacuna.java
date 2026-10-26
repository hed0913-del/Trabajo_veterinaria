package modelo;

public class MascotaVacuna {
    private int id;
    private int mascotaId;
    private int vacunaId;
    private Integer veterinarioId;
    private String fechaAplicacion;
    private String proximaDosis;
    private String lote;
    private String observaciones;

    public MascotaVacuna() {}

    public MascotaVacuna(int id, int mascotaId, int vacunaId, Integer veterinarioId, String fechaAplicacion, String proximaDosis, String lote, String observaciones) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.vacunaId = vacunaId;
        this.veterinarioId = veterinarioId;
        this.fechaAplicacion = fechaAplicacion;
        this.proximaDosis = proximaDosis;
        this.lote = lote;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }
    public int getVacunaId() { return vacunaId; }
    public void setVacunaId(int vacunaId) { this.vacunaId = vacunaId; }
    public Integer getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Integer veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(String fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public String getProximaDosis() { return proximaDosis; }
    public void setProximaDosis(String proximaDosis) { this.proximaDosis = proximaDosis; }
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
